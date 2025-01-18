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
    public UserBase login(Stage currentStage, String email, String password) {
        try {
            UserBase user = userService.authenticate(email, password);
            if (user != null) {
                System.out.println("ADMINISTRATOR".equalsIgnoreCase(user.getRole()));
                System.out.println(user.getRole());
                if ("ADMINISTRATOR".equalsIgnoreCase(user.getRole())) {
                    Parent adminDashboard = FXMLLoader.load(getClass().getResource("/fxml/admin/admin_dashboard.fxml"));
                    Scene adminScene = new Scene(adminDashboard);
                    currentStage.setScene(adminScene);
                } else {
                    Parent userDashboard = FXMLLoader.load(getClass().getResource("/fxml/user/user_dashboard.fxml"));
                    Scene userScene = new Scene(userDashboard);
                    currentStage.setScene(userScene);
                }
            } else {
                showAlert("Invalid credentials.", "Error", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("An error occurred.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        return null;
    }

    public void register(Stage currentStage) {
        try {
            Parent registerPane = FXMLLoader.load(getClass().getResource("/fxml/auth/register_panel.fxml"));
            Scene registerScene = new Scene(registerPane);
            currentStage.setScene(registerScene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("An error occurred.", "Error", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
