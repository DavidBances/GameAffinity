package com.gameaffinity.service;

import com.gameaffinity.model.UserRole;
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

    public String generateToken(int userId, String email, List<UserRole> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("id", userId)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // 🔹 Ahora usa getSignKey()
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey()) // 🔹 Ahora usa getSignKey()
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado.");
        } catch (UnsupportedJwtException e) {
            System.out.println("Token no soportado.");
        } catch (MalformedJwtException e) {
            System.out.println("Token mal formado.");
        } catch (SignatureException e) {
            System.out.println("Firma del token no válida.");
        } catch (IllegalArgumentException e) {
            System.out.println("Token vacío o nulo.");
        }
        return false;
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public int extractUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Integer.class);
    }

    public List<String> extractRoles(String token) {
        Claims claims = getClaims(token);
        return claims.get("roles", List.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) // 🔹 Ahora usa getSignKey()
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
