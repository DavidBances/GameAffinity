package com.gameaffinity.controller;

import com.gameaffinity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@Tag(name = "User Registration", description = "API para la gestión del registro de usuarios")
public class RegisterController {

    private final UserService userService;

    /**
     * Constructs a new RegisterController and initializes the UserService.
     */
    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user in the "Level Track" application.
     *
     * @param name     The name of the user.
     * @param email    The email address of the user.
     * @param password The password for the new account.
     * @return A {@code String} indicating the registration result.
     */
    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un nuevo usuario con nombre, email y contraseña.")
    public ResponseEntity<?> register(@RequestParam String name,
                                      @RequestParam String email,
                                      @RequestParam String password) {

        // Llama al servicio para registrar al usuario
        String result = userService.registerUser(name, email, password, "REGULAR_USER");

        // Si el registro fue exitoso
        if (result.equals("Registro exitoso")) {
            return ResponseEntity.ok().body("{\"message\": \"Usuario registrado exitosamente. Por favor, inicia sesión.\"}");
        }

        // En caso de error, devuelve un código de estado HTTP apropiado, por ejemplo, 400 (Bad Request)
        return ResponseEntity.badRequest().body("{\"error\": \"" + result + "\"}");
    }
}
