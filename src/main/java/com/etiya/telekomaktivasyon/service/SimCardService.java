package com.etiya.telekomaktivasyon.service;

import com.etiya.telekomaktivasyon.entity.SimCard;
import com.etiya.telekomaktivasyon.repository.SimCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SimCardService {

    @Autowired
    private SimCardRepository simCardRepository;

    public List<SimCard> getAvailableSimCards() {
        return simCardRepository.findByStatus("available");
    }
}