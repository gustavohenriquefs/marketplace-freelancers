package com.ufc.quixada.api.presentation.dtos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateProposeRequestDTO(
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    BigDecimal price,

    @Min(value = 1, message = "Duração deve ser no mínimo 1 dia")
    int duration,

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "Descrição deve ter entre 10 e 1000 caracteres")
    String description
) {}
