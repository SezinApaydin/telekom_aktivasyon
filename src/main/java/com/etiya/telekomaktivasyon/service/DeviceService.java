package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.entity.Device;
import com.etiya.telekomaktivasyon.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAvailableDevices() {
        return deviceRepository.findByIsActiveTrueAndStockQuantityGreaterThan(0);
    }
}