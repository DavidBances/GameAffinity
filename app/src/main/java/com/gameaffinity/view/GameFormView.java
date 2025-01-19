package com.gameaffinity.view;

import com.gameaffinity.model.Game;
import com.gameaffinity.controller.GameManagementController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GameFormView {

    @FXML
    private TextField nameField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField priceField;

    @FXML
    private Button saveButton;

    private Game game;
    private final GameManagementController gameManagementController = new GameManagementController();

    @FXML
    private void initialize() {

        saveButton.setOnAction(event -> {
            saveGame(
                    nameField.getText(),
                    genreField.getText(),
                    priceField.getText());
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        });
    }

    public void saveGame(String name, String genre, String priceText) {

        try {
            if (gameManagementController.addGame(name, genre, priceText)) {
                showAlert("Game added successfully!", "Success", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Failed to add the game.", "Error", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Price must be a valid number.", "Validation Error", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
