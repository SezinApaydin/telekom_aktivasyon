package com.etiya.telekomaktivasyon.controller;

import com.etiya.telekomaktivasyon.entity.SimCard;
import com.etiya.telekomaktivasyon.service.SimCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sim-cards")
public class SimCardController {

    @Autowired
    private SimCardService simCardService;

    @GetMapping
    public List<SimCard> getAvailableSimCards() {
        return simCardService.getAvailableSimCards();
    }
}