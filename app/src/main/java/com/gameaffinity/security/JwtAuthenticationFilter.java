package com.gameaffinity.security;

import com.gameaffinity.service.JwtService;

import com.google.j2objc.annotations.AutoreleasePool;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
        String email = jwtService.extractEmail(token);  // Extraer email
        int userId = jwtService.extractUserId(token);  // Extraer ID
        List<String> roles = jwtService.extractRoles(token);  // Extraer roles como lista

        // Convertir roles a SimpleGrantedAuthority
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Autenticación con el email como principal y el ID como detalles
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        // Establecer detalles (puedes guardar el ID como detalle)
        authenticationToken.setDetails(userId);

        // Establece la autenticación en el contexto de seguridad de Spring Security
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}