package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // Obtener el userId basado en el token JWT (interfaz interna para reutilizar lógica en otros métodos)
    private int getUserIdFromToken() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); // Extrae email del token
        return libraryService.getUserIdByEmail(email); // Busca el userId usando el email
    }

    // Obtener juegos del usuario autenticado
    @GetMapping("/user")
    @Operation(summary = "Obtener juegos del usuario autenticado", description = "Devuelve la lista de juegos del usuario autenticado.")
    public ResponseEntity<List<Game>> getGamesByUser() {
        int userId = getUserIdFromToken();
        List<Game> games = libraryService.getGamesByUserId(userId);
        if (games.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(games);
    }

    // Obtener juegos de un amigo (nuevo método)
    @GetMapping("/friendId")
    @Operation(summary = "Obtener la biblioteca de un amigo", description = "Devuelve la biblioteca de juegos de un usuario especificado si el usuario autenticado tiene permisos.")
    public ResponseEntity<List<Game>> getGamesByFriend(@RequestParam int friendId) {
        int currentUserId = getUserIdFromToken(); // ID del usuario autenticado
        if (!libraryService.checkFriendship(currentUserId, friendId)) {
            throw new SecurityException("Acceso denegado: no tienes permisos para ver esta biblioteca.");
        }
        List<Game> friendGames = libraryService.getGamesByUserId(friendId);
        if (friendGames.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(friendGames);
    }

    @GetMapping("/user/genre")
    @Operation(summary = "Obtener juegos por género", description = "Devuelve los juegos del usuario autenticado filtrados por género.")
    public ResponseEntity<List<Game>> getGamesByGenre(@RequestParam String genre) {
        int userId = getUserIdFromToken();

        List<Game> games = libraryService.getGamesByGenreUser(userId, genre);
        if (games.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(games);
    }

    @GetMapping("/user/name")
    @Operation(summary = "Obtener juegos por nombre", description = "Devuelve los juegos del usuario autenticado filtrados por nombre.")
    public ResponseEntity<List<Game>> getGamesByName(@RequestParam String name) {
        int userId = getUserIdFromToken();

        List<Game> games = libraryService.getGamesByNameUser(userId, name);
        if (games.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(games);
    }

    @GetMapping("/user/genre-and-name")
    @Operation(summary = "Obtener juegos por género y nombre", description = "Devuelve los juegos del usuario autenticado filtrados por género y nombre.")
    public ResponseEntity<List<Game>> getGamesByGenreAndName(@RequestParam String genre, @RequestParam String name) {
        int userId = getUserIdFromToken();
        List<Game> games = libraryService.getGamesByGenreAndNameUser(userId, genre, name);
        if (games.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(games);
    }

    @PostMapping("/add")
    @Operation(summary = "Añadir juego a la biblioteca", description = "Añade un juego a la biblioteca del usuario autenticado.")
    public ResponseEntity<?> addGameToLibrary(@RequestParam String gameName) throws Exception {
        try {
            // Decodificación manual del gameName
            String decodedGameName = URLDecoder.decode(gameName, StandardCharsets.UTF_8);

            int userId = getUserIdFromToken();
            boolean result = libraryService.addGameToLibrary(userId, decodedGameName);

            Map<String, Object> response = new HashMap<>();
            if (result) {
                response.put("message", "Juego añadido a tu biblioteca.");
                response.put("success", true);  // Incluimos el resultado booleano
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Error al añadir juego a la biblioteca.");
                response.put("success", false);  // Incluimos el resultado booleano
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al procesar el nombre del juego.");
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @DeleteMapping("/remove")
    @Operation(summary = "Eliminar juego de la biblioteca", description = "Elimina un juego de la biblioteca del usuario autenticado.")
    public ResponseEntity<?> removeGameFromLibrary(@RequestParam String gameName) {
        try {
            String decodedGameName = URLDecoder.decode(gameName, StandardCharsets.UTF_8);

            int userId = getUserIdFromToken();
            boolean result = libraryService.removeGameFromLibrary(userId, decodedGameName);
            Map<String, Object> response = new HashMap<>();
            if (result) {
                response.put("message", "Juego eliminado de tu biblioteca.");
                response.put("success", true);  // Incluimos el resultado booleano
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Error al eliminar juego de la biblioteca.");
                response.put("success", false);  // Incluimos el resultado booleano
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al procesar el nombre del juego.");
            errorResponse.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update/state")
    @Operation(summary = "Actualizar estado del juego", description = "Actualiza el estado de un juego en la biblioteca del usuario autenticado.")
    public ResponseEntity<?> updateGameState(@RequestParam int gameId, @RequestParam String newState) {
        int userId = getUserIdFromToken();
        boolean result = libraryService.updateGameState(gameId, userId, newState);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Estado del juego actualizado.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al actualizar el estado del juego.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/score")
    @Operation(summary = "Actualizar puntuación del juego", description = "Actualiza la puntuación de un juego en la biblioteca del usuario autenticado.")
    public ResponseEntity<?> updateGameScore(@RequestParam int gameId, @RequestParam int score) {
        int userId = getUserIdFromToken();
        boolean result = libraryService.updateGameScore(gameId, userId, score);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Puntuación del juego actualizada.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al actualizar la puntuación del juego.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/review")
    @Operation(summary = "Actualizar puntuación del juego", description = "Actualiza la puntuación de un juego en la biblioteca del usuario autenticado.")
    public ResponseEntity<?> updateReview(@RequestParam int gameId, @RequestParam String review) {
        int userId = getUserIdFromToken();

        String decodedReview = URLDecoder.decode(review, StandardCharsets.UTF_8);

        boolean result = libraryService.updateGameReview(gameId, userId, decodedReview);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Reseña del juego actualizada.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al actualizar la reseña del juego.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update/time_played")
    @Operation(summary = "Actualizar puntuación del juego", description = "Actualiza la puntuación de un juego en la biblioteca del usuario autenticado.")
    public ResponseEntity<?> updateTimePlayed(@RequestParam int gameId, @RequestParam Double timePlayed) {
        int userId = getUserIdFromToken();
        boolean result = libraryService.updateTimePlayed(gameId, userId, timePlayed);
        Map<String, Object> response = new HashMap<>();
        if (result) {
            response.put("message", "Tiempo de juego del juego actualizada.");
            response.put("success", true);  // Incluimos el resultado booleano
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Error al actualizar el tiempo de juego del juego.");
            response.put("success", false);  // Incluimos el resultado booleano
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/genres")
    @Operation(summary = "Obtener todos los géneros", description = "Devuelve una lista de todos los géneros de la biblioteca.")
    public ResponseEntity<List<String>> getAllGenres() {
        List<String> genres = libraryService.getAllGenres();
        if (genres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/avgScore/{gameId}")
    @Operation(summary = "Obtener puntuación promedio de un juego", description = "Devuelve la puntuación promedio de un juego.")
    public ResponseEntity<Integer> getGameScore(@PathVariable int gameId) {
        int score = libraryService.getGameScore(gameId);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/avgTimePlayed/{gameId}")
    @Operation(summary = "Obtener puntuación promedio de un juego", description = "Devuelve la puntuación promedio de un juego.")
    public ResponseEntity<Double> getTimePlayed(@PathVariable int gameId) {
        double timePlayed = libraryService.getTimePlayed(gameId);
        return ResponseEntity.ok(timePlayed);
    }
}