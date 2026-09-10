package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.entity.*;
import com.etiya.telekomaktivasyon.repository.*;
import com.etiya.telekomaktivasyon.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private SimCardRepository simCardRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Cart createCart(String customerIdentifier) {
        Cart cart = new Cart();
        cart.setCustomerIdentifier(customerIdentifier);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setStatus("active");
        cart = cartRepository.save(cart);
        updateCartJsonData(cart);
        return cart;
    }

    public CartItem addPackageToCart(Integer cartId, Integer packageId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Sepet bulunamadı: " + cartId));
        PackageEntity packageEntity = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Paket bulunamadı: " + packageId));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setItemType("package");
        item.setPackageEntity(packageEntity);
        item.setQuantity(1);
        CartItem saved = cartItemRepository.save(item);
        updateCartJsonData(cart);
        return saved;
    }

    public CartItem addDeviceToCart(Integer cartId, Integer deviceId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Sepet bulunamadı: " + cartId));
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Cihaz bulunamadı: " + deviceId));

        if (device.getStockQuantity() == null || device.getStockQuantity() <= 0) {
            throw new BusinessException("Bu cihaz stokta yok: " + device.getModel());
        }

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setItemType("device");
        item.setDevice(device);
        item.setQuantity(1);
        CartItem saved = cartItemRepository.save(item);
        updateCartJsonData(cart);
        return saved;
    }

    public CartItem addSimCardToCart(Integer cartId, Integer simCardId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Sepet bulunamadı: " + cartId));
        SimCard simCard = simCardRepository.findById(simCardId)
                .orElseThrow(() -> new RuntimeException("SIM kart bulunamadı: " + simCardId));
        if (!"available".equals(simCard.getStatus())) {
            throw new BusinessException("Bu SIM kart müsait değil: " + simCard.getMsisdn());
        }

        long existingSimCount = cartItemRepository.findByCart_Id(cartId).stream()
                .filter(ci -> "sim".equals(ci.getItemType()))
                .count();
        if (existingSimCount > 0) {
            throw new BusinessException("Sepette zaten bir SIM kart var, birden fazla eklenemez.");
        }

        simCard.setStatus("reserved");
        simCard.setIsReserved(true);
        simCard.setReservedAt(LocalDateTime.now());
        simCardRepository.save(simCard);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setItemType("sim");
        item.setSimCard(simCard);
        item.setQuantity(1);
        CartItem saved = cartItemRepository.save(item);
        updateCartJsonData(cart);
        return saved;
    }

    public void removeCartItem(Integer cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Sepet öğesi bulunamadı: " + cartItemId));
        Cart cart = item.getCart();
        cartItemRepository.deleteById(cartItemId);
        updateCartJsonData(cart);
    }

    public List<CartItem> getCartItems(Integer cartId) {
        return cartItemRepository.findByCart_Id(cartId);
    }

    private void updateCartJsonData(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCart_Id(cart.getId());

        ObjectNode root = objectMapper.createObjectNode();
        root.put("id", cart.getId());
        root.put("customerIdentifier", cart.getCustomerIdentifier());
        root.put("status", cart.getStatus());
        root.put("created_at", cart.getCreatedAt() != null ? cart.getCreatedAt().toString() : null);

        ArrayNode itemsArray = objectMapper.createArrayNode();
        for (CartItem item : items) {
            ObjectNode itemNode = objectMapper.createObjectNode();
            itemNode.put("id", item.getId());
            ArrayNode properties = objectMapper.createArrayNode();

            if ("device".equals(item.getItemType()) && item.getDevice() != null) {
                Device device = item.getDevice();
                itemNode.put("name", device.getModel());
                itemNode.put("type", "DEVICE");
                addProperty(properties, "brand", device.getBrand());
                addProperty(properties, "price", String.valueOf(device.getPrice()));
            } else if ("package".equals(item.getItemType()) && item.getPackageEntity() != null) {
                PackageEntity pkg = item.getPackageEntity();
                itemNode.put("name", pkg.getName());
                itemNode.put("type", "PACKAGE");
                addProperty(properties, "monthlyPrice", String.valueOf(pkg.getMonthlyPrice()));
                addProperty(properties, "dataQuotaGb", String.valueOf(pkg.getDataQuotaGb()));
            } else if ("sim".equals(item.getItemType()) && item.getSimCard() != null) {
                SimCard sim = item.getSimCard();
                itemNode.put("name", sim.getMsisdn());
                itemNode.put("type", "SIM_CARD");
                addProperty(properties, "msisdn", sim.getMsisdn());
            }

            itemNode.put("quantity", item.getQuantity());
            itemNode.set("properties", properties);
            itemsArray.add(itemNode);
        }
        root.set("items", itemsArray);

        cart.setJsonData(objectMapper.writeValueAsString(root));
        cartRepository.save(cart);
    }

    private void addProperty(ArrayNode properties, String name, String value) {
        ObjectNode prop = objectMapper.createObjectNode();
        prop.put("propertyName", name);
        prop.put("propertyValue", value);
        properties.add(prop);
    }
}