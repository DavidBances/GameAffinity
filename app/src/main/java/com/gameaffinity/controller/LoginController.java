package com.gameaffinity.controller;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * The LoginController class handles the login process for the "Level Track"
 * application.
 * It manages user authentication by interacting with the UserService.
 *
 * @author Level Track
 * @since 1.0
 */
public class LoginController {

    private UserService userService;

    /**
     * Constructs a new LoginController and initializes the UserService.
     *
     * @throws Exception if there is an issue initializing the UserService.
     */
    public LoginController() {
        try {
            this.userService = new UserService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Authenticates a user using their email and password.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @return A {@code UserBase} object representing the authenticated user,
     *         or {@code null} if authentication fails.
     */
    public UserBase login(String email, String password) {
        return userService.authenticate(email, password);
    }
}
