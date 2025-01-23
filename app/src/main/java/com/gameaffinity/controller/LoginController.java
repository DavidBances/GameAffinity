package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.JwtService;
import com.gameaffinity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * Constructor sin parámetros, inicializa manualmente los servicios requeridos.
     */
    public LoginController(JwtService jwtService
    ) {
        this.userService = new UserService(); // Inicialización manual
        this.jwtService = jwtService;  // Inicialización manual
    }

    /**
     * Endpoint para autenticar usuarios y devolver un token JWT.
     *
     * @param email    Email del usuario
     * @param password Contraseña del usuario
     * @return Un token JWT válido si las credenciales son correctas, o un error de autenticación.
     */
    @PostMapping
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        UserBase user = userService.authenticate(email, password);

        // Si la autenticación falla
        if (user == null) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        // Genera y devuelve el token JWT
        String token = jwtService.generateToken(email, Collections.singletonList(user.getRole()));
        System.out.println(token);
        return ResponseEntity.ok(token);
    }
}