package com.gameaffinity.controller;

import com.gameaffinity.model.Friendship;
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

    // Obtener el userId basado en el token JWT (interfaz interna para reutilizar lógica en otros métodos)
    private int getUserIdFromToken() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); // Extrae email del token
        return friendshipService.getUserIdByEmail(email); // Busca el userId usando el email
    }

    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> sendFriendRequest(@RequestParam String friendEmail) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String result = friendshipService.sendFriendRequest(email, friendEmail);
        Map<String, Object> response = new HashMap<>();

        response.put("message", result.equals("true") ? "Solicitud enviada." : result);
        response.put("success", result.equals("true"));

        if (result.equals("true")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("/requests")
    @Operation(summary = "Obtener solicitudes de amistad", description = "Obtiene las solicitudes de amistad pendientes de un usuario.")
    public ResponseEntity<List<Friendship>> getFriendRequests() {
        int userId = getUserIdFromToken();
        List<Friendship> requests = friendshipService.getFriendRequests(userId);
        if (requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/respond")
    @Operation(summary = "Responder solicitud de amistad", description = "Acepta o rechaza una solicitud de amistad.")
    public ResponseEntity<?> respondToFriendRequest(@RequestBody Friendship friendship, @RequestParam String status) {
        boolean result = friendshipService.respondToFriendRequest(friendship, status);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Respuesta enviada.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al enviar respuesta.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/friends")
    @Operation(summary = "Obtener lista de amigos", description = "Devuelve la lista de amigos de un usuario.")
    public ResponseEntity<List<UserBase>> getFriends() {
        int userId = getUserIdFromToken();
        List<UserBase> friends = friendshipService.getFriends(userId);
        if (friends.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/friend-id")
    @Operation(summary = "Obtener ID de usuario por email", description = "Busca un usuario por email y devuelve su ID.")
    public int getFriendIdByEmail(@RequestParam String friendEmail) {
        return friendshipService.getUserIdByEmail(friendEmail);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar amigo", description = "Elimina un amigo de la lista de amistades.")
    public ResponseEntity<?> deleteFriend(@RequestParam int friendId) {
        int userId = getUserIdFromToken();
        boolean result = friendshipService.deleteFriend(userId, friendId);

        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Amigo eliminado.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al eliminar amigo.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }
}
