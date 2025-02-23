package com.gameaffinity.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameaffinity.model.Game;
import com.gameaffinity.model.Platform;
import com.gameaffinity.repository.GameRepository;
import com.gameaffinity.repository.PlatformRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class GameDatabaseLoader {

    private static final String INPUT_FILE = "games_final.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;

    public GameDatabaseLoader(GameRepository gameRepository, PlatformRepository platformRepository) {
        this.gameRepository = gameRepository;
        this.platformRepository = platformRepository;
    }

    public static void main(String[] args) {
        // Iniciar contexto de Spring para obtener el bean del servicio
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GameDatabaseLoader.class)) {
            GameDatabaseLoader loader = context.getBean(GameDatabaseLoader.class);

            // Preguntar si se quiere proceder con la carga
            System.out.println("⚡ ¿Quieres insertar los juegos en la base de datos? (yes/no)");
            String respuesta = System.console().readLine(); // Leer entrada del usuario

            if ("yes".equalsIgnoreCase(respuesta)) {
                loader.insertGamesIntoDatabase();
            } else {
                System.out.println("❌ Operación cancelada. No se insertaron juegos.");
            }
        }
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
}
