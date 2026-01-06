package com.ufc.quixada.api.presentation.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FreelancerRequestDTO (
    String id,
    @NotBlank(message = "O nome não pode estar vazio")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    String name,

    @NotBlank(message = "O email não pode estar vazio")
    @Email(message = "O email é inválido")
    String email,

    @NotBlank(message = "A área não pode estar vazia")
    @Size(max = 100, message = "A área deve ter no máximo 100 caracteres")
    String area
) {}
