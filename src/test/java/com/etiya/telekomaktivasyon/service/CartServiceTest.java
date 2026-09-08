package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.entity.*;
import com.etiya.telekomaktivasyon.exception.BusinessException;
import com.etiya.telekomaktivasyon.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private PackageRepository packageRepository;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private SimCardRepository simCardRepository;

    @InjectMocks
    private CartService cartService;

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        cart.setId(1);
    }

    @Test
    void shouldThrowWhenDeviceOutOfStock() {
        Device device = new Device();
        device.setId(1);
        device.setModel("iPhone 15");
        device.setStockQuantity(0);

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(deviceRepository.findById(1)).thenReturn(Optional.of(device));

        assertThrows(BusinessException.class, () -> cartService.addDeviceToCart(1, 1));
    }

    @Test
    void shouldThrowWhenSimCardNotAvailable() {
        SimCard simCard = new SimCard();
        simCard.setId(1);
        simCard.setMsisdn("5321112233");
        simCard.setStatus("reserved");

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(simCardRepository.findById(1)).thenReturn(Optional.of(simCard));

        assertThrows(BusinessException.class, () -> cartService.addSimCardToCart(1, 1));
    }

    @Test
    void shouldThrowWhenCartAlreadyHasSimCard() {
        SimCard simCard = new SimCard();
        simCard.setId(2);
        simCard.setMsisdn("5321112244");
        simCard.setStatus("available");

        CartItem existingSimItem = new CartItem();
        existingSimItem.setItemType("sim");

        when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
        when(simCardRepository.findById(2)).thenReturn(Optional.of(simCard));
        when(cartItemRepository.findByCart_Id(1)).thenReturn(List.of(existingSimItem));

        assertThrows(BusinessException.class, () -> cartService.addSimCardToCart(1, 2));
    }
}