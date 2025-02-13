package com.gameaffinity.service;

import com.gameaffinity.dao.LibraryDAO;
import com.gameaffinity.dao.LibraryDAOImpl;
import com.gameaffinity.model.Game;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class LibraryService {
    private final LibraryDAO libraryDAO;

    public LibraryService() {
        try {
            this.libraryDAO = new LibraryDAOImpl();
        } catch (SQLException e) {
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

    public boolean removeGameFromLibrary(int userId, String gameName) {
        int libraryId = libraryDAO.getLibraryIdByUserId(userId);
        Game game = libraryDAO.getGameByName(gameName);
        return libraryDAO.removeGameFromLibrary(libraryId, game.getId());
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

    public boolean updateGameReview(int gameId, int userId, String review) {
        Game game = libraryDAO.getGameByGameId(gameId);
        game.setReview(review);
        return libraryDAO.updateGameReview(gameId, userId, review);
    }

    public boolean updateTimePlayed(int gameId, int userId, Double timePlayed) {
        Game game = libraryDAO.getGameByGameId(gameId);
        game.setTimePlayed(timePlayed);
        return libraryDAO.updateTimePlayed(gameId, userId, timePlayed);
    }

    public List<String> getAllGenres() {
        return libraryDAO.getAllGenres();
    }

    public int getGameScore(int gameId) {
        return libraryDAO.getGameScore(gameId);
    }

    public Double getTimePlayed(int gameId) {
        return libraryDAO.getTimePlayed(gameId);
    }

    public int getUserIdByEmail(String email) {
        return libraryDAO.getUserIdByEmail(email);
    }

    public boolean checkFriendship(int userId, int friendId) {
        return libraryDAO.checkFriendship(userId, friendId);
    }
}
