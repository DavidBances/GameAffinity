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

public class LibraryDAOImpl implements LibraryDAO {
    private final Connection connection;

    public LibraryDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Game> getGamesByUserId(int userId) {
        List<Game> games = new ArrayList<>();
        String query = QueryLoader.getQuery("library.getGamesByUserId");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                games.add(new Game(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        rs.getString("state"),
                        rs.getInt("game_score")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    @Override
    public boolean addGameToLibrary(int userId, int gameId, String state) {
        String query = QueryLoader.getQuery("library.addGame");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, getLibraryIdByUserId(userId));
            stmt.setInt(2, userId);
            stmt.setInt(3, gameId);
            stmt.setString(4, state);
            stmt.setInt(5, 0);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeGameFromLibrary(int libraryId, int gameId) {
        String query = QueryLoader.getQuery("library.removeGame");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, libraryId);
            stmt.setInt(2, gameId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateGameState(int gameId, int userId, String newState) {
        String query = QueryLoader.getQuery("library.updateGameState");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newState);
            stmt.setInt(2, gameId);
            stmt.setInt(3, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Game getGameByName(String gameName) {
        String query = QueryLoader.getQuery("game.getGameByName");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, gameName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Game(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        "Available",
                        0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Game getGameByGameId(int gameId) {
        String query = QueryLoader.getQuery("game.getGameByGameId");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Game(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        "Available",
                        0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public int getLibraryIdByUserId(int userId) {
        String query = QueryLoader.getQuery("library.getLibraryIdByUserId");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Library not found for user ID: " + userId);
    }

    @Override
    public List<Game> getGamesByGenreUser(int userId, String genre) {
        List<Game> games = new ArrayList<>();
        String query = QueryLoader.getQuery("library.getGamesByGenreUser");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, "%" + genre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                games.add(new Game(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        rs.getString("state"),
                        rs.getInt("game_score")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    @Override
    public List<Game> getGamesByNameUser(int userId, String name) {
        List<Game> games = new ArrayList<>();
        String query = QueryLoader.getQuery("library.getGamesByNameUser");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                games.add(new Game(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        rs.getString("state"),
                        rs.getInt("game_score")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    @Override
    public List<Game> getGamesByGenreAndNameUser(int userId, String genre, String name) {
        List<Game> games = new ArrayList<>();
        String query = QueryLoader.getQuery("library.getGamesByGenreAndNameUser");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, "%" + genre + "%");
            stmt.setString(3, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                games.add(new Game(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getDouble("price"),
                        rs.getString("state"),
                        rs.getInt("game_score")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    @Override
    public boolean isGameInLibrary(int userId, int gameId) {
        String query = QueryLoader.getQuery("library.isGameInLibrary");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, getLibraryIdByUserId(userId));
            stmt.setInt(2, gameId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateGameScore(int gameId, int userId, int score) {
        String query = QueryLoader.getQuery("library.updateGameScore");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, score);
            stmt.setInt(2, gameId);
            stmt.setInt(3, userId);
            stmt.setInt(4, getLibraryIdByUserId(userId));
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getGameScore(int gameId) {
        String query = QueryLoader.getQuery("library.getGameScore");
        int totalScore = 0;
        int numberOfScores = 0;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                totalScore += rs.getInt("game_score");
                numberOfScores++;
            }
            if (numberOfScores > 0) {
                return totalScore / numberOfScores;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<String> getAllGenres() {
        List<String> genres = new ArrayList<>();
        String query = QueryLoader.getQuery("library.getAllGenres");
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                genres.add(rs.getString("genre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

    /**
     * Searches a user ID based on the email.
     *
     * @param email The email of the user.
     * @return The ID of the user if it can be found; -1 otherwise.
     */
    @Override
    public int getUserIdByEmail(String email) {
        String query = QueryLoader.getQuery("friendship.getUserIdByUserEmail");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean checkFriendship(int userId, int friendId) {
        String checkFriendshipQuery = QueryLoader.getQuery("friendship.checkFriendship");
        try (PreparedStatement checkFriendshipStmt = connection.prepareStatement(checkFriendshipQuery)) {
            checkFriendshipStmt.setInt(1, userId);
            checkFriendshipStmt.setInt(2, friendId);
            checkFriendshipStmt.setInt(3, friendId);
            checkFriendshipStmt.setInt(4, userId);
            try (ResultSet rs = checkFriendshipStmt.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking valid request", e);
        }
    }
}

