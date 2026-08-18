package com.etiya.telekomaktivasyon.controller;

import com.etiya.telekomaktivasyon.entity.PackageEntity;
import com.etiya.telekomaktivasyon.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping
    public List<PackageEntity> getActivePackages() {
        return packageService.getActivePackages();
    }
}