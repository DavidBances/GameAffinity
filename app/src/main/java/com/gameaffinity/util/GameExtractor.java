package com.gameaffinity.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameaffinity.model.Game;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class GameExtractor {
    private static final String RAWG_FOLDER = "rawg_json";
    private static final String OUTPUT_FILE = "games_final.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        GameExtractor extractor = new GameExtractor();
        extractor.combinarArchivos();
    }

    public void combinarArchivos() {
        File folder = new File(RAWG_FOLDER);
        List<Game> juegosCompletos = new ArrayList<>();

        for (File file : folder.listFiles((dir, name) -> name.endsWith(".json"))) {
            try {
                Game[] games = objectMapper.readValue(file, Game[].class);
                for (Game game : games) {
                    juegosCompletos.add(game);
                }
            } catch (Exception e) {
                System.err.println("Error procesando archivo: " + file.getName());
                e.printStackTrace();
            }
        }

        guardarJuegosEnJson(juegosCompletos);
    }

    private void guardarJuegosEnJson(List<Game> juegos) {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
            objectMapper.writeValue(writer, juegos);
            System.out.println("Archivo final guardado en " + OUTPUT_FILE);
        } catch (Exception e) {
            System.err.println("Error al guardar el archivo final.");
            e.printStackTrace();
        }
    }
}
