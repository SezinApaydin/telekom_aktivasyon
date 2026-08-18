package com.etiya.telekomaktivasyon.repository;

import com.etiya.telekomaktivasyon.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}