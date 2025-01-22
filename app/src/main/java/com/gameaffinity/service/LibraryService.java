package com.gameaffinity.service;

import com.gameaffinity.dao.LibraryDAO;
import com.gameaffinity.dao.GameDAO;
import com.gameaffinity.dao.LibraryDAOImpl;
import com.gameaffinity.dao.GameDAOImpl;
import com.gameaffinity.model.Game;

import java.sql.SQLException;
import java.util.List;

public class LibraryService {
    private final LibraryDAO libraryDAO;
    private final GameDAO gameDAO;

    public LibraryService() {
        try{
            this.libraryDAO = new LibraryDAOImpl();
            this.gameDAO = new GameDAOImpl();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Game> getGamesByUserId(int userId) {
        return libraryDAO.getGamesByUserId(userId);
    }

    public List<Game> getGamesByGenreUser(int userId, String genre) {
        return libraryDAO.getGamesByGenreUser(userId, genre);
    }

    public List<Game> getGamesByNameUser(int userId, String name) {
        return libraryDAO.getGamesByNameUser(userId, name);
    }

    public List<Game> getGamesByGenreAndNameUser(int userId, String genre, String name) {
        return libraryDAO.getGamesByGenreAndNameUser(userId, genre, name);
    }

    public boolean addGameToLibrary(int userId, String gameName) throws Exception {
        Game game = libraryDAO.getGameByName(gameName);
        if (game == null) {
            throw new Exception("Game not found in the database.");
        }
        boolean alreadyInLibrary = libraryDAO.isGameInLibrary(userId, game.getId());
        if (alreadyInLibrary) {
            return false;
        }
        return libraryDAO.addGameToLibrary(userId, game.getId(), "Available");
    }

    public boolean removeGameFromLibrary(int userId, int gameId) {
        int libraryId = libraryDAO.getLibraryIdByUserId(userId);
        return libraryDAO.removeGameFromLibrary(libraryId, gameId);
    }

    public boolean updateGameState(int gameId, int userId, String newState) {
        Game game = libraryDAO.getGameByGameId(gameId);
        game.setState(newState);
        return libraryDAO.updateGameState(gameId, userId, newState);
    }

    public boolean updateGameScore(int gameId, int userId, int score) {
        Game game = libraryDAO.getGameByGameId(gameId);
        game.setScore(score);
        return libraryDAO.updateGameScore(gameId, userId, score);
    }

    //Sirve para mostrar todos los juegos en la pantalla en la que se muestran todos los juegos
    public List<Game> getAllGames() {
        return gameDAO.getAllGames();
    }

    public List<String> getAllGenres() {
        return libraryDAO.getAllGenres();
    }

    public int getGameScore(int gameId) {
        return libraryDAO.getGameScore(gameId);
    }

}
