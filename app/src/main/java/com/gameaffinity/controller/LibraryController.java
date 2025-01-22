package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener juegos por usuario", description = "Devuelve la lista de juegos de un usuario específico.")
    public List<Game> getGamesByUserId(@PathVariable int userId) {
        return libraryService.getGamesByUserId(userId);
    }

    @GetMapping("/user/{userId}/genre")
    @Operation(summary = "Obtener juegos por género", description = "Devuelve los juegos de un usuario filtrados por género.")
    public List<Game> getGamesByGenreUser(@PathVariable int userId, @RequestParam String genre) {
        return libraryService.getGamesByGenreUser(userId, genre);
    }

    @GetMapping("/user/{userId}/name")
    @Operation(summary = "Obtener juegos por nombre", description = "Devuelve los juegos de un usuario filtrados por nombre.")
    public List<Game> getGamesByNameUser(@PathVariable int userId, @RequestParam String name) {
        return libraryService.getGamesByNameUser(userId, name);
    }

    @GetMapping("/user/{userId}/genre-and-name")
    @Operation(summary = "Obtener juegos por género y nombre", description = "Devuelve los juegos de un usuario filtrados por género y nombre.")
    public List<Game> getGamesByGenreAndNameUser(@PathVariable int userId, @RequestParam String genre, @RequestParam String name) {
        return libraryService.getGamesByGenreAndNameUser(userId, genre, name);
    }

    @PostMapping("/add")
    @Operation(summary = "Añadir juego a la biblioteca", description = "Añade un juego a la biblioteca de un usuario.")
    public boolean addGameToLibrary(@RequestParam int userId, @RequestParam String gameName) throws Exception {
        return libraryService.addGameToLibrary(userId, gameName);
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Eliminar juego de la biblioteca", description = "Elimina un juego de la biblioteca de un usuario.")
    public boolean removeGameFromLibrary(@RequestParam int userId, @RequestParam int gameId) {
        return libraryService.removeGameFromLibrary(userId, gameId);
    }

    @PutMapping("/update/state")
    @Operation(summary = "Actualizar estado del juego", description = "Actualiza el estado de un juego en la biblioteca de un usuario.")
    public boolean updateGameState(@RequestParam int gameId, @RequestParam int userId, @RequestParam String newState) {
        return libraryService.updateGameState(gameId, userId, newState);
    }

    @PutMapping("/update/score")
    @Operation(summary = "Actualizar puntuación del juego", description = "Actualiza la puntuación de un juego en la biblioteca de un usuario.")
    public boolean updateGameScore(@RequestParam int gameId, @RequestParam int userId, @RequestParam int score) {
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
    @Operation(summary = "Obtiene la puntuación total de un juego", description = "Devuelve la puntuación medía de un juego")
    public int getGameScore(@PathVariable int gameId){
        return libraryService.getGameScore(gameId);
    }
}
