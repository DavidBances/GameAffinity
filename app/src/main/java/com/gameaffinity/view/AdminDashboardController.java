package com.gameaffinity.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminDashboardController {

    @FXML
    private StackPane mainContent;

    @FXML
    public void handleManageUsers() {
        try {
            Pane userManagementPane = FXMLLoader.load(getClass().getResource("/fxml/user_management.fxml"));
            mainContent.getChildren().setAll(userManagementPane);
        } catch (Exception e) {
            showError("Error loading User Management", e.getMessage());
        }
    }

    @FXML
    public void handleManageGames() {
        try {
            Pane gameManagementPane = FXMLLoader.load(getClass().getResource("/fxml/game_management.fxml"));
            mainContent.getChildren().setAll(gameManagementPane);
        } catch (Exception e) {
            showError("Error loading Game Management", e.getMessage());
        }
    }

    @FXML
    public void handleLogout() {
        try {
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            Pane loginPane = FXMLLoader.load(getClass().getResource("/fxml/login_panel.fxml"));
            Scene loginScene = new Scene(loginPane);
            currentStage.setScene(loginScene);
        } catch (Exception e) {
            showError("Error during logout", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

