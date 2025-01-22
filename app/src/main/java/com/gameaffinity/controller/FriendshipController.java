package com.gameaffinity.controller;

import com.gameaffinity.model.Friendship;
import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
@Tag(name = "Friendship Management", description = "API para la gestión de amistades entre usuarios")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController() {
        this.friendshipService = new FriendshipService();
    }

    @PostMapping("/request")
    @Operation(summary = "Enviar solicitud de amistad", description = "Envía una solicitud de amistad de un usuario a otro.")
    public boolean sendFriendRequest(@RequestBody Friendship friendship) {
        return friendshipService.sendFriendRequest(friendship);
    }

    @GetMapping("/requests/{userId}")
    @Operation(summary = "Obtener solicitudes de amistad", description = "Obtiene las solicitudes de amistad pendientes de un usuario.")
    public List<Friendship> getFriendRequests(@PathVariable int userId) {
        return friendshipService.getFriendRequests(userId);
    }

    @PutMapping("/respond")
    @Operation(summary = "Responder solicitud de amistad", description = "Acepta o rechaza una solicitud de amistad.")
    public boolean respondToFriendRequest(@RequestBody Friendship friendship, @RequestParam String status) {
        return friendshipService.respondToFriendRequest(friendship, status);
    }

    @GetMapping("/friends/{userId}")
    @Operation(summary = "Obtener lista de amigos", description = "Devuelve la lista de amigos de un usuario.")
    public List<UserBase> getFriends(@PathVariable int userId) {
        return friendshipService.getFriends(userId);
    }

    @GetMapping("/user-id")
    @Operation(summary = "Obtener ID de usuario por email", description = "Busca un usuario por email y devuelve su ID.")
    public int getUserIdByEmail(@RequestParam String userEmail) {
        return friendshipService.getUserIdByEmail(userEmail);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar amigo", description = "Elimina un amigo de la lista de amistades.")
    public boolean deleteFriend(@RequestParam int userId, @RequestParam int friendId) {
        return friendshipService.deleteFriend(userId, friendId);
    }
}
