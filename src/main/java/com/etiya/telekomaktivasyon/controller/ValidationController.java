package com.etiya.telekomaktivasyon.controller;

import com.etiya.telekomaktivasyon.service.TcknValidator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validate")
public class ValidationController {

    @GetMapping("/tckn")
    public boolean validateTckn(@RequestParam String tckn) {
        return TcknValidator.isValid(tckn);
    }
}