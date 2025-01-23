package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Game Management", description = "API para la gestión de juegos")
public class GameManagementController {

    private final GameManagementService gameManagementService;

    public GameManagementController() {
        this.gameManagementService = new GameManagementService();
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
            @RequestParam String priceText,
            HttpServletRequest request // Para usar el email autenticado
    ) {
        String email = (String) request.getAttribute("userEmail");
        // Aquí puedes decidir verificar permisos con el email del token si es necesario.
        return gameManagementService.addGame(name, genre, priceText);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar un juego", description = "Elimina un juego de la base de datos utilizando su ID.")
    public boolean deleteGame(@RequestParam int gameId, HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        // Verifica el email si necesitas restricciones aquí
        return gameManagementService.deleteGame(gameId);
    }
}