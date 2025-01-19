package com.gameaffinity.view;

import com.gameaffinity.controller.LibraryController;
import com.gameaffinity.model.Game;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class FriendLibraryView {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> genreComboBox;
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

    private final LibraryController libraryController = new LibraryController();
    private int friendId;

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
        stateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState()));
        scoreColumn
                .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());

        refreshFriendGamesList();
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    private void refreshFriendGamesList() {
        List<Game> games = libraryController.getGamesByUserId(friendId);
        gamesTable.setItems(FXCollections.observableArrayList(games));
    }

    private void refreshFriendGamesListByName(String name) {
        List<Game> games = libraryController.getGamesByNameUser(this.friendId, name);
        gamesTable.setItems(FXCollections.observableArrayList(games));
    }

    private void refreshFriendGamesListByGenre(String genre) {
        List<Game> games = libraryController.getGamesByGenreUser(this.friendId, genre);
        gamesTable.setItems(FXCollections.observableArrayList(games));
    }

    @FXML
    private void handleSearchButtonClick() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            refreshFriendGamesListByName(keyword);
        } else {
            refreshFriendGamesList();
        }
    }

    @FXML
    private void handleFilterButtonClick() {
        String selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        if ("All".equalsIgnoreCase(selectedGenre)) {
            refreshFriendGamesList();
        } else {
            refreshFriendGamesListByGenre(selectedGenre);
        }
    }

    @FXML
    private void handleBackButtonClick() {
        // Implement back button logic to navigate back to the previous view
    }
}
