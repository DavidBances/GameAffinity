package com.gameaffinity.controller;

import java.io.IOException;
import java.util.List;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

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
     * @param game The game to be added.
     * @return {@code true} if the game was added successfully, {@code false} if the
     *         game already exists.
     */
    public boolean addGame(Game game) {
        if (gameService.isGameInDatabase(game.getName())) {
            return false;
        } else {
            return gameService.addGame(game);
        }
    }

    public boolean deleteGame(Game game) {
        return gameService.deleteGame(game.getId());
    }

}
