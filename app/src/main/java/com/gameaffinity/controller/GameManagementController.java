package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@PreAuthorize("hasAuthority('Administrator')")
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
        if (games.isEmpty()){
            return ResponseEntity.noContent().build();
        }
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
        if (result){
            return ResponseEntity.ok("{\"message\": \"Juego añadido.\"}");
        }else{
            return ResponseEntity.badRequest().body("{\"error\": \"Error al añadir el juego.\"}");
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar un juego", description = "Elimina un juego de la base de datos utilizando su ID.")
    public ResponseEntity<?> deleteGame(@RequestParam int gameId) {
        boolean result = gameManagementService.deleteGame(gameId);
        if (result){
            return ResponseEntity.ok("{\"message\": \"Juego eliminado.\"}");
        }else{
            return ResponseEntity.badRequest().body("{\"error\": \"Error al eliminar el juego.\"}");
        }
    }
}