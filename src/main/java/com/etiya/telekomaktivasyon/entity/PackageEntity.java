package com.etiya.telekomaktivasyon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "packages", schema = "telekom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "monthly_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyPrice;

    @Column(name = "data_quota_gb")
    private Integer dataQuotaGb;

    private Integer minutes;

    @Column(name = "sms_count")
    private Integer smsCount;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}