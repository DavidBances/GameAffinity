package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-management")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "User Management", description = "API para la gestión de usuarios")
public class UserManagementController {

    private final UserService userService;

    public UserManagementController() {
        this.userService = new UserService();
    }

    @GetMapping("/all")
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista con todos los usuarios.")
    public List<UserBase> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update-role")
    @Operation(summary = "Actualizar rol de usuario", description = "Asigna un nuevo rol a un usuario.")
    public boolean updateUserRole(@RequestBody UserBase user, @RequestParam String newRole) {
        return userService.updateUserRole(user, newRole);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario de la base de datos.")
    public boolean deleteUser(@RequestBody UserBase user) {
        return userService.deleteUser(user.getId());
    }
}
