package com.gameaffinity.controller;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.LibraryService;

import java.util.List;

/**
 * The LibraryController class manages the interactions between the library
 * services
 * and the presentation layer, handling operations related to a user's game
 * library.
 *
 * @author DavidBances
 * @since 1.0
 */
public class LibraryController {

    private final LibraryService libraryService;

    /**
     * Constructs a new LibraryController, initializing the LibraryService.
     */
    public LibraryController() {
        this.libraryService = new LibraryService();
    }


    /**
     * Retrieves a list of games owned by a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of {@code Game} objects owned by the user.
     */
    public List<Game> getGamesByUserId(int userId) {
        return libraryService.getGamesByUserId(userId);
    }

    /**
     * Retrieves a list of games by a specific genre for a given user.
     *
     * @param userId The ID of the user.
     * @param genre  The genre to filter by.
     * @return A list of {@code Game} objects matching the genre for the user.
     */
    public List<Game> getGamesByGenreUser(int userId, String genre) {
        return libraryService.getGamesByGenreUser(userId, genre);
    }

    public List<Game> getGamesByNameUser(int userId, String name) {
        return libraryService.getGamesByNameUser(userId, name);
    }

    public List<Game> getGamesByGenreAndNameUser(int userId, String genre, String name) {
        return libraryService.getGamesByGenreAndNameUser(userId, genre, name);
    }

    /**
     * Adds a game to a user's library.
     *
     * @param userId   The ID of the user.
     * @param gameName The name of the game to add.
     * @return {@code true} if the game was added successfully, {@code false} if it
     *         was already in the library.
     * @throws Exception if the game is not found in the database.
     */
    public boolean addGameToLibrary(int userId, String gameName) throws Exception {
        return libraryService.addGameToLibrary(userId, gameName);
    }

    /**
     * Removes a game from a user's library.
     *
     * @param userId The ID of the user.
     * @param gameId The ID of the game to remove.
     * @return {@code true} if the game was removed successfully, {@code false}
     *         otherwise.
     */
    public boolean removeGame(int userId, int gameId) {
        return libraryService.removeGameFromLibrary(userId, gameId);
    }

    /**
     * Retrieves a list of all games in the library.
     *
     * @return A list of all {@code Game} objects in the library.
     */
    public List<Game> getAllGames() {
        return libraryService.getAllGames();
    }

    /**
     * Retrieves a list of all genres available in the library.
     *
     * @return A list of genres as {@code String} values.
     */
    public List<String> getAllGenres() {
        return libraryService.getAllGenres();
    }

    /**
     * Updates the state of a game for a user.
     *
     * @param gameId   The ID of the game.
     * @param userId   The ID of the user.
     * @param newState The new state to set (e.g., "Available", "Completed").
     * @return {@code true} if the state was updated successfully, {@code false}
     *         otherwise.
     */
    public boolean updateGameState(int gameId, int userId, String newState) {
        return libraryService.updateGameState(gameId, userId, newState);
    }

    /**
     * Updates the score of a game for a user.
     *
     * @param gameId The ID of the game.
     * @param userId The ID of the user.
     * @param score  The new score to set.
     * @return {@code true} if the score was updated successfully, {@code false}
     *         otherwise.
     */
    public boolean updateGameScore(int gameId, int userId, int score) {
        return libraryService.updateGameScore(gameId, userId, score);
    }
}
