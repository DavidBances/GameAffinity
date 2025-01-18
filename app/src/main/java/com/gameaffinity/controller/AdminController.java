package com.gameaffinity.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The AdminController class handles administrative operations such as managing
 * users and games.
 * <p>
 * This class provides methods to assign roles to users, add, update, and delete
 * games, as well as
 * retrieve information about games and users.
 *
 * @author Level Track
 * @since 1.0
 */
public class AdminController {

    public void openGamesManagementView(StackPane mainContent) {
        try {
            Pane gameManagementPane = FXMLLoader.load(getClass().getResource("/fxml/admin/game_management.fxml"));
            mainContent.getChildren().setAll(gameManagementPane);
        } catch (Exception e) {
            showAlert("Error.", "Error", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void openUserManagementView(StackPane mainContent) {
        try {
            Pane userManagementPane = FXMLLoader.load(getClass().getResource("/fxml/admin/user_management.fxml"));
            mainContent.getChildren().setAll(userManagementPane);
        } catch (Exception e) {
            showAlert("Error.", "Error", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void logout(Stage currentStage) {
        try {
            Pane loginPane = FXMLLoader.load(getClass().getResource("/fxml/auth/login_panel.fxml"));
            Scene loginScene = new Scene(loginPane);
            currentStage.setScene(loginScene);
        } catch (Exception e) {
            showAlert("Error.", "Error", AlertType.ERROR);
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
