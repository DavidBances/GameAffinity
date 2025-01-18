package com.gameaffinity.controller;

import java.io.IOException;
import java.util.List;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameService;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class GameManagementController {

    private GameService gameService;

    public GameManagementController() {
        try {
            this.gameService = new GameService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshGameTable(TableView<Game> gameTable) {
        gameTable.getItems().clear();
        gameTable.getItems().addAll(this.getAllGames());
    }

    /**
     * Retrieves a list of all games from the database.
     *
     * @return A list of all games. Returns {@code null} if an error occurs.
     */
    public List<Game> getAllGames() {
        try {
            return gameService.getAllGames();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public void saveGame(String name, String genre, String priceText) {

        try {
            double price = Double.parseDouble(priceText);
            // Crear un nuevo juego
            Game newGame = new Game(1, name, genre, price, "Available", 0);
            if (gameService.addGame(newGame)) {
                showAlert("Game added successfully!", "Success", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Failed to add the game.", "Error", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Price must be a valid number.", "Validation Error", Alert.AlertType.ERROR);
        }
    }

    /**
     * Adds a new game to the database.
     *
     * @param game The game to be added.
     * @return {@code true} if the game was added successfully, {@code false} if the
     *         game already exists.
     */
    public boolean addGame(Game game) {
        if (gameService.isGameInDatabase(game.getName())) {
            return false;
        } else {
            return gameService.addGame(game);
        }
    }

    public void deleteGame(Game game) {
        if (game != null) {
            boolean confirmed = showConfirmationDialog("Are you sure you want to delete this game?");
            if (confirmed) {
                boolean deleted = gameService.deleteGame(game.getId());
                if (deleted) {
                    showAlert("Game deleted successfully!", "Exito", AlertType.INFORMATION);
                } else {
                    showAlert("Failed to delete game.", "Error", AlertType.ERROR);
                }
            }
        } else {
            showAlert("Please select a game to delete.", "Alerta", AlertType.WARNING);
        }
    }

    private void showAlert(String message, String title, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String message) {
        // Mostrar un cuadro de confirmación
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }

}
