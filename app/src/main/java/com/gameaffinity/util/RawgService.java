package com.gameaffinity.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameaffinity.model.Game;
import com.gameaffinity.model.Platform;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RawgService {
    private static final String API_KEY = "d6d7e5ecf6904dc3aa90a817e3c2fe81";
    private static final String BASE_URL = "https://api.rawg.io/api/games";
    private static final int PAGE_SIZE = 40;
    private static final String OUTPUT_FOLDER = "rawg_json";
    private static final int MAX_REQUESTS = 1808;
    private static int requestsMade = 0;
    private static int actualPage = 245;


    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        RawgService downloader = new RawgService();
        downloader.descargarTodosLosJuegos();
    }

    public void descargarTodosLosJuegos() {
        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        boolean morePages = true;

        while (morePages && requestsMade < MAX_REQUESTS) {
            String url = BASE_URL + "?key=" + API_KEY + "&page=" + actualPage + "&page_size=" + PAGE_SIZE;

            System.out.println("Obteniendo página " + actualPage + ": " + url);

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("User-Agent", "Mozilla/5.0 (compatible; GameAffinityBot/1.0)");
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode games = rootNode.get("results");

                requestsMade++;


                List<Game> gameList = new ArrayList<>();

                for (JsonNode game : games) {
                    int id = game.get("id").asInt();
                    String name = game.get("name").asText();
                    int releaseYear = game.has("released") && !game.get("released").isNull()
                            ? Integer.parseInt(game.get("released").asText().substring(0, 4))
                            : -1;
                    String imageUrl = game.has("background_image") ? game.get("background_image").asText() : "Sin imagen";

                    List<Platform> platforms = new ArrayList<>();
                    if (game.has("platforms")) {
                        for (JsonNode platform : game.get("platforms")) {
                            String platformName = platform.get("platform").get("name").asText();
                            platforms.add(new Platform(platformName));
                        }
                    }


                    String genre = "Desconocido";
                    if (game.has("genres") && game.get("genres").size() > 0) {
                        genre = game.get("genres").get(0).get("name").asText();
                    }

                    // 🔹 Segunda llamada para obtener descripción y desarrollador
                    JsonNode details = obtenerDetallesJuego(id);
                    String description = details.has("description_raw") && !details.get("description_raw").isNull()
                            ? details.get("description_raw").asText()
                            : "Sin descripción";

                    String developer = "Desconocido";
                    if (details.has("developers") && details.get("developers").size() > 0) {
                        developer = details.get("developers").get(0).get("name").asText();
                    }

                    double price = 0.0; // RAWG no proporciona precio

                    Game extractedGame = new Game(id, name, genre, price, description, imageUrl, releaseYear, developer, new HashSet<>(platforms));
                    gameList.add(extractedGame);
                }

                guardarJsonEnArchivo(gameList, actualPage);

                int resultsEnPagina = games.size();
                if (resultsEnPagina < PAGE_SIZE) {
                    morePages = false;
                } else {
                    actualPage++;
                }
            } catch (Exception e) {
                System.err.println("Error al obtener la página " + actualPage);
                e.printStackTrace();
                morePages = false;
            }
        }

        System.out.println("Descarga completada.");
    }

    private JsonNode obtenerDetallesJuego(int gameId) {
        try {
            String url = BASE_URL + "/" + gameId + "?key=" + API_KEY;
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (compatible; GameAffinityBot/1.0)");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);

            requestsMade++;

            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            System.err.println("Error obteniendo detalles del juego ID " + gameId);
            return objectMapper.createObjectNode();
        }
    }

    private void guardarJsonEnArchivo(List<Game> games, int page) {
        String fileName = OUTPUT_FOLDER + File.separator + "juegos_pagina_" + page + ".json";
        try (FileWriter writer = new FileWriter(fileName)) {
            objectMapper.writeValue(writer, games);
            System.out.println("Guardado archivo: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo " + fileName);
            e.printStackTrace();
        }
    }
}
