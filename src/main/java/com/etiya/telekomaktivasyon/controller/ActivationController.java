package com.etiya.telekomaktivasyon.controller;

import com.etiya.telekomaktivasyon.entity.Order;
import com.etiya.telekomaktivasyon.service.ActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class ActivationController {

    @Autowired
    private ActivationService activationService;

    @PostMapping("/{orderId}/activate")
    public Order activate(@PathVariable Integer orderId) {
        return activationService.activateOrder(orderId);
    }
}