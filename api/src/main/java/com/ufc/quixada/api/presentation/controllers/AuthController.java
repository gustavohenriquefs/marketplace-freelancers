package com.ufc.quixada.api.presentation.controllers;

import com.ufc.quixada.api.application.usecases.CreateUser;
import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.infrastructure.models.UserJpaModel;
import com.ufc.quixada.api.infrastructure.services.TokenService;
import com.ufc.quixada.api.presentation.dtos.LoginRequestDTO;
import com.ufc.quixada.api.presentation.dtos.RegisterUserRequestDTO;
import com.ufc.quixada.api.presentation.dtos.TokenResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CreateUser createUser;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService; // Da Infra

    public AuthController(
            CreateUser createUser, AuthenticationManager authenticationManager,
            TokenService tokenService){
        this.createUser = createUser;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequestDTO dto) {
        // 1. Converter DTO -> Domain Entity
        User domainUser = new User();
        domainUser.setName(dto.name());
        domainUser.setEmail(dto.email());
        domainUser.setPassword(dto.password());

        // 2. Chamar UseCase
        createUser.execute(domainUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        // O Login é um caso especial onde frequentemente usamos o AuthManager do Spring direto
        // pois ele já bate no banco e valida hash.

        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // O Principal retornado é o UserModel da Infra (que é UserDetails)
        var userModel = (UserJpaModel) auth.getPrincipal();

        // Gera token usando a entidade da infra (necessário para pegar roles)
        String token = tokenService.generateToken(userModel);

        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
