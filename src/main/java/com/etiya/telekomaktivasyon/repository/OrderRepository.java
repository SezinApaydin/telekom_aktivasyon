package com.etiya.telekomaktivasyon.repository;

import com.etiya.telekomaktivasyon.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}