package com.etiya.telekomaktivasyon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "sim_cards", schema = "telekom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 15)
    private String msisdn;

    @Column(unique = true, length = 22)
    private String iccid;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "is_reserved", nullable = false)
    private Boolean isReserved;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;
}