package com.gameaffinity.dao;

import com.gameaffinity.model.*;
import com.gameaffinity.util.DatabaseConnection;
import com.gameaffinity.util.QueryLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the FriendshipDAO interface for managing friendship-related
 * data.
 * This class interacts with the database to perform CRUD operations for
 * friendship management.
 *
 * @author DavidBances
 * @since 1.0
 */

public class FriendshipDAOImpl implements FriendshipDAO {
    private final Connection connection;

    /**
     * Constructs a new FriendshipDAOImpl and initializes the database connection.
     *
     * @throws SQLException if there is an issue initializing the database connection.
     */

    public FriendshipDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Creates an instance of the appropriate UserBase subclass based on the user's
     * role.
     *
     * @param role  The role of the user.
     * @param id    The user ID.
     * @param name  The user's name.
     * @param email The user's email.
     * @return An instance of Administrator, Moderator, or Regular_User.
     */
    private UserBase createUserInstance(String role, int id, String name, String email) {
        return switch (role) {
            case "ADMINISTRATOR" -> new Administrator(id, name, email);
            case "MODERATOR" -> new Moderator(id, name, email);
            default -> new RegularUser(id, name, email);
        };
    }

    /**
     * Retrieves the friends of a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of UserBase objects representing the user's friends.
     */
    @Override
    public List<UserBase> getFriends(int userId) {
        List<UserBase> friends = new ArrayList<>();
        String query = QueryLoader.getQuery("friendship.getFriends");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String role = rs.getString("role");
                UserBase user = createUserInstance(
                        role,
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"));
                friends.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public Friendship getFriendshipById(int friendshipId) {
        String query = QueryLoader.getQuery("friendship.getFriendshipById");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, friendshipId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Friendship(
                        rs.getInt("id"),
                        rs.getInt("requester_id"),
                        getUserEmailById(rs.getInt("requester_id")),
                        rs.getInt("receiver_id"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Sends a friend request from one user to another.
     *
     * @param requesterId The ID of the user sending the request.
     * @param receiverId  The ID of the user receiving the request.
     * @return {@code true} if the request was successfully sent; {@code false}
     * otherwise.
     */
    @Override
    public String sendFriendRequest(int requesterId, int receiverId) {
        String query = QueryLoader.getQuery("friendship.sendRequest");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, requesterId);
            stmt.setInt(2, receiverId);
            stmt.setString(3, "Pending");
            return stmt.executeUpdate() > 0 ? "true" : "Error sending friend request";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Validates whether a friend request can be sent between two users.
     *
     * @param requesterId The ID of the requesting user.
     * @param receiverId  The ID of the receiving user.
     * @return {@code true} if the request is valid; {@code false} otherwise.
     */
    public String checkValidRequest(int requesterId, int receiverId) {
        if (requesterId == receiverId) {
            return "No puedes enviar una solicitud de amistad a ti mismo";
        } else if (receiverId == -1 || requesterId == -1) {
            return "Email incorrecto";
        }
        String checkFriendshipQuery = QueryLoader.getQuery("friendship.checkFriendship");
        try (PreparedStatement checkFriendshipStmt = connection.prepareStatement(checkFriendshipQuery)) {
            checkFriendshipStmt.setInt(1, requesterId);
            checkFriendshipStmt.setInt(2, receiverId);
            checkFriendshipStmt.setInt(3, receiverId);
            checkFriendshipStmt.setInt(4, requesterId);
            try (ResultSet rs = checkFriendshipStmt.executeQuery()) {
                if (rs.next()) {
                    return "No puedes enviar una solicitud de amistad a un usuario que ya es tu amigo.";
                }
            }

            String checkPendingRequestQuery = QueryLoader.getQuery("friendship.checkPendingRequest");
            try (PreparedStatement checkPendingRequestStmt = connection.prepareStatement(checkPendingRequestQuery)) {
                checkPendingRequestStmt.setInt(1, requesterId);
                checkPendingRequestStmt.setInt(2, receiverId);
                checkPendingRequestStmt.setInt(3, receiverId);
                checkPendingRequestStmt.setInt(4, requesterId);
                try (ResultSet rs = checkPendingRequestStmt.executeQuery()) {
                    if (rs.next()) {
                        return "No puedes enviar una solicitud de amistad a un usuario que ya tiene/s una solicitud pendiente.";
                    }
                }
            }
            //TODO hay que hacer que devuelva String para devolver mejores respuestas
            return "true";
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking valid request", e);
        }
    }


    /**
     * Receives a friend request from one user to another.
     *
     * @param userId The ID of the user sending the request.
     * @return A list of the friendship requests received by the user with the id userId
     */
    @Override
    public List<Friendship> getFriendRequests(int userId) {
        List<Friendship> requests = new ArrayList<>();
        String query = QueryLoader.getQuery("friendship.getRequests");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add(new Friendship(
                        rs.getInt("id"),
                        rs.getInt("requester_id"),
                        getUserEmailById(rs.getInt("requester_id")),
                        rs.getInt("receiver_id"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * Updates the friendship status.
     *
     * @param friendshipId The ID of the friendship between the users.
     * @param status       The status of the request, pending, accepted...
     * @return {@code true} if the request was successfully updated; {@code false}
     * otherwise.
     */
    @Override
    public boolean updateFriendRequestStatus(int friendshipId, String status) {
        String query = QueryLoader.getQuery("friendship.updateStatus");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, friendshipId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

    private String getUserEmailById(int userId) {
        String query = QueryLoader.getQuery("friendship.getUserEmailByUserId");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("email");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Deletes a friend.
     *
     * @param userId   The ID of the user that wants to delete a friend.
     * @param friendId The ID of the friend that is going to be deleted by the user.
     * @return {@code true} if the friendship was successfully removed;
     * {@code false} otherwise.
     */
    @Override
    public boolean deleteFriend(int userId, int friendId) {
        String query = QueryLoader.getQuery("friendship.deleteFriend");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
