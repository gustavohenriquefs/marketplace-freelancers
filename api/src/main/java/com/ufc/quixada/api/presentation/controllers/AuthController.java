package com.ufc.quixada.api.presentation.controllers;

import com.ufc.quixada.api.application.usecases.CreateUser;
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

    private final CreateUser createUserUseCase;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(
            CreateUser createUserUseCase, AuthenticationManager authenticationManager,
            TokenService tokenService){
        this.createUserUseCase = createUserUseCase;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserDTO dto) {
        User domainUser = new User();
        domainUser.setName(dto.name());
        domainUser.setEmail(dto.email());
        domainUser.setPassword(dto.password());

        createUserUseCase.execute(domainUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO dto) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var userModel = (UserJpaModel) auth.getPrincipal();

        String token = tokenService.generateToken(userModel);

        return ResponseEntity.ok(new TokenDTO(token));
    }
}
