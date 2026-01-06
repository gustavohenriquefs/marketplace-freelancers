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

    @Autowired
    UserJpaRepository UserRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        if (path != null && path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        if(token != null){
            var email = tokenService.validateToken(token);

            if(!email.isEmpty()) {
                UserDetails user = UserRepo.findByEmail(email).orElse(null);

                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

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