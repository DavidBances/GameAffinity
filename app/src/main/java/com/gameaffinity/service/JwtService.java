package com.gameaffinity.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private static final String SECRET_KEY = "mySecretKey1234567890987654321mySecretKey1234567890!";
    private static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(int userId, String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email) // Añade el email del usuario
                .claim("id", userId) // Añade el ID del usuario
                .claim("roles", roles) // Añade roles al payload
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiración
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Firma
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true; // Si no lanza excepciones, es válido
        } catch (JwtException e) {
            return false; // Si hay error, el token es inválido o expirado
        }
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject(); // Generalmente el email se almacena en `sub` (Subject)
    }

    public int extractUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Integer.class); // Extraer el ID como Long
    }

    public List<String> extractRoles(String token) {
        Claims claims = getClaims(token);
        return claims.get("roles", List.class); // Asegúrate de guardar los roles como `List`
    }


    // Extrae los Claims del token
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}