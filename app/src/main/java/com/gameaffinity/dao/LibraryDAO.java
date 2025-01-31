package com.gameaffinity.dao;

import com.gameaffinity.model.Game;

import java.util.List;

public interface LibraryDAO {
    List<Game> getGamesByUserId(int userId);

    boolean addGameToLibrary(int libraryId, int gameId, String state);

    boolean removeGameFromLibrary(int libraryId, int gameId);

    boolean updateGameState(int gameId, int userId, String newState);

    Game getGameByName(String gameName);

    Game getGameByGameId(int gameId);

    int getLibraryIdByUserId(int userId);

    List<Game> getGamesByGenreUser(int userId, String genre);

    List<Game> getGamesByNameUser(int userId, String name);

    List<Game> getGamesByGenreAndNameUser(int userId, String genre, String name);

    boolean isGameInLibrary(int libraryId, int gameId);

    int getGameScore(int gameId);

    Double getTimePlayed(int gameId);

    boolean updateGameScore(int gameId, int userId, int score);

    boolean updateGameReview(int gameId, int userId, String review);

    boolean updateTimePlayed(int gameId, int userId, Double timePlayed);

    List<String> getAllGenres();

    int getUserIdByEmail(String email);

    boolean checkFriendship(int userId, int friendId);
}
