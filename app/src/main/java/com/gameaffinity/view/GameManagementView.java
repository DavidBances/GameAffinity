package com.gameaffinity.view;

import com.gameaffinity.controller.GameManagementController;
import com.gameaffinity.model.Game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GameManagementView {

    @FXML
    private TableView<Game> gameTable;
    @FXML
    private TableColumn<Game, String> nameColumn;
    @FXML
    private TableColumn<Game, String> genreColumn;
    @FXML
    private TableColumn<Game, Double> priceColumn;
    @FXML
    private Button addGameButton;
    @FXML
    private Button deleteGameButton;

    private final GameManagementController gameManagementController = new GameManagementController();

    @FXML
    public void initialize() {
        // Initialize table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        addGameButton.setOnAction(event -> {
            openGameFormDialog();
            System.out.println("HOLA");
            refreshGameTable();
        });

        deleteGameButton.setOnAction(event -> {
            deleteGame(gameTable.getSelectionModel().getSelectedItem());
            refreshGameTable();
        });

        refreshGameTable();
    }

    public void refreshGameTable() {
        gameTable.getItems().clear();
        gameTable.getItems().addAll(gameManagementController.getAllGames());
    }

    public void openGameFormDialog() {
        try {
            // Cargar el FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/game_form_dialog.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Añadir Juego");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteGame(Game game) {
        if (game != null) {
            boolean confirmed = showConfirmationDialog("Are you sure you want to delete this game?");
            if (confirmed) {
                boolean deleted = gameManagementController.deleteGame(game);
                if (deleted) {
                    showAlert("Game deleted successfully!", "Exito", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Failed to delete game.", "Error", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Please select a game to delete.", "Alerta", Alert.AlertType.WARNING);
        }
    }


    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String message) {
        // Mostrar un cuadro de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }
}
