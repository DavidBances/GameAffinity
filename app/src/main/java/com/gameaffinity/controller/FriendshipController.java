package com.gameaffinity.controller;

import com.gameaffinity.exception.GameAffinityException;
import com.gameaffinity.exception.UserNotFoundException;
import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.FriendshipStatus;
import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friendships")
@Tag(name = "Friendship Management", description = "API para la gestión de amistades entre usuarios")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    // Obtener el email del usuario autenticado basado en el token JWT
    private String getUserEmailFromToken() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/request")
    @Operation(summary = "Enviar solicitud de amistad", description = "Envía una solicitud de amistad a otro usuario.")
    public ResponseEntity<Map<String, Object>> sendFriendRequest(@RequestParam String friendEmail) {
        String userEmail = getUserEmailFromToken();
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("message", "Solicitud enviada.");
            response.put("success", friendshipService.sendFriendRequest(userEmail, friendEmail));
            return ResponseEntity.ok(response);
        } catch (GameAffinityException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/requests")
    @Operation(summary = "Obtener solicitudes de amistad", description = "Obtiene las solicitudes de amistad pendientes del usuario autenticado.")
    public ResponseEntity<List<Friendship>> getFriendRequests() {
        String userEmail = getUserEmailFromToken();
        List<Friendship> requests = friendshipService.getFriendRequests(userEmail);
        return requests.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(requests);
    }

    @PutMapping("/respond")
    @Operation(summary = "Responder solicitud de amistad", description = "Acepta o rechaza una solicitud de amistad.")
    public ResponseEntity<Map<String, Object>> respondToFriendRequest(@RequestParam int friendshipId, @RequestParam FriendshipStatus status) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("message", "Respuesta enviada.");
            response.put("success", friendshipService.respondToFriendRequest(friendshipId, status));
            return ResponseEntity.ok(response);
        } catch (GameAffinityException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/friends")
    @Operation(summary = "Obtener lista de amigos", description = "Devuelve la lista de amigos del usuario autenticado.")
    public ResponseEntity<List<UserBase>> getFriends() {
        String userEmail = getUserEmailFromToken();
        List<UserBase> friends = friendshipService.getFriends(userEmail);
        return friends.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(friends);
    }

    @GetMapping("/friend-id")
    @Operation(summary = "Obtener ID de usuario por email", description = "Busca un usuario por email y devuelve su ID.")
    public ResponseEntity<Map<String, Object>> getFriendIdByEmail(@RequestParam String friendEmail) {
        int userId = friendshipService.getUserIdByEmail(friendEmail);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar amigo", description = "Elimina a un usuario de la lista de amistades.")
    public ResponseEntity<Map<String, Object>> deleteFriend(@RequestParam String friendEmail) {
        String userEmail = getUserEmailFromToken();
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("message", "Amigo eliminado correctamente");
            response.put("success", friendshipService.deleteFriend(userEmail, friendEmail));
            return ResponseEntity.ok(response);
        } catch (GameAffinityException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
