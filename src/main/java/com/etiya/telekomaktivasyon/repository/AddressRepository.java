package com.etiya.telekomaktivasyon.repository;

import com.etiya.telekomaktivasyon.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}