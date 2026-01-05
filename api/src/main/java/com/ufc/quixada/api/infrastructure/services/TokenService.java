package com.ufc.quixada.api.infrastructure.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ufc.quixada.api.infrastructure.models.UserJpaModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Defina essa propriedade no application.properties: api.security.token.secret=SuaSenhaSecreta
    @Value("${api.security.token.secret}")
    private String secret;

    // Gera o token (recebe o UserModel da infra, pois ele tem os dados de login)
    public String generateToken(UserJpaModel user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("quixada-api") // Nome da sua aplicação
                    .withSubject(user.getEmail()) // O "dono" do token
                    .withExpiresAt(genExpirationDate()) // Expira em 2h
                    .withClaim("id", user.getId()) // Adiciona o ID do usuário como claim
                    .withClaim("contractorId", user.getContractorProfile().getId()) // Adiciona o contratorId como claim
                    .withClaim("freelancerId", user.getFreelancerProfile().getId()) // Adiciona o freelancerId como claim, se existir
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // Valida o token e retorna o Email (Subject) se for válido
    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("quixada-api") // Tem que bater com o emissor acima
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return ""; // Token inválido ou expirado
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
