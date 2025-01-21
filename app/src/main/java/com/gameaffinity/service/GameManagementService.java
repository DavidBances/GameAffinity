package com.gameaffinity.service;

import com.gameaffinity.dao.GameDAO;
import com.gameaffinity.dao.GameDAOImpl;
import com.gameaffinity.model.Game;

import java.sql.SQLException;
import java.util.List;

public class GameManagementService {
    private final GameDAO gameDAO;

    public GameManagementService(){
        try {
            this.gameDAO = new GameDAOImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addGame(String name, String genre, String priceText) {
        try {
            double price = Double.parseDouble(priceText);
            Game newGame = new Game(1, name, genre, price, "Available", 0);
            if (gameDAO.isGameInDatabase(newGame.getName())) {
                return false;
            } else {
                return gameDAO.addGame(newGame);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid price format: " + priceText);
            return false;
        }
    }

    public boolean deleteGame(int gameId) {
        return gameDAO.deleteGame(gameId);
    }


    public List<Game> getAllGames() {
            return gameDAO.getAllGames();
    }
}
