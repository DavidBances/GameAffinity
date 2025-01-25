package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Game Management", description = "API para la gestión de juegos")
public class GameManagementController {

    private final GameManagementService gameManagementService;

    @Autowired
    public GameManagementController(GameManagementService gameManagementService) {
        this.gameManagementService = gameManagementService;
    }

    @GetMapping("/all")
    @Operation(summary = "Obtener todos los juegos", description = "Recupera una lista de todos los juegos en la base de datos.")
    public ResponseEntity<List<Game>> getAllGames() {
        System.out.println("Entrando en el método getAllGames");

        List<Game> games = gameManagementService.getAllGames();
        if (games.isEmpty()) {
            System.out.println("No se encontraron juegos");
            return ResponseEntity.noContent().build();
        }
        System.out.println("Juegos encontrados: " + games.size());
        return ResponseEntity.ok(games);
    }

    @PostMapping("/add")
    @Operation(summary = "Añadir un nuevo juego", description = "Agrega un nuevo juego a la base de datos.")
    public ResponseEntity<?> addGame(
            @RequestParam String name,
            @RequestParam String genre,
            @RequestParam String priceText
    ) {
        boolean result = gameManagementService.addGame(name, genre, priceText);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Juego añadido.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al añadir el juego.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar un juego", description = "Elimina un juego de la base de datos utilizando su ID.")
    public ResponseEntity<?> deleteGame(@RequestParam int gameId) {
        boolean result = gameManagementService.deleteGame(gameId);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Juego eliminado.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al eliminar el juego.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }
}