package com.gameaffinity.security;

import com.gameaffinity.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // No hay token, continuar
            return;
        }

        final String token = authHeader.substring(7); // Eliminar "Bearer "

        try {
            validateTokenAndExtractInfo(token);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Responder con 401 si el token es inválido
            response.getWriter().write("Unauthorized: Invalid or expired token.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    protected void validateTokenAndExtractInfo(String token) {
        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Token inválido.");
        }

        String email = jwtService.extractEmail(token);
        int userId = jwtService.extractUserId(token);
        List<String> roles = jwtService.extractRoles(token);

        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Autenticación en el contexto de seguridad
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
