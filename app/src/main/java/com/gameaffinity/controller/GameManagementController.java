package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameManagementService;
import com.gameaffinity.util.SteamApiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Game Management", description = "API para la gestión de juegos")
public class GameManagementController {

    private final GameManagementService gameManagementService;
    private final SteamApiClient steamApiClient;

    public GameManagementController() {
        this.gameManagementService = new GameManagementService();
        this.steamApiClient = new SteamApiClient();
    }

    @GetMapping("/all")
    @Operation(summary = "Obtener todos los juegos", description = "Recupera una lista de todos los juegos en la base de datos.")
    public List<Game> getAllGames() {
        return gameManagementService.getAllGames();
    }

    @PostMapping("/add")
    @Operation(summary = "Añadir un nuevo juego", description = "Agrega un nuevo juego a la base de datos.")
    public boolean addGame(
            @RequestParam String name,
            @RequestParam String genre,
            @RequestParam String priceText) {
        try {
            return gameManagementService.addGame(name, genre, priceText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El precio debe ser un número válido.");
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar un juego", description = "Elimina un juego de la base de datos utilizando su ID.")
    public boolean deleteGame(@RequestParam int gameId) {
        return gameManagementService.deleteGame(gameId);
    }
}
