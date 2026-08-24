package com.etiya.telekomaktivasyon.controller;

import com.etiya.telekomaktivasyon.entity.Address;
import com.etiya.telekomaktivasyon.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        return addressService.createAddress(address);
    }
}