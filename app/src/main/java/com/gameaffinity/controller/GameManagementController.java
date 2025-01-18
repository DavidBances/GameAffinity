package com.gameaffinity.controller;

import java.io.IOException;
import java.util.List;

import com.gameaffinity.model.Game;
import com.gameaffinity.service.GameService;
import com.gameaffinity.view.GameFormView;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;

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

    public void openGameFormDialog(Game game) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/game_form_dialog.fxml"));
            DialogPane dialogPane = loader.load();

            GameFormView controller = loader.getController();
            if (game != null) {
                controller.setGame(game);
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Modificar Perfil");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGame(int gameId, String name, String genre, String priceText) {
        Game game = gameService.getGameByName(name);

        if (name.isEmpty()) {
            name = game.getName();
        }
        if (genre.isEmpty()) {
            genre = game.getGenre();
        }

        double price = 0;
        if (priceText.isEmpty()) {
            price = game.getPrice();
        }

        try {
            price = Double.parseDouble(priceText);

            if (gameId != -1) {
                // Actualizar juego existente
                game.setName(name);
                game.setGenre(genre);
                game.setPrice(price);

                if (gameService.updateGame(game)) {
                    showAlert("Game updated successfully!", "Success", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Failed to update the game.", "Error", Alert.AlertType.ERROR);
                }
            } else {
                // Crear un nuevo juego
                Game newGame = new Game(gameId, name, genre, price, "Available", 0);
                if (gameService.addGame(newGame)) {
                    showAlert("Game added successfully!", "Success", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Failed to add the game.", "Error", Alert.AlertType.ERROR);
                }
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
