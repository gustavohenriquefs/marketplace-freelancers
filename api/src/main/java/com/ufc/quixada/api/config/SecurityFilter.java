package com.ufc.quixada.api.config;

import com.ufc.quixada.api.infrastructure.repositories.UserJpaRepository;
import com.ufc.quixada.api.infrastructure.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    // Injetamos o repositório da INFRA, pois precisamos do UserModel (UserDetails)
    // e não da entidade de Domínio pura.
    @Autowired
    UserJpaRepository UserRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Se já existe autenticação (ex: outro filtro/handler setou), não mexa.
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Não tenta validar token nas rotas públicas de auth.
        String path = request.getRequestURI();
        if (path != null && path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        if(token != null){
            var email = tokenService.validateToken(token);

            if(!email.isEmpty()) {
                // Busca o UserModel (que é um UserDetails) no banco
                UserDetails user = UserRepo.findByEmail(email).orElse(null);

                if (user != null) {
                    // O Spring Security monta a autenticação baseada nas Roles que estão dentro do UserModel
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    // Salva no contexto da requisição
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}