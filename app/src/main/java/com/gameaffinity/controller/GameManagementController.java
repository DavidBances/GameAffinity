package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameManagementService;

import java.util.List;

public class GameManagementController {

    private final GameManagementService gameManagementService;

    public GameManagementController() {
        this.gameManagementService = new GameManagementService();
    }

    /**
     * Retrieves a list of all games from the database.
     *
     * @return A list of all games. Returns {@code null} if an error occurs.
     */
    public List<Game> getAllGames() {
        return gameManagementService.getAllGames();
    }

    /**
     * Adds a new game to the database.
     *
     * @param name The name of the game to be added.
     * @return {@code true} if the game was added successfully, {@code false} if the
     *         game already exists.
     */
    public boolean addGame(String name, String genre, String priceText) throws NumberFormatException{
        return gameManagementService.addGame(name, genre, priceText);
    }

    public boolean deleteGame(Game game) {
        return gameManagementService.deleteGame(game.getId());
    }

}
