package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@Tag(name = "Library Management", description = "API para la gestión de la biblioteca de juegos del usuario")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController() {
        this.libraryService = new LibraryService();
    }

    // Obtener el userId basado en el token JWT (interfaz interna para reutilizar lógica en otros métodos)
    private int getUserIdFromToken() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); // Extrae email del token
        return libraryService.getUserIdByEmail(email); // Busca el userId usando el email
    }

    // Obtener juegos del usuario autenticado
    @GetMapping("/user")
    @Operation(summary = "Obtener juegos del usuario autenticado", description = "Devuelve la lista de juegos del usuario autenticado.")
    public List<Game> getGamesByUser() {
        int userId = getUserIdFromToken();
        return libraryService.getGamesByUserId(userId);
    }

    // Obtener juegos de un amigo (nuevo método)
    @GetMapping("/friend/{userId}")
    @Operation(summary = "Obtener la biblioteca de un amigo", description = "Devuelve la biblioteca de juegos de un usuario especificado si el usuario autenticado tiene permisos.")
    public List<Game> getGamesByFriend(@PathVariable int userId) {
        // Verificar si el usuario autenticado tiene permiso de ver esta biblioteca
        int currentUserId = getUserIdFromToken(); // ID del usuario autenticado
        if (!libraryService.checkFriendship(currentUserId, userId)) {
            throw new SecurityException("Acceso denegado: no tienes permisos para ver esta biblioteca.");
        }
        return libraryService.getGamesByUserId(userId); // Devuelve la biblioteca de juegos del amigo
    }

    @GetMapping("/user/genre")
    @Operation(summary = "Obtener juegos por género", description = "Devuelve los juegos del usuario autenticado filtrados por género.")
    public List<Game> getGamesByGenre(@RequestParam String genre) {
        int userId = getUserIdFromToken();
        return libraryService.getGamesByGenreUser(userId, genre);
    }

    @GetMapping("/user/name")
    @Operation(summary = "Obtener juegos por nombre", description = "Devuelve los juegos del usuario autenticado filtrados por nombre.")
    public List<Game> getGamesByName(@RequestParam String name) {
        int userId = getUserIdFromToken();
        return libraryService.getGamesByNameUser(userId, name);
    }

    @GetMapping("/user/genre-and-name")
    @Operation(summary = "Obtener juegos por género y nombre", description = "Devuelve los juegos del usuario autenticado filtrados por género y nombre.")
    public List<Game> getGamesByGenreAndName(@RequestParam String genre, @RequestParam String name) {
        int userId = getUserIdFromToken();
        return libraryService.getGamesByGenreAndNameUser(userId, genre, name);
    }

    @PostMapping("/add")
    @Operation(summary = "Añadir juego a la biblioteca", description = "Añade un juego a la biblioteca del usuario autenticado.")
    public boolean addGameToLibrary(@RequestParam String gameName) throws Exception {
        int userId = getUserIdFromToken();
        return libraryService.addGameToLibrary(userId, gameName);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Eliminar juego de la biblioteca", description = "Elimina un juego de la biblioteca del usuario autenticado.")
    public boolean removeGameFromLibrary(@RequestParam int gameId) {
        int userId = getUserIdFromToken();
        return libraryService.removeGameFromLibrary(userId, gameId);
    }

    @PutMapping("/update/state")
    @Operation(summary = "Actualizar estado del juego", description = "Actualiza el estado de un juego en la biblioteca del usuario autenticado.")
    public boolean updateGameState(@RequestParam int gameId, @RequestParam String newState) {
        int userId = getUserIdFromToken();
        return libraryService.updateGameState(gameId, userId, newState);
    }

    @PutMapping("/update/score")
    @Operation(summary = "Actualizar puntuación del juego", description = "Actualiza la puntuación de un juego en la biblioteca del usuario autenticado.")
    public boolean updateGameScore(@RequestParam int gameId, @RequestParam int score) {
        int userId = getUserIdFromToken();
        return libraryService.updateGameScore(gameId, userId, score);
    }

    @GetMapping("/all")
    @Operation(summary = "Obtener todos los juegos", description = "Devuelve la lista de todos los juegos de la biblioteca.")
    public List<Game> getAllGames() {
        return libraryService.getAllGames();
    }

    @GetMapping("/genres")
    @Operation(summary = "Obtener todos los géneros", description = "Devuelve una lista de todos los géneros de la biblioteca.")
    public List<String> getAllGenres() {
        return libraryService.getAllGenres();
    }

    @GetMapping("/avgScore/{gameId}")
    @Operation(summary = "Obtener puntuación de un juego", description = "Devuelve la puntuación promedio de un juego.")
    public int getGameScore(@PathVariable int gameId) {
        return libraryService.getGameScore(gameId);
    }
}