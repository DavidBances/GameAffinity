package com.gameaffinity.service;

import com.gameaffinity.dao.GameDAO;
import com.gameaffinity.dao.GameDAOImpl;
import com.gameaffinity.model.Game;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class GameManagementService {
    private final GameDAO gameDAO;


    public List<Game> getAllGames() {
        return gameDAO.getAllGames();
    }

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
            Game newGame = new Game(1, name, genre, price, "Available", 0, "");
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
}
