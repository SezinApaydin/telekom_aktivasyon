package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.dto.PackageResponseDto;
import com.etiya.telekomaktivasyon.repository.PackageRepository;
import com.etiya.telekomaktivasyon.service.mapper.PackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private PackageMapper packageMapper;

    public List<PackageResponseDto> getActivePackages() {
        return packageRepository.findByIsActiveTrue().stream()
                .map(packageMapper::toDto)
                .collect(Collectors.toList());
    }
}