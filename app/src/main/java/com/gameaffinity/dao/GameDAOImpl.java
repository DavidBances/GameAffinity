package com.gameaffinity.dao;

import com.gameaffinity.model.Game;
import com.gameaffinity.util.DatabaseConnection;
import com.gameaffinity.util.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDAOImpl implements GameDAO {
    private final Connection connection;

    public GameDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Checks if a game already exists in the database by its name.
     *
     * @param gameName The name of the game to check.
     * @return {@code true} if the game exists in the database; {@code false}
     *         otherwise.
     */
    @Override
    public boolean isGameInDatabase(String gameName) {
        String query = QueryLoader.getQuery("game.existsByName");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, gameName);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a new game to the database.
     *
     * @param game The {@code Game} object to be added to the database.
     * @return {@code true} if the game was successfully added; {@code false}
     *         otherwise.
     */
    @Override
    public boolean addGame(Game game) {
        String query = QueryLoader.getQuery("game.addGame");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, game.getName());
            stmt.setString(2, game.getGenre());
            stmt.setDouble(3, game.getPrice());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a game from the database by its ID.
     *
     * @param gameId The ID of the game to be deleted.
     * @return {@code true} if the game was successfully deleted; {@code false}
     *         otherwise.
     */
    @Override
    public boolean deleteGame(int gameId) {
        String query = QueryLoader.getQuery("game.deleteGame");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all games from the database.
     *
     * @return A list of all {@code Game} objects in the database.
     */
    @Override
    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        String query = QueryLoader.getQuery("game.findAll");
        try (PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                games.add(new Game(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        "Available",
                        0,
                        rs.getString("image_url")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return games;
    }
}
