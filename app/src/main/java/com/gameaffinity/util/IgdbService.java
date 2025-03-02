package com.gameaffinity.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameaffinity.model.Developer;
import com.gameaffinity.model.Game;
import com.gameaffinity.model.Genre;
import com.gameaffinity.model.Platform;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IgdbService {
    private static final String CLIENT_ID = "kujju3neabsuhd42dnsa7p9tn596in"; // 🔹 Sustituye con tu Client ID de Twitch
    private static final String ACCESS_TOKEN = "gcabzuofvutba4wg0cnfx1uy8u0w23"; // 🔹 Sustituye con tu access token
    private static final String CLIENT_SECRET = "bkv27ej5r8nuhyubkue8eaxtorqd5p";
    private static final String BASE_URL = "https://api.igdb.com/v4/games";
    private static final String OUTPUT_FOLDER = "igdb_json";
    private static final int MAX_REQUESTS_PER_SECOND = 4; // IGDB tiene un límite de peticiones
    private static int requestsMade = 0;
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        IgdbService downloader = new IgdbService();
        downloader.descargarJuegos();
    }

    public void descargarJuegos() {
        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        boolean morePages = true;
        int offset = 0;
        int limit = 500;

        while (morePages) {
            String query = "fields id, name, genres.name, platforms.name, summary, cover.url, first_release_date, involved_companies.company.name; " +
                    "sort first_release_date desc; limit " + limit + "; offset " + offset + ";";

            System.out.println("🔹 Obteniendo juegos desde offset " + offset);

            try {
                HttpHeaders headers = getHeaders();
                HttpEntity<String> entity = new HttpEntity<>(query, headers);

                ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, String.class);
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                if (rootNode == null || rootNode.isEmpty()) {
                    System.out.println("❌ Respuesta vacía o nula");
                    break;
                }

                requestsMade++;

                if (requestsMade % MAX_REQUESTS_PER_SECOND == 0) {
                    System.out.println("💤 Esperando 1 segundo para respetar el límite de 4 peticiones por segundo...");
                    Thread.sleep(1000); // Esperar 1 segundo (1000 milisegundos)
                }

                List<Game> gameList = new ArrayList<>();

                for (JsonNode game : rootNode) {
                    // Verificar si el campo 'id' existe
                    int id = game.has("id") ? game.get("id").asInt() : -1;
                    String name = game.has("name") ? game.get("name").asText() : "Desconocido";
                    int releaseYear = game.has("first_release_date") ?
                            (int) (game.get("first_release_date").asLong() / 31556952L + 1970) : -1;
                    String imageUrl = game.has("cover") && game.get("cover").has("url") ?
                            "https:" + game.get("cover").get("url").asText().replace("t_thumb", "t_cover_big") : "Sin imagen";
                    String description = game.has("summary") ? game.get("summary").asText() : "Sin descripción";

                    // Extraer los géneros
                    Set<Genre> genres = extractGenres(game);

                    // Extraer las plataformas
                    Set<Platform> platforms = extractPlatforms(game);

                    // Extraer los desarrolladores
                    Set<Developer> developers = extractDevelopers(game);

                    // Crear el objeto Game
                    Game extractedGame = new Game(id, name, genres, 0.0, description, imageUrl, releaseYear, developers, platforms);
                    gameList.add(extractedGame);
                }

                guardarJsonEnArchivo(gameList, offset);

                if (rootNode.size() < limit) {
                    morePages = false;
                } else {
                    offset += limit;
                }
            } catch (Exception e) {
                System.err.println("❌ Error al obtener juegos en offset " + offset);
                e.printStackTrace();
                morePages = false;
            }
        }

        System.out.println("✅ Descarga completada.");
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-ID", CLIENT_ID);
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setContentType(MediaType.TEXT_PLAIN);
        return headers;
    }

    private void guardarJsonEnArchivo(List<Game> games, int offset) {
        String fileName = OUTPUT_FOLDER + File.separator + "juegos_offset_" + offset + ".json";
        try (FileWriter writer = new FileWriter(fileName)) {
            objectMapper.writeValue(writer, games);
            System.out.println("📁 Guardado archivo: " + fileName);
        } catch (IOException e) {
            System.err.println("❌ Error al guardar el archivo " + fileName);
            e.printStackTrace();
        }
    }

    private Set<Genre> extractGenres(JsonNode gameNode) {
        Set<Genre> genres = new HashSet<>();
        if (gameNode.has("genres")) {
            for (JsonNode genreNode : gameNode.get("genres")) {
                genres.add(new Genre(genreNode.get("name").asText())); // Cambiado para usar Genre en lugar de String
            }
        }
        return genres;
    }

    private Set<Platform> extractPlatforms(JsonNode gameNode) {
        Set<Platform> platforms = new HashSet<>();
        if (gameNode.has("platforms")) {
            for (JsonNode platformNode : gameNode.get("platforms")) {
                String platformName = platformNode.has("name") ? platformNode.get("name").asText() : "Desconocido";
                platforms.add(new Platform(platformName)); // Asegúrate de tener la clase Platform creada
            }
        }
        return platforms;
    }

    private Set<Developer> extractDevelopers(JsonNode gameNode) {
        Set<Developer> developers = new HashSet<>();
        if (gameNode.has("involved_companies") && !gameNode.get("involved_companies").isNull()) {
            for (JsonNode involvedCompany : gameNode.get("involved_companies")) {
                if (involvedCompany.has("company") && involvedCompany.get("company").has("name")) {
                    developers.add(new Developer(involvedCompany.get("company").get("name").asText())); // Cambiado para usar Developer en lugar de String
                }
            }
        }
        return developers;
    }
}
