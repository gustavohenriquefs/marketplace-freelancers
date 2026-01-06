package com.ufc.quixada.api.presentation.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProposeRequestDTO(
    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que 0")
    BigDecimal price,

    @Positive(message = "A duração deve ser maior que 0")
    int duration,

    @NotBlank(message = "A descrição não pode estar vazia")
    @Size(max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
    String description
) {
}
