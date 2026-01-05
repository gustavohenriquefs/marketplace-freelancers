package com.ufc.quixada.api.presentation.dtos;

public record RegisterUserDTO(
        String name,
        String email,
        String password
) {

}
