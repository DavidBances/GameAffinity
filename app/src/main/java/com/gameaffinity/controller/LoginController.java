package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@Tag(name = "Login", description = "API para la autenticación de usuarios")
public class LoginController {

    private final UserService userService;

    /**
     * Constructs a new LoginController and initializes the UserService.
     */
    public LoginController() {
        this.userService = new UserService();
    }

    /**
     * Authenticates a user using their email and password.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @return A {@code UserBase} object representing the authenticated user,
     * or {@code null} if authentication fails.
     */
    @PostMapping
    @Operation(summary = "Autenticar usuario", description = "Autentica a un usuario usando su email y contraseña.")
    public UserBase login(@RequestParam String email, @RequestParam String password) {
        return userService.authenticate(email, password);
    }
}
