package com.etiya.telekomaktivasyon.controller;

import com.etiya.telekomaktivasyon.entity.Device;
import com.etiya.telekomaktivasyon.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public List<Device> getAvailableDevices() {
        return deviceService.getAvailableDevices();
    }
}