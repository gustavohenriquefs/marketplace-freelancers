package com.ufc.quixada.api.presentation.dtos;

import jakarta.validation.constraints.*;

public record LoginRequestDTO(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String password
) {}
