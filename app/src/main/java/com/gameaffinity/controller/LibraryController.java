package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.model.GameState;
import com.gameaffinity.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/library")
@Tag(name = "Library Management", description = "API para la gestión de la biblioteca de juegos del usuario")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    private int getUserIdFromToken() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return libraryService.getUserIdByEmail(email);
    }

    @GetMapping("/user")
    @Operation(summary = "Obtener juegos del usuario autenticado", description = "Devuelve la lista de juegos del usuario autenticado.")
    public ResponseEntity<List<Game>> getGamesByUser() {
        List<Game> games = libraryService.getGamesByUserId(getUserIdFromToken());
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }

    @PostMapping("/add")
    @Operation(summary = "Añadir juego a la biblioteca", description = "Añade un juego a la biblioteca del usuario autenticado.")
    public ResponseEntity<Map<String, Object>> addGameToLibrary(@RequestParam String gameName) {
        String decodedGameName = URLDecoder.decode(gameName, StandardCharsets.UTF_8);
        boolean result = libraryService.addGameToLibrary(getUserIdFromToken(), decodedGameName);
        Map<String, Object> response = new HashMap<>();
        response.put("message", result ? "Juego añadido a tu biblioteca." : "Error al añadir juego.");
        response.put("success", result);

        return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Eliminar juego de la biblioteca", description = "Elimina un juego de la biblioteca del usuario autenticado.")
    public ResponseEntity<Map<String, Object>> removeGameFromLibrary(@RequestParam String gameName) {
        String decodedGameName = URLDecoder.decode(gameName, StandardCharsets.UTF_8);
        boolean result = libraryService.removeGameFromLibrary(getUserIdFromToken(), decodedGameName);
        Map<String, Object> response = new HashMap<>();
        response.put("message", result ? "Juego eliminado de tu biblioteca." : "Error al eliminar juego.");
        response.put("success", result);

        return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/update/state")
    @Operation(summary = "Actualizar estado del juego", description = "Actualiza el estado de un juego en la biblioteca del usuario autenticado.")
    public ResponseEntity<Map<String, Object>> updateGameState(@RequestParam int gameId, @RequestParam GameState newState) {
        boolean result = libraryService.updateGameState(gameId, getUserIdFromToken(), newState);
        Map<String, Object> response = new HashMap<>();
        response.put("message", result ? "Estado del juego actualizado." : "Error al actualizar el estado del juego.");
        response.put("success", result);

        return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/genres")
    @Operation(summary = "Obtener todos los géneros", description = "Devuelve una lista de todos los géneros de la biblioteca del usuario.")
    public ResponseEntity<List<String>> getAllGenres() {
        List<String> genres = libraryService.getAllGenres(getUserIdFromToken());
        return genres.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(genres);
    }

    @GetMapping("/avgScore/{gameId}")
    @Operation(summary = "Obtener puntuación promedio de un juego", description = "Devuelve la puntuación promedio de un juego en la biblioteca del usuario.")
    public ResponseEntity<Integer> getGameScore(@PathVariable int gameId) {
        return ResponseEntity.ok(libraryService.getGameScore(gameId));
    }

    @GetMapping("/avgTimePlayed/{gameId}")
    @Operation(summary = "Obtener tiempo promedio jugado", description = "Devuelve el tiempo promedio jugado de un juego en la biblioteca del usuario.")
    public ResponseEntity<Double> getTimePlayed(@PathVariable int gameId) {
        return ResponseEntity.ok(libraryService.getTimePlayed(gameId));
    }
}
