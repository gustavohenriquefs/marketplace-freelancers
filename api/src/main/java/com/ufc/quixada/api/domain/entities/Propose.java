package com.ufc.quixada.api.domain.entities;

import com.ufc.quixada.api.domain.enums.ProposeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Propose {
    private Long id;
    private ProposeStatus status;
    private BigDecimal price;
    private int duration;
    private String description;
    private LocalDate createdAt;

    private Freelancer freelancer;
}
