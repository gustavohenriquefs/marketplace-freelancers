package com.ufc.quixada.api.presentation.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "O email não pode estar vazio")
        @Email(message = "O email é inválido")
        String email,

        @NotBlank(message = "A senha não pode estar vazia")
        String password
) {
}
