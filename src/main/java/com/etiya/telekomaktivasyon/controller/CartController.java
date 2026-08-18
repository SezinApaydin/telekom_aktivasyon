package com.etiya.telekomaktivasyon.controller;

import com.etiya.telekomaktivasyon.entity.Cart;
import com.etiya.telekomaktivasyon.entity.CartItem;
import com.etiya.telekomaktivasyon.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public Cart createCart(@RequestParam String customerIdentifier) {
        return cartService.createCart(customerIdentifier);
    }

    @PostMapping("/{cartId}/items/package/{packageId}")
    public CartItem addPackage(@PathVariable Integer cartId, @PathVariable Integer packageId) {
        return cartService.addPackageToCart(cartId, packageId);
    }

    @PostMapping("/{cartId}/items/device/{deviceId}")
    public CartItem addDevice(@PathVariable Integer cartId, @PathVariable Integer deviceId) {
        return cartService.addDeviceToCart(cartId, deviceId);
    }

    @PostMapping("/{cartId}/items/sim/{simCardId}")
    public CartItem addSimCard(@PathVariable Integer cartId, @PathVariable Integer simCardId) {
        return cartService.addSimCardToCart(cartId, simCardId);
    }

    @DeleteMapping("/items/{cartItemId}")
    public void removeItem(@PathVariable Integer cartItemId) {
        cartService.removeCartItem(cartItemId);
    }

    @GetMapping("/{cartId}/items")
    public List<CartItem> getCartItems(@PathVariable Integer cartId) {
        return cartService.getCartItems(cartId);
    }
}