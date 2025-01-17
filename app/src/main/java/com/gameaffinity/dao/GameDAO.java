package com.gameaffinity.dao;

import com.gameaffinity.model.Game;

import java.util.List;

public interface GameDAO {
    boolean isGameInDatabase(String gameName);

    boolean addGame(Game game);

    boolean updateGame(Game game);

    boolean deleteGame(int gameId);

    List<Game> getAllGames();

    List<Game> searchGamesByName(String keyword);
}
