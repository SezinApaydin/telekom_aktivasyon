package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.entity.*;
import com.etiya.telekomaktivasyon.repository.*;
import com.etiya.telekomaktivasyon.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Cart createCart(String customerIdentifier) {
        Cart cart = new Cart();
        cart.setCustomerIdentifier(customerIdentifier);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setStatus("active");
        return cartRepository.save(cart);
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
        return cartItemRepository.save(item);
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
        return cartItemRepository.save(item);
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
        simCardRepository.save(simCard);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setItemType("sim");
        item.setSimCard(simCard);
        item.setQuantity(1);
        return cartItemRepository.save(item);
    }

    public void removeCartItem(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public List<CartItem> getCartItems(Integer cartId) {
        return cartItemRepository.findByCart_Id(cartId);
    }
}