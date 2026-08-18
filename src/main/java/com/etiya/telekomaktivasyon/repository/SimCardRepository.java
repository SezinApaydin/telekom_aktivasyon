package com.etiya.telekomaktivasyon.repository;

import com.etiya.telekomaktivasyon.entity.SimCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SimCardRepository extends JpaRepository<SimCard, Integer> {
    List<SimCard> findByStatus(String status);
}