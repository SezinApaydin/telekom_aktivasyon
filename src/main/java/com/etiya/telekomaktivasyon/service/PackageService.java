package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.entity.PackageEntity;
import com.etiya.telekomaktivasyon.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    public List<PackageEntity> getActivePackages() {
        return packageRepository.findByIsActiveTrue();
    }
}