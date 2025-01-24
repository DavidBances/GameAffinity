package com.gameaffinity.service;

import com.gameaffinity.dao.UserDAO;
import com.gameaffinity.dao.UserDAOImpl;
import com.gameaffinity.model.Administrator;
import com.gameaffinity.model.Moderator;
import com.gameaffinity.model.RegularUser;
import com.gameaffinity.model.UserBase;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        try {
            this.userDAO = new UserDAOImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Authenticates a user based on email and password.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return The authenticated UserBase object or null if authentication fails.
     */
    public UserBase authenticate(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email and password are required.");
        }

        UserBase user = userDAO.findByEmailAndPassword(email, password);
        if (user == null) {
            return null;
        }

        return createUserInstance(user.getRole(), user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    /**
     * Registers a new user in the system.
     *
     * @param name     The user's name.
     * @param email    The user's email.
     * @param password The user's password.
     * @param role     The user's role (Administrator, Moderator, Regular_User).
     * @return A String with the register result.
     */
    public String registerUser(String name, String email, String password, String role) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()
                || role == null || role.isEmpty()) {
            return "All fields are required.";
        }

        if (!isValidEmail(email)) {
            return "Invalid email format.";
        }

        if (emailExists(email)) {
            return "El email ya está en uso.";
        }

        if (!isValidRole(role)) {
            return "Invalid role. Accepted roles: Administrator, Moderator, Regular_User.";
        }

        UserBase user = createUserInstance(role, 0, name, email, password);
        if (userDAO.createUser(user)) {
            return "Cuenta creada con éxito.";
        } else {
            return "Error al crear la cuenta.";
        }
    }

    /**
     * Updates a user's profile.
     *
     * @param email       The user's email.
     * @param password    The user's password.
     * @param newName     The updated name.
     * @param newEmail    The updated email.
     * @param newPassword The updated password.
     * @return True if the profile was successfully updated, false otherwise.
     */
    public boolean updateUserProfile(String email, String password, String newName, String newEmail, String newPassword) {

        UserBase authenticated = authenticate(email, password);
        if (authenticated == null) {
            return false;
        }

        if (newName == null || newName.isEmpty()) {
            newName = authenticated.getName();
        }
        if (newEmail == null || newEmail.isEmpty()) {
            newEmail = authenticated.getEmail();
        }
        if (newPassword == null || newPassword.isEmpty()) {
            newPassword = authenticated.getPassword();
        } else {
            newPassword = hashPassword(newPassword);
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        authenticated.setName(newName);
        authenticated.setEmail(newEmail);
        authenticated.setPassword(newPassword);

        return userDAO.updateProfile(authenticated);
    }

    /**
     * Helper method to validate the email format.
     *
     * @param email The email to validate.
     * @return True if the email format is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al inicializar el algoritmo de hash", e);
        }
    }

    private boolean isValidRole(String role) {
        return role.equalsIgnoreCase("ADMINISTRATOR") ||
                role.equalsIgnoreCase("MODERATOR") ||
                role.equalsIgnoreCase("REGULAR_USER");
    }

    /**
     * Overloaded helper method to create a new user instance from scratch.
     *
     * @param role     The user's role.
     * @param id       The user's ID.
     * @param name     The user's name.
     * @param email    The user's email.
     * @param password The user's password.
     * @return A subclass instance of UserBase.
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

    private boolean emailExists(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        return userDAO.emailExists(email);
    }

    public List<UserBase> getAllUsers() {
        return userDAO.findAll();
    }

    public boolean updateUserRole(UserBase user, String newRole) {
        if (!isValidRole(newRole)) {
            return false;
        }
        user.setRole(newRole);
        return userDAO.updateUserRole(user.getId(), newRole);
    }

    public boolean deleteUser(int userId) {
        return userDAO.delete(userId);
    }

    public UserBase getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }
}