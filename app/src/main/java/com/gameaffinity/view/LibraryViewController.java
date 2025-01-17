package com.gameaffinity.view;

import com.gameaffinity.controller.LibraryController;
import com.gameaffinity.model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LibraryViewController {

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private Button filterButton;
    @FXML
    private TableView<Game> gamesTable;
    @FXML
    private TableColumn<Game, Integer> idColumn;
    @FXML
    private TableColumn<Game, String> nameColumn;
    @FXML
    private TableColumn<Game, String> genreColumn;
    @FXML
    private TableColumn<Game, Double> priceColumn;
    @FXML
    private TableColumn<Game, String> stateColumn;
    @FXML
    private TableColumn<Game, Integer> scoreColumn;
    @FXML
    private Button addGameButton;
    @FXML
    private Button removeGameButton;
    @FXML
    private Button backButton;

    private int userId;
    private String userRole;
    private LibraryController libraryController;

    public void initialize(int userId, String userRole, Stage primaryStage) {
        this.userId = userId;
        this.userRole = userRole;
        this.libraryController = new LibraryController();

        loadGenres();
        setupListeners();
        refreshGamesList();
    }

    private void loadGenres() {
        genreComboBox.getItems().clear();
        genreComboBox.getItems().add("All");
        genreComboBox.getItems().addAll(libraryController.getAllGenres());
    }

    private void setupListeners() {
        searchButton.setOnAction(e -> refreshGamesListByName(searchField.getText().trim()));
        filterButton.setOnAction(e -> {
            String selectedGenre = genreComboBox.getValue();
            if ("All".equalsIgnoreCase(selectedGenre)) {
                refreshGamesList();
            } else {
                refreshGamesListByGenre(selectedGenre);
            }
        });

        addGameButton.setOnAction(e -> {
            String gameName = showInputDialog("Enter Game Name to Add:");
            if (gameName != null && !gameName.isEmpty()) {
                try {
                    boolean success = libraryController.addGameToLibrary(userId, gameName);
                    showAlert(success ? "Game added successfully!" : "Failed to add game.",
                            Alert.AlertType.INFORMATION);
                    refreshGamesList();
                } catch (Exception ex) {
                    showAlert("Error adding game: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });

        removeGameButton.setOnAction(e -> {
            Game selectedGame = gamesTable.getSelectionModel().getSelectedItem();
            if (selectedGame != null) {
                try {
                    boolean success = libraryController.removeGame(userId, selectedGame.getId());
                    showAlert(success ? "Game removed successfully!" : "Failed to remove game.",
                            Alert.AlertType.INFORMATION);
                    refreshGamesList();
                } catch (Exception ex) {
                    showAlert("Error removing game: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                showAlert("No game selected.", Alert.AlertType.WARNING);
            }
        });

        backButton.setOnAction(e -> {
            // Implement logic to return to the main menu
        });
    }

    private void refreshGamesList() {
        // Implement logic to refresh games list
    }

    private void refreshGamesListByName(String name) {
        // Implement logic to refresh games by name
    }

    private void refreshGamesListByGenre(String genre) {
        // Implement logic to refresh games by genre
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String showInputDialog(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(message);
        dialog.setTitle("Input");
        return dialog.showAndWait().orElse(null);
    }
}
