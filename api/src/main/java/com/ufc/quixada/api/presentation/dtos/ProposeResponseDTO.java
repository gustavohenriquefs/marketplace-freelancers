package com.ufc.quixada.api.presentation.dtos;

import com.ufc.quixada.api.domain.enums.ProposeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProposeResponseDTO(
        Long id,
        ProposeStatus status,
        BigDecimal price,
        int duration,
        String description,
        LocalDate createdAt,
        LocalDate updatedAt,
        Long freelancerId
) {
}
