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

import java.util.List;

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
    public ResponseEntity<?> sendFriendRequest(@RequestBody Friendship friendship) {
        boolean result = friendshipService.sendFriendRequest(friendship);
        if (result) {
            return ResponseEntity.status(201).body("{\"message\": \"Solicitud enviada.\"}");
        }
        return ResponseEntity.badRequest().body("{\"error\": \"No se pudo enviar la solicitud.\"}");
    }


    @GetMapping("/requests")
    @Operation(summary = "Obtener solicitudes de amistad", description = "Obtiene las solicitudes de amistad pendientes de un usuario.")
    public ResponseEntity<List<Friendship>> getFriendRequests() {
        int userId = getUserIdFromToken();
        List<Friendship> requests = friendshipService.getFriendRequests(userId);
        if(requests.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/respond")
    @Operation(summary = "Responder solicitud de amistad", description = "Acepta o rechaza una solicitud de amistad.")
    public ResponseEntity<?> respondToFriendRequest(@RequestBody Friendship friendship, @RequestParam String status) {
        boolean result = friendshipService.respondToFriendRequest(friendship, status);
        if(result){
            return ResponseEntity.status(201).body(true);
        }else{
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/friends")
    @Operation(summary = "Obtener lista de amigos", description = "Devuelve la lista de amigos de un usuario.")
    public ResponseEntity<List<UserBase>> getFriends() {
        int userId = getUserIdFromToken();
        List<UserBase> friends = friendshipService.getFriends(userId);
       if(friends.isEmpty()){
           return ResponseEntity.noContent().build();
       }
       return ResponseEntity.ok(friends);
    }

    @GetMapping("/friend-id/{friendEmail}")
    @Operation(summary = "Obtener ID de usuario por email", description = "Busca un usuario por email y devuelve su ID.")
    public int getFriendIdByEmail(@PathVariable String friendEmail) {
        return friendshipService.getUserIdByEmail(friendEmail);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar amigo", description = "Elimina un amigo de la lista de amistades.")
    public ResponseEntity<?> deleteFriend(@RequestParam int friendId) {
        int userId = getUserIdFromToken();
        boolean result = friendshipService.deleteFriend(userId, friendId);

        if (result) {
            return ResponseEntity.status(201).body("{\"message\": \"Amigo eliminado.\"}");
        }else{
            return ResponseEntity.badRequest().body("{\"error\": \"No se pudo eliminar.\"}");
        }
    }
}
