package com.gameaffinity.service;

import com.gameaffinity.dao.LibraryDAO;
import com.gameaffinity.dao.GameDAO;
import com.gameaffinity.dao.LibraryDAOImpl;
import com.gameaffinity.dao.GameDAOImpl;
import com.gameaffinity.model.Game;

import java.util.List;

public class LibraryService {
    private final LibraryDAO libraryDAO;
    private final GameDAO gameDAO;

    public LibraryService() throws Exception {
        this.libraryDAO = new LibraryDAOImpl();
        this.gameDAO = new GameDAOImpl();
    }

    public List<Game> getGamesByUserId(int userId) {
        return libraryDAO.getGamesByUserId(userId);
    }

    public boolean isGameInLibrary(int userId, int gameId) {
        return libraryDAO.isGameInLibrary(userId, gameId);
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

    public boolean addGameToLibrary(int userId, int gameId, String state) {
        return libraryDAO.addGameToLibrary(userId, gameId, state);
    }

    public boolean removeGameFromLibrary(int libraryId, int gameId) {
        return libraryDAO.removeGameFromLibrary(libraryId, gameId);
    }

    public boolean updateGameState(int gameId, int userId, String newState) {
        return libraryDAO.updateGameState(gameId, userId, newState);
    }

    public Game getGameByName(String gameName) {
        return libraryDAO.getGameByName(gameName);
    }

    public int getLibraryIdByUserId(int userId) {
        return libraryDAO.getLibraryIdByUserId(userId);
    }

    public List<Game> getAllGames() {
        return gameDAO.getAllGames();
    }

    public List<Game> getGamesByGenre(String genre) {
        return libraryDAO.getGamesByGenre(genre);
    }

    public List<String> getAllGenres() {
        return libraryDAO.getAllGenres();
    }

    public boolean updateGameScore(int gameId, int userId, int score) {
        return libraryDAO.updateGameScore(gameId, userId, score);
    }

    public int getGameScore(int gameId, int userId) {
        return libraryDAO.getGameScore(gameId, userId);
    }

}
