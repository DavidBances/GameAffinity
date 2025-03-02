package com.gameaffinity.controller;

import com.gameaffinity.exception.GameAffinityException;
import com.gameaffinity.model.Game;
import com.gameaffinity.model.GameState;
import com.gameaffinity.model.LibraryGames;
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

    @GetMapping("/allGames")
    @Operation(summary = "Obtener juegos del usuario autenticado", description = "Devuelve la lista de juegos del usuario autenticado.")
    public ResponseEntity<List<Game>> getGamesByUser() {
        List<Game> games = libraryService.getGamesByUserId(getUserIdFromToken());
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }

    @GetMapping("/gamesByUserAndGameName")
    @Operation(summary = "Obtener juegos del usuario autenticado filtrados por nombre", description = "Devuelve la lista de juegos del usuario autenticado filtrados por nombre.")
    public ResponseEntity<List<Game>> getGamesByUserAndName(@RequestParam String gameName) {
        List<Game> games = libraryService.findGamesByUserAndName(getUserIdFromToken(), gameName);
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }

    @GetMapping("/gamesByUserAndGenre")
    @Operation(summary = "Obtener juegos del usuario autenticado filtrados por género", description = "Devuelve la lista de juegos del usuario autenticado filtrados por género.")
    public ResponseEntity<List<Game>> getGamesByUserAndGenre(@RequestParam String genre) {
        List<Game> games = libraryService.findGamesByUserAndGenre(getUserIdFromToken(), genre);
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }


    @GetMapping("/gamesByUserAndGenreAndName")
    @Operation(summary = "Obtener juegos del usuario autenticado filtrados por género y nombre", description = "Devuelve la lista de juegos del usuario autenticado filtrados por género y nombre.")
    public ResponseEntity<List<Game>> getGamesByUserAndGenreAndName(@RequestParam String genre, @RequestParam String gameName) {
        List<Game> games = libraryService.findGamesByUserAndGenreAndName(getUserIdFromToken(), genre, gameName);
        return games.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(games);
    }


    @PostMapping("/add")
    @Operation(summary = "Añadir juego a la biblioteca", description = "Añade un juego a la biblioteca del usuario autenticado.")
    public ResponseEntity<Map<String, Object>> addGameToLibrary(@RequestParam String gameName) {
        String decodedGameName = URLDecoder.decode(gameName, StandardCharsets.UTF_8);
        Map<String, Object> response = new HashMap<>();

        try {
            boolean result = libraryService.addGameToLibrary(getUserIdFromToken(), decodedGameName);
            response.put("message", result ? "Juego añadido a tu biblioteca." : "Error al añadir juego.");
            response.put("success", result);
            return ResponseEntity.ok(response);
        } catch (GameAffinityException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Eliminar juego de la biblioteca", description = "Elimina un juego de la biblioteca del usuario autenticado.")
    public ResponseEntity<Map<String, Object>> removeGameFromLibrary(@RequestParam String gameName) {
        String decodedGameName = URLDecoder.decode(gameName, StandardCharsets.UTF_8);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = libraryService.removeGameFromLibrary(getUserIdFromToken(), decodedGameName);
            response.put("message", result ? "Juego eliminado de tu biblioteca." : "Error al eliminar juego.");
            response.put("success", result);
            return ResponseEntity.ok(response);
        } catch (GameAffinityException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }
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

    @PutMapping("/update/score")
    @Operation(summary = "Actualizar estado del juego", description = "Actualiza el estado de un juego en la biblioteca del usuario autenticado.")
    public ResponseEntity<Map<String, Object>> updateGameScore(@RequestParam int gameId, @RequestParam int score) {
        boolean result = libraryService.updateGameScore(gameId, getUserIdFromToken(), score);
        Map<String, Object> response = new HashMap<>();
        response.put("message", result ? "Estado del juego actualizado." : "Error al actualizar el estado del juego.");
        response.put("success", result);

        return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/update/review")
    @Operation(summary = "Actualizar estado del juego", description = "Actualiza el estado de un juego en la biblioteca del usuario autenticado.")
    public ResponseEntity<Map<String, Object>> updateGameReview(@RequestParam int gameId, @RequestParam String review) {
        boolean result = libraryService.updateGameReview(gameId, getUserIdFromToken(), review);
        Map<String, Object> response = new HashMap<>();
        response.put("message", result ? "Reseña del juego actualizada." : "Error al actualizar la reseña del juego.");
        response.put("success", result);

        return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/update/timePlayed")
    @Operation(summary = "Actualizar estado del juego", description = "Actualiza el estado de un juego en la biblioteca del usuario autenticado.")
    public ResponseEntity<Map<String, Object>> updateTimePlayed(@RequestParam int gameId, @RequestParam Double timePlayed) {
        boolean result = libraryService.updateTimePlayed(gameId, getUserIdFromToken(), timePlayed);
        Map<String, Object> response = new HashMap<>();
        response.put("message", result ? "Tiempo jugado actualizado." : "Error al actualizar el tiempo de juego.");
        response.put("success", result);

        return result ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/genres")
    @Operation(summary = "Obtener todos los géneros", description = "Devuelve una lista de todos los géneros de la biblioteca del usuario.")
    public ResponseEntity<List<String>> getAllGenres() {
        List<String> genres = libraryService.getAllGenres(getUserIdFromToken());
        return genres.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(genres);
    }

    @GetMapping("/gameInfo/{gameId}")
    @Operation(summary = "Obtener información del juego en la biblioteca",
            description = "Devuelve los detalles del juego en la biblioteca del usuario autenticado.")
    public ResponseEntity<LibraryGames> getLibraryGameInfo(@PathVariable int gameId) {
        LibraryGames libraryGame = null;
        try {
            libraryGame = libraryService.findLibraryGame(gameId, getUserIdFromToken());
        } catch (GameAffinityException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(libraryGame);
    }
}
