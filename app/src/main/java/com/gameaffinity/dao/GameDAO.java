package com.gameaffinity.dao;

import com.gameaffinity.model.Game;

import java.util.List;

public interface GameDAO {
    boolean isGameInDatabase(String gameName);

    List<Game> getGamesByName(String name);

    List<Game> getGamesByGenre(String genre);

    List<Game> getGamesByGenreAndName(String genre, String name);

    boolean addGame(Game game);

    boolean deleteGame(int gameId);

    List<Game> getAllGames();
}
