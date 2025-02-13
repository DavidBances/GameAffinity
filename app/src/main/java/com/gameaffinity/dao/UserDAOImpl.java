package com.gameaffinity.dao;

import com.gameaffinity.model.Administrator;
import com.gameaffinity.model.Moderator;
import com.gameaffinity.model.RegularUser;
import com.gameaffinity.model.UserBase;
import com.gameaffinity.util.DatabaseConnection;
import com.gameaffinity.util.QueryLoader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private final Connection connection;

    public UserDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Finds a user based on their email and password.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return A {@link UserBase} object representing the user if found, or
     * {@code null} if not found.
     */
    @Override
    public UserBase findByEmailAndPassword(String email, String password) {
        UserBase user = null;
        String query = QueryLoader.getQuery("user.findByEmailAndPassword");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                user = createUserInstance(
                        role,
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Creates a user instance based on the provided role and user details.
     *
     * @param role     The role of the user (e.g., "ADMINISTRATOR", "MODERATOR",
     *                 "REGULAR_USER").
     * @param id       The user's ID.
     * @param name     The user's name.
     * @param email    The user's email.
     * @param password The user's password.
     * @return A {@link UserBase} object representing the user.
     */
    private UserBase createUserInstance(String role, int id, String name, String email, String password) {
        UserBase user = switch (role.toUpperCase()) {
            case "ADMINISTRATOR" -> new Administrator(id, name, email);
            case "MODERATOR" -> new Moderator(id, name, email);
            default -> new RegularUser(id, name, email);
        };
        user.setPassword(password);
        return user;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return A list of {@link UserBase} objects representing all users.
     */
    @Override
    public List<UserBase> findAll() {
        List<UserBase> users = new ArrayList<>();
        String query = QueryLoader.getQuery("user.findAll");
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String role = rs.getString("role");
                users.add(createUserInstance(
                        role,
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Deletes a user from the system based on their ID.
     *
     * @param id The ID of the user to be deleted.
     * @return {@code true} if the user was successfully deleted, {@code false}
     * otherwise.
     */
    @Override
    public boolean delete(int id) {
        String query = QueryLoader.getQuery("user.delete");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates a new user and inserts them into the system.
     *
     * @param user The user to be created.
     * @return {@code true} if the user was successfully created, {@code false}
     * otherwise.
     */
    @Override
    public boolean createUser(UserBase user) {
        String query = QueryLoader.getQuery("user.insert");
        String query2 = QueryLoader.getQuery("library.insert");

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stmt2 = connection.prepareStatement(query2)) {

                stmt.setString(1, user.getName());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getPassword());
                stmt.setString(4, user.getRole());

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int userId = rs.getInt(1);

                            stmt2.setInt(1, userId);
                            stmt2.executeUpdate();

                            connection.commit();
                            return true;
                        }
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates the profile information of a user.
     *
     * @param user The user whose profile is to be updated.
     * @return {@code true} if the profile was successfully updated, {@code false}
     * otherwise.
     */
    @Override
    public boolean updateProfile(UserBase user) {
        String query = QueryLoader.getQuery("user.updateProfile");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates the role of a user.
     *
     * @param userId  The ID of the user whose role is to be updated.
     * @param newRole The new role for the user.
     * @return {@code true} if the role was successfully updated, {@code false}
     * otherwise.
     */
    @Override
    public boolean updateUserRole(int userId, String newRole) {
        String query = QueryLoader.getQuery("user.updateRole");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newRole);
            stmt.setInt(2, userId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if an email address already exists in the system.
     *
     * @param email The email to check.
     * @return {@code true} if the email exists, {@code false} otherwise.
     */
    @Override
    public boolean emailExists(String email) {
        String query = QueryLoader.getQuery("user.emailExists");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserBase getUserByEmail(String email) {
        UserBase user = null;
        String query = QueryLoader.getQuery("user.findByEmail");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                user = createUserInstance(
                        role,
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
