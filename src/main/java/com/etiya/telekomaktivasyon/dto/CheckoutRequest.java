package com.etiya.telekomaktivasyon.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private Integer cartId;
    private Integer addressId;
    private String customerName;
    private String tckn;
}