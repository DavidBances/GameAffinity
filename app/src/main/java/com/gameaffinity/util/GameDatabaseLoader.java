package com.gameaffinity.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameaffinity.model.Game;
import com.gameaffinity.model.Platform;
import com.gameaffinity.repository.GameRepository;
import com.gameaffinity.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GameDatabaseLoader {

    private static final String INPUT_FILE = "./GameAffinity/games_final.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;

    public GameDatabaseLoader(GameRepository gameRepository, PlatformRepository platformRepository) {
        this.gameRepository = gameRepository;
        this.platformRepository = platformRepository;
    }

    @Transactional
    public void insertGamesIntoDatabase() {
        try {
            // Leer JSON y convertirlo en lista de juegos
            File file = new File(INPUT_FILE);
            List<Game> games = List.of(objectMapper.readValue(file, Game[].class));

            for (Game game : games) {
                // Verificar si el juego ya existe en la base de datos
                if (gameRepository.existsByName(game.getName())) {
                    System.out.println("🔹 Juego ya existe en la base de datos: " + game.getName());
                    continue; // Saltar este juego
                }

                // Guardar plataformas primero (para evitar duplicados)
                Set<Platform> savedPlatforms = new HashSet<>();
                for (Platform platform : game.getPlatforms()) {
                    Optional<Platform> existingPlatform = platformRepository.findByName(platform.getName());
                    savedPlatforms.add(existingPlatform.orElseGet(() -> platformRepository.save(platform)));
                }

                // Asociar las plataformas al juego y guardarlo
                game.setPlatforms(new HashSet<>(savedPlatforms));
                gameRepository.save(game);

                System.out.println("✅ Juego insertado: " + game.getName());
            }

            System.out.println("📌 Inserción de juegos completada.");
        } catch (Exception e) {
            System.err.println("❌ Error al insertar juegos en la base de datos.");
            e.printStackTrace();
        }
    }

    @Transactional
    public void insert1FileOfGamesIntoDatabase() {
        try {
            // Leer JSON y convertirlo en lista de juegos
            // Para el portatil: "D:/Java/GameAffinity/rawg_json/juegos_pagina_1.json"
            // Para el ordenador sobremesa: "E:/Cosas/GameAffinity/GameAffinity/rawg_json/juegos_pagina_1.json"
            File file = new File("E:/Cosas/GameAffinity/GameAffinity/rawg_json/juegos_pagina_1.json");
            List<Game> games = List.of(objectMapper.readValue(file, Game[].class));

            for (Game game : games) {
                // Verificar si el juego ya existe en la base de datos
                if (gameRepository.existsByName(game.getName())) {
                    System.out.println("🔹 Juego ya existe en la base de datos: " + game.getName());
                    continue; // Saltar este juego
                }

                // Guardar plataformas primero (para evitar duplicados)
                Set<Platform> savedPlatforms = new HashSet<>();
                for (Platform platform : game.getPlatforms()) {
                    Optional<Platform> existingPlatform = platformRepository.findByName(platform.getName());
                    savedPlatforms.add(existingPlatform.orElseGet(() -> platformRepository.save(platform)));
                }

                // Asociar las plataformas al juego y guardarlo
                game.setPlatforms(new HashSet<>(savedPlatforms));
                gameRepository.save(game);

                System.out.println("✅ Juego insertado: " + game.getName());
            }

            System.out.println("📌 Inserción de juegos completada.");
        } catch (Exception e) {
            System.err.println("❌ Error al insertar juegos en la base de datos.");
            e.printStackTrace();
        }
    }
}
