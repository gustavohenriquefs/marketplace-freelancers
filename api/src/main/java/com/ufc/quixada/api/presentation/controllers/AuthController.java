package com.ufc.quixada.api.presentation.controllers;

import com.ufc.quixada.api.application.usecases.CreateUserUseCase;
import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.infrastructure.models.UserJpaModel;
import com.ufc.quixada.api.infrastructure.services.TokenService;
import com.ufc.quixada.api.presentation.dtos.LoginDTO;
import com.ufc.quixada.api.presentation.dtos.RegisterUserDTO;
import com.ufc.quixada.api.presentation.dtos.TokenDTO;
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

    private final CreateUserUseCase createUserUseCase;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService; // Da Infra

    public AuthController(
            CreateUserUseCase createUserUseCase, AuthenticationManager authenticationManager,
            TokenService tokenService){
        this.createUserUseCase = createUserUseCase;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserDTO dto) {
        // 1. Converter DTO -> Domain Entity
        User domainUser = new User();
        domainUser.setName(dto.name());
        domainUser.setEmail(dto.email());
        domainUser.setPassword(dto.password());

        // 2. Chamar UseCase
        createUserUseCase.execute(domainUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO dto) {
        // O Login é um caso especial onde frequentemente usamos o AuthManager do Spring direto
        // pois ele já bate no banco e valida hash.

        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // O Principal retornado é o UserModel da Infra (que é UserDetails)
        var userModel = (UserJpaModel) auth.getPrincipal();

        // Gera token usando a entidade da infra (necessário para pegar roles)
        String token = tokenService.generateToken(userModel);

        return ResponseEntity.ok(new TokenDTO(token));
    }
}
