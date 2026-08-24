package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.dto.CheckoutRequest;
import com.etiya.telekomaktivasyon.entity.*;
import com.etiya.telekomaktivasyon.exception.BusinessException;
import com.etiya.telekomaktivasyon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckoutService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    public Order checkout(CheckoutRequest request) {
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new BusinessException("Sepet bulunamadı: " + request.getCartId()));

        if (!"active".equals(cart.getStatus())) {
            throw new BusinessException("Bu sepet zaten tamamlanmış veya süresi dolmuş.");
        }

        List<CartItem> items = cartItemRepository.findByCart_Id(cart.getId());
        if (items.isEmpty()) {
            throw new BusinessException("Sepet boş, sipariş oluşturulamaz.");
        }

        if (!TcknValidator.isValid(request.getTckn())) {
            throw new BusinessException("Geçersiz TCKN: " + request.getTckn());
        }

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new BusinessException("Adres bulunamadı: " + request.getAddressId()));

        Order order = new Order();
        order.setCart(cart);
        order.setAddress(address);
        order.setCustomerName(request.getCustomerName());
        order.setTckn(request.getTckn());
        order.setStatus("pending");
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        cart.setStatus("checked_out");
        cartRepository.save(cart);

        return order;
    }
}