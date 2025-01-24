package com.gameaffinity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor con inyección necesaria
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/register", "/api/login", "/api/users/authenticate").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("Administrator")
                        .requestMatchers("/api/users/**").hasAuthority("Regular_User")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class) // Registra el filtro antes del filtro por defecto de autenticación
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}