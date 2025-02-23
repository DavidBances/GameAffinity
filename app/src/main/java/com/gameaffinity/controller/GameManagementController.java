package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        List<Game> games = gameManagementService.getAllGames();
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }

    @GetMapping("/genre")
    @Operation(summary = "Obtener juegos por género", description = "Devuelve los juegos filtrados por género.")
    public ResponseEntity<List<Game>> getGamesByGenre(@RequestParam String genre) {
        List<Game> games = gameManagementService.getGamesByGenre(genre);
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }

    @GetMapping("/name")
    @Operation(summary = "Obtener juegos por nombre", description = "Devuelve los juegos filtrados por nombre.")
    public ResponseEntity<List<Game>> getGamesByName(@RequestParam String name) {
        List<Game> games = gameManagementService.getGamesByName(name);
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }

    @GetMapping("/genre-and-name")
    @Operation(summary = "Obtener juegos por género y nombre", description = "Devuelve los juegos filtrados por género y nombre.")
    public ResponseEntity<List<Game>> getGamesByGenreAndName(@RequestParam String genre, @RequestParam String name) {
        List<Game> games = gameManagementService.getGamesByGenreAndName(genre, name);
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }

    @PostMapping("/add")
    @Operation(summary = "Añadir un nuevo juego", description = "Agrega un nuevo juego a la base de datos.")
    public ResponseEntity<Map<String, Object>> addGame(
            @RequestParam String name,
            @RequestParam String genre,
            @RequestParam String priceText
    ) {
        boolean result = gameManagementService.addGame(name, genre, Double.parseDouble(priceText));
        Map<String, Object> response = new HashMap<>();
        response.put("message", result ? "Juego añadido." : "Error al añadir el juego.");
        response.put("success", result);

        return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar un juego", description = "Elimina un juego de la base de datos utilizando su ID.")
    public ResponseEntity<Map<String, Object>> deleteGame(@RequestParam int gameId) {
        boolean result = gameManagementService.deleteGame(gameId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", result ? "Juego eliminado." : "Error al eliminar el juego.");
        response.put("success", result);

        return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
