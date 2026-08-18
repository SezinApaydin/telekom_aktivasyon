package com.etiya.telekomaktivasyon.repository;

import com.etiya.telekomaktivasyon.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    List<Device> findByIsActiveTrueAndStockQuantityGreaterThan(Integer quantity);
}