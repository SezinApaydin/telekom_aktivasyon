package com.etiya.telekomaktivasyon.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class PackageResponseDto {
    private Integer id;
    private String name;
    private BigDecimal monthlyPrice;
    private Integer dataQuotaGb;
    private Integer minutes;
}