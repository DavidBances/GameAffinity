package com.gameaffinity.view;

import com.gameaffinity.controller.LibraryController;
import com.gameaffinity.model.Game;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class GameDatabaseView {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private TableView<Game> databaseTable;
    @FXML
    private TableColumn<Game, Integer> idColumn;
    @FXML
    private TableColumn<Game, String> nameColumn;
    @FXML
    private TableColumn<Game, String> genreColumn;
    @FXML
    private TableColumn<Game, Double> priceColumn;

    private final LibraryController libraryController = new LibraryController();
    private int userId;

    public void initialize() {
        // Set up genre combo box
        genreComboBox.getItems().add("All");
        List<String> genres = libraryController.getAllGenres();
        genreComboBox.getItems().addAll(genres);

        // Set up the table columns
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
        priceColumn
                .setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        refreshGameDatabase();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private void refreshGameDatabase() {
        List<Game> games = libraryController.getAllGames();
        databaseTable.setItems(FXCollections.observableArrayList(games));
    }

    private void refreshGameDatabase(String keyword) {
        List<Game> games = libraryController.searchGamesByName(keyword);
        databaseTable.setItems(FXCollections.observableArrayList(games));
    }

    private void refreshGameDatabaseByGenre(String genre) {
        List<Game> games = libraryController.getGamesByGenre(genre);
        databaseTable.setItems(FXCollections.observableArrayList(games));
    }

    @FXML
    private void handleSearchButtonClick() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            refreshGameDatabase(keyword);
        } else {
            refreshGameDatabase();
        }
    }

    @FXML
    private void handleFilterButtonClick() {
        String selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        if (!"All".equalsIgnoreCase(selectedGenre)) {
            refreshGameDatabaseByGenre(selectedGenre);
        } else {
            refreshGameDatabase();
        }
    }

    @FXML
    private void handleAddGameButtonClick() {
        Game selectedGame = databaseTable.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            try {
                boolean success = libraryController.addGameToLibrary(userId, selectedGame.getName());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info");
                alert.setHeaderText(null);
                alert.setContentText(success ? "Game added to library!" : "Game already in library or not found.");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error adding game");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No game selected");
            alert.setContentText("Please select a game to add.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBackButtonClick() {
        // Implement back button logic to navigate back to the previous view
    }
}
