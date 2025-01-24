package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.JwtService;
import com.gameaffinity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "API para la gestión de usuarios")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService; // Usa el constructor actual
        this.jwtService = jwtService;
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Autenticar usuario", description = "Autentica a un usuario mediante su email y contraseña, devolviendo un token JWT.")
    public String authenticate(@RequestParam String email, @RequestParam String password) {
        UserBase user = userService.authenticate(email, password);
        if (user != null) {
            // Devuelve un token JWT en lugar del UserBase completo
            return jwtService.generateToken(user.getId(), email, Collections.singletonList(user.getRole()));
        }
        throw new IllegalArgumentException("Credenciales inválidas.");
    }

    @PutMapping("/update-profile")
    @Operation(summary = "Actualizar perfil de usuario", description = "Actualiza la información del perfil del usuario.")
    public ResponseEntity<?> updateProfile(
            @RequestParam String password,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newEmail,
            @RequestParam(required = false) String newPassword
    ) {
        String emailFromToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean result = userService.updateUserProfile(emailFromToken, password, newName, newEmail, newPassword);
        if(result){
            return ResponseEntity.ok("{\"message\": \"Perfil actualizado.\"}");
        }else{
            return ResponseEntity.badRequest().body("{\"error\": \"Error al actualizar el perfil.\"}");
        }
    }

    @GetMapping("/getRole")
    public ResponseEntity<?> getRole() {

        String emailFromToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(emailFromToken);

        // 3. Busca el usuario en la base de datos
        UserBase user = userService.getUserByEmail(emailFromToken);

        if (user == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        // 4. Retorna el rol del usuario
        return ResponseEntity.ok(user.getRole());
    }
}