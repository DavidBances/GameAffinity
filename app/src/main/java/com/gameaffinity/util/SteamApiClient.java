package com.gameaffinity.util;

import com.gameaffinity.model.Game;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SteamApiClient {

    private static final String API_KEY = "5B79D7FDBF64851449B1FD7EAE92C29C";
    private static final String BASE_URL = "https://api.steampowered.com";
    private static final int MAX_REQUESTS = 10000; // Límite de solicitudes

    private final RestTemplate restTemplate;

    private int requestCount = 0; // Contador de solicitudes realizadas

    public SteamApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public Game getGameDetails(int appId) {
        String url = "https://store.steampowered.com/api/appdetails?appids=" + appId;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject jsonResponse = new JSONObject(response.getBody());

            // Verificar que el appId está presente y que 'success' es true
            if (jsonResponse.has(String.valueOf(appId))) {
                JSONObject gameResponse = jsonResponse.getJSONObject(String.valueOf(appId));
                if (gameResponse.getBoolean("success")) {
                    JSONObject gameData = gameResponse.getJSONObject("data");

                    String name = gameData.optString("name", "Unknown");
                    JSONArray genresArray = gameData.optJSONArray("genres");
                    String genre = genresArray != null && genresArray.length() > 0
                            ? genresArray.getJSONObject(0).optString("description", "Unknown")
                            : "Unknown";

                    JSONObject priceOverview = gameData.optJSONObject("price_overview");
                    double price = priceOverview != null ? priceOverview.optDouble("final", 0.0) / 100.0 : 0.0;

                    return new Game(appId, name, genre, price, "Available", 0, "");
                }
            }
        }
        return null;  // En caso de error o datos faltantes
    }

    public List<Game> getAllGames() {
        String url = BASE_URL + "/ISteamApps/GetAppList/v2/?key=" + API_KEY;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        // Procesar la respuesta para devolver una lista de juegos
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("applist")) {
            Map<String, Object> appList = (Map<String, Object>) responseBody.get("applist");
            List<Map<String, Object>> apps = (List<Map<String, Object>>) appList.get("apps");

            // Obtener todos los juegos y sus detalles hasta alcanzar el límite
            return apps.stream()
                    .map(app -> {
                        if (requestCount >= MAX_REQUESTS) {
                            System.out.println("Se alcanzó el límite de solicitudes: " + MAX_REQUESTS);
                            return null;  // Detener el flujo cuando se alcanza el límite
                        }

                        int appId = (Integer) app.get("appid");
                        Game gameDetails = getGameDetails(appId);  // Llamada a getGameDetails

                        if (gameDetails != null) {
                            System.out.println(requestCount);
                            requestCount++;  // Incrementar el contador de solicitudes
                            return new Game(
                                    appId,
                                    gameDetails.getName(),
                                    gameDetails.getGenre(),
                                    gameDetails.getPrice(),
                                    gameDetails.getState(),
                                    gameDetails.getScore(),
                                    gameDetails.getImageUrl()
                            );
                        }
                        return null;  // Si no se encuentran detalles del juego, devolver null (puedes manejarlo si es necesario)
                    })
                    .filter(Objects::nonNull)  // Filtrar los juegos nulos (en caso de que no se obtengan detalles)
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
