package com.etiya.telekomaktivasyon.repository;

import com.etiya.telekomaktivasyon.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PackageRepository extends JpaRepository<PackageEntity, Integer> {
    List<PackageEntity> findByIsActiveTrue();
}