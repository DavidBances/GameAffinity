package com.gameaffinity.controller;

import com.gameaffinity.service.UserService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The RegisterController class handles user registration in the application.
 * It ensures users can create an account while checking for duplicate emails.
 *
 * @author Level Track
 * @since 1.0
 */
public class RegisterController {

    private UserService userService;

    /**
     * Constructs a new RegisterController and initializes the UserService.
     *
     * @throws Exception if there is an issue initializing the UserService.
     */
    public RegisterController() {
        try {
            this.userService = new UserService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a new user in the "Level Track" application.
     *
     * @param name     The name of the user.
     * @param email    The email address of the user.
     * @param password The password for the new account.
     * @return {@code true} if the registration is successful, {@code false}
     *         otherwise.
     */
    public void register(String name, String email, String password) {
        try {
            if (userService.emailExists(email)) {
                showAlert("El email ya está en uso.", "Error", AlertType.ERROR);
            }
            if (userService.registerUser(name, email, password, "REGULAR_USER")) {
                showAlert("Cuenta creada con éxito.", "Cuenta creada", AlertType.INFORMATION);
            }
        } catch (IllegalArgumentException e) {
            showAlert("ERROR.", "Error", AlertType.ERROR);
        }
    }

    public void back(Stage currentStage) {
        try {
            Pane loginPane = FXMLLoader.load(getClass().getResource("/fxml/auth/login_panel.fxml"));
            Scene loginScene = new Scene(loginPane);
            currentStage.setScene(loginScene);
        } catch (Exception e) {
            showAlert("ERROR.", "Error", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String message, String title, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
