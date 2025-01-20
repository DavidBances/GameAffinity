package com.gameaffinity.view;

import com.gameaffinity.controller.LibraryController;
import com.gameaffinity.model.Game;

import com.gameaffinity.model.UserBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class FriendLibraryView {

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

    private UserBase user;

    public void setUser(UserBase user){
        this.user = user;
        refreshGamesList();
    }

    public void initialize() {
        gamesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configureTableColumns();
        loadGenres();

        searchButton.setOnAction(e -> refreshGamesListByName(searchField.getText().trim()));
        filterButton.setOnAction(e -> {
            String selectedGenre = genreComboBox.getValue();
            String search = searchField.getText().trim();
            if ("All".equalsIgnoreCase(selectedGenre)) {
                refreshGamesList();
            } else if (!searchField.getText().trim().isEmpty()){
                refreshGamesListByGenreAndName(selectedGenre, search);
            }else{
                refreshGamesListByGenre(selectedGenre);
            }
        });
    }

    private void configureTableColumns() {
        // Set up the table columns
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
        priceColumn
                .setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        stateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState()));
        scoreColumn
                .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());
    }

    private void loadGenres() {
        genreComboBox.getItems().clear();
        genreComboBox.getItems().add("All");
        genreComboBox.getItems().addAll(libraryController.getAllGenres());
        genreComboBox.getSelectionModel().selectFirst();
    }

    private void refreshGamesList() {
        List<Game> games = libraryController.getGamesByUserId(user.getId());
        gamesTable.setItems(FXCollections.observableArrayList(games));
    }

    private void refreshGamesListByName(String name) {
        List<Game> games = libraryController.getGamesByNameUser(this.user.getId(), name);
        gamesTable.setItems(FXCollections.observableArrayList(games));
    }

    private void refreshGamesListByGenre(String genre) {
        List<Game> games = libraryController.getGamesByGenreUser(this.user.getId(), genre);
        gamesTable.setItems(FXCollections.observableArrayList(games));
    }

    private void refreshGamesListByGenreAndName(String genre, String name) {
        List<Game> games = libraryController.getGamesByGenreAndNameUser(this.user.getId(), genre, name);
        gamesTable.setItems(FXCollections.observableArrayList(games));
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
