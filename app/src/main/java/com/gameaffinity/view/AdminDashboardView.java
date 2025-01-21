package com.gameaffinity.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class AdminDashboardView {

    @FXML
    public ImageView userManagementImage;
    @FXML
    public ImageView gameManagementImage;

    @FXML
    private StackPane mainContent;

    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        initializeImages();
        userManagementImage.setOnMouseClicked(
                event -> openUserManagementView(mainContent));
        gameManagementImage.setOnMouseClicked(
                event -> openGamesManagementView(mainContent));
        logoutButton.setOnAction(event -> logout());

    }

    private void initializeImages() {
        Image userManagementImg = new Image(getClass().getResource("/images/admin/userManagement.png").toExternalForm());
        userManagementImage.setImage(userManagementImg);

        Image gameManagementImg = new Image(getClass().getResource("/images/admin/gameManagement.png").toExternalForm());
        gameManagementImage.setImage(gameManagementImg);
    }

    public void openUserManagementView(StackPane mainContent) {
        try {
            Stage currentStage = (Stage) userManagementImage.getScene().getWindow();
            Parent userManagement = FXMLLoader.load(getClass().getResource("/fxml/admin/user_management.fxml"));
            Scene userManagementScene = new Scene(userManagement);
            currentStage.setScene(userManagementScene);
        } catch (Exception e) {
            showAlert("Error.", "Error", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void openGamesManagementView(StackPane mainContent) {
        try {
            Stage currentStage = (Stage) gameManagementImage.getScene().getWindow();
            Parent gameManagement = FXMLLoader.load(getClass().getResource("/fxml/admin/game_management.fxml"));
            Scene gameManagementScene = new Scene(gameManagement);
            currentStage.setScene(gameManagementScene);
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
