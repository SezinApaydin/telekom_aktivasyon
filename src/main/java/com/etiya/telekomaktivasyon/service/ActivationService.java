package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.entity.*;
import com.etiya.telekomaktivasyon.exception.BusinessException;
import com.etiya.telekomaktivasyon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivationService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private SimCardRepository simCardRepository;

    private final RestClient restClient = RestClient.create();

    private static final String ACTIVATION_URL = "http://localhost:3000/api/activate";

    public Order activateOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Sipariş bulunamadı: " + orderId));

        if ("activated".equals(order.getStatus())) {
            throw new BusinessException("Bu sipariş zaten aktive edilmiş.");
        }

        List<CartItem> items = cartItemRepository.findByCart_Id(order.getCart().getId());
        CartItem simItem = items.stream()
                .filter(ci -> "sim".equals(ci.getItemType()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Bu siparişte SIM kartı yok, aktivasyon yapılamaz."));

        SimCard simCard = simItem.getSimCard();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("msisdn", simCard.getMsisdn());
        requestBody.put("orderId", order.getId());

        try {
            restClient.post()
                    .uri(ACTIVATION_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);
        } catch (HttpServerErrorException e) {
            throw new BusinessException("Şebeke tarafında hata oluştu, aktivasyon başarısız. Lütfen tekrar deneyin.");
        } catch (Exception e) {
            throw new BusinessException("Aktivasyon servisine ulaşılamadı. Node.js servisinin çalıştığından emin olun.");
        }

        order.setStatus("activated");
        orderRepository.save(order);

        simCard.setStatus("activated");
        simCardRepository.save(simCard);

        return order;
    }
}