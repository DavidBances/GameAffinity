package com.gameaffinity.security;

import com.gameaffinity.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extraer el encabezado "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String token;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Continuar con los demás filtros si no hay token
            return;
        }

        // Extraer el token quitando "Bearer "
        token = authHeader.substring(7);

        try {
            // Llama a tu método para validar el token y establecer la autenticación
            validateTokenAndExtractInfo(token);
        } catch (Exception e) {
            // Podrías agregar log o manejar errores específicos
            SecurityContextHolder.clearContext();
        }

        // Continuar con el resto de los filtros
        filterChain.doFilter(request, response);
    }

    protected void validateTokenAndExtractInfo(String token) {
        String email = jwtService.extractEmail(token); // Extrae el email
        List<String> roles = jwtService.extractRoles(token); // Extrae los roles como una lista

        // Transformar roles a SimpleGrantedAuthority
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Aquí puedes usar `authorities` para autenticar al usuario con Spring Security
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        // Establece la autenticación en el contexto de seguridad de Spring Security
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}