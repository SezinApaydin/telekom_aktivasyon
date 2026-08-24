package com.etiya.telekomaktivasyon.controller;

import com.etiya.telekomaktivasyon.dto.CheckoutRequest;
import com.etiya.telekomaktivasyon.entity.Order;
import com.etiya.telekomaktivasyon.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping
    public Order checkout(@RequestBody CheckoutRequest request) {
        return checkoutService.checkout(request);
    }
}