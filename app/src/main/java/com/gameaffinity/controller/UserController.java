package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "API para la gestión de usuarios")
public class UserController {

    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Autenticar usuario", description = "Autentica a un usuario mediante su email y contraseña.")
    public UserBase authenticate(@RequestParam String email, @RequestParam String password) {
        return userService.authenticate(email, password);
    }

    @PutMapping("/update-profile")
    @Operation(summary = "Actualizar perfil de usuario", description = "Actualiza la información del perfil del usuario.")
    public boolean updateProfile(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newEmail,
            @RequestParam(required = false) String newPassword
    ) {
        return userService.updateUserProfile(email, password, newName, newEmail, newPassword);
    }
}
