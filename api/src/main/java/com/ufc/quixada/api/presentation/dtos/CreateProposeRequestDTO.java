package com.ufc.quixada.api.presentation.dtos;

import java.math.BigDecimal;

public record CreateProposeRequestDTO(
    BigDecimal price,
    int duration,
    String description
) {
}
