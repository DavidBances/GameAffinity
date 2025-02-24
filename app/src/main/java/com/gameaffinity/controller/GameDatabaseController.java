package com.gameaffinity.controller;

import com.gameaffinity.util.GameDatabaseLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Scanner;

@RestController
@RequestMapping("/api/database")
public class GameDatabaseController {

    private final GameDatabaseLoader gameDatabaseLoader;

    public GameDatabaseController(GameDatabaseLoader gameDatabaseLoader) {
        this.gameDatabaseLoader = gameDatabaseLoader;
    }

    // 🔹 2️⃣ Endpoint que recibe la confirmación y ejecuta la carga
    @PostMapping("/load-games")
    public ResponseEntity<String> loadGames(@RequestParam String confirm) {
        if ("yes".equalsIgnoreCase(confirm)) {
            gameDatabaseLoader.insertGamesIntoDatabase();
            return ResponseEntity.ok("✅ Juegos insertados en la base de datos.");
        } else if ("one".equalsIgnoreCase(confirm)) {
            gameDatabaseLoader.insert1FileOfGamesIntoDatabase();
            return ResponseEntity.ok("✅ Se insertó un archivo de juegos en la base de datos.");
        } else {
            return ResponseEntity.badRequest().body("❌ Operación cancelada. No se insertaron juegos.");
        }
    }
}
