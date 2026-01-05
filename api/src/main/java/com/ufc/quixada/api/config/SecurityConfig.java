package com.ufc.quixada.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Qualifier("authenticationEntryPoint")
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
                        // 1. Libera o Swagger (usando a lista acima)
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()

                        // 2. Libera Login e Registro
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()

                        // 4. O resto bloqueado
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(exceptionHandlerAuthenticationEntryPoint)
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