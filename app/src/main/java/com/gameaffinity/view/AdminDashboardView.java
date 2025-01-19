package com.gameaffinity.view;

import com.gameaffinity.controller.AdminController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminDashboardView {

    @FXML
    private StackPane mainContent;

    @FXML
    private Button manageUsersButton;

    @FXML
    private Button manageGamesButton;

    @FXML
    private Button logoutButton;

    private AdminController adminController = new AdminController();

    @FXML
    public void initialize() {
        manageUsersButton.setOnAction(
                event -> openUserManagementView(mainContent));
        manageGamesButton.setOnAction(
                event -> openGamesManagementView(mainContent));
        logoutButton.setOnAction(event -> logout());

    }

    public void openUserManagementView(StackPane mainContent) {
        try {
            Parent userManagement = FXMLLoader.load(getClass().getResource("/fxml/admin/user_management.fxml"));
            mainContent.getChildren().setAll(userManagement);
        } catch (Exception e) {
            showAlert("Error.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void openGamesManagementView(StackPane mainContent) {
        try {
            Parent gameManagement = FXMLLoader.load(getClass().getResource("/fxml/admin/game_management.fxml"));
            mainContent.getChildren().setAll(gameManagement);
        } catch (Exception e) {
            showAlert("Error.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            Parent login = FXMLLoader.load(getClass().getResource("/fxml/auth/login_panel.fxml"));
            Scene loginScene = new Scene(login);
            currentStage.setScene(loginScene);
        } catch (Exception e) {
            showAlert("Error.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
