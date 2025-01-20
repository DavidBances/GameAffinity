package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameService;

import java.util.List;

public class GameManagementController {

    private GameService gameService;

    public GameManagementController() {
        try {
            this.gameService = new GameService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Retrieves a list of all games from the database.
     *
     * @return A list of all games. Returns {@code null} if an error occurs.
     */
    public List<Game> getAllGames() {
        try {
            return gameService.getAllGames();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a new game to the database.
     *
     * @param name The name of the game to be added.
     * @return {@code true} if the game was added successfully, {@code false} if the
     *         game already exists.
     */
    public boolean addGame(String name, String genre, String priceText) throws NumberFormatException{

        double price = Double.parseDouble(priceText);
        Game newGame = new Game(1, name, genre, price, "Available", 0);
        if (gameService.isGameInDatabase(newGame.getName())) {
            return false;
        } else {
            return gameService.addGame(newGame);
        }
    }

    public boolean deleteGame(Game game) {
        return gameService.deleteGame(game.getId());
    }

}
