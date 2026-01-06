package com.ufc.quixada.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Qualifier("delegatedAuthenticationEntryPoint")
    @Autowired private ExceptionHandlerAuthenticationEntryPoint exceptionHandlerAuthenticationEntryPoint;

    @Autowired private SecurityFilter securityFilter;
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/docs",
            "/api-docs-object"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Você precisa se autenticar para acessar esse recurso."))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Você não tem permissão para acessar esse recurso."))
                )
                .cors(
                        httpSecurityCorsConfigurer ->
                                httpSecurityCorsConfigurer.configurationSource(
                                        request -> {
                                            var cors = new CorsConfiguration();
                                            cors.applyPermitDefaultValues();
                                            cors.addAllowedMethod("*");
                                            return cors;
                                        }))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}