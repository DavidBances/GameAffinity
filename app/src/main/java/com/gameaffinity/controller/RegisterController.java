package com.gameaffinity.controller;

import com.gameaffinity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@Tag(name = "User Registration", description = "API para la gestión del registro de usuarios")
public class RegisterController {

    private final UserService userService;

    /**
     * Constructs a new RegisterController and initializes the UserService.
     */
    public RegisterController() {
        this.userService = new UserService();
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
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password) {
        return userService.registerUser(name, email, password, "REGULAR_USER");
    }
}
