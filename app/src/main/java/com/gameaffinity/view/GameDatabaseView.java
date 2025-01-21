package com.gameaffinity.view;

import com.gameaffinity.controller.LibraryController;
import com.gameaffinity.model.Game;

import com.gameaffinity.model.UserBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class GameDatabaseView {

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private Button filterButton;
    @FXML
    private TableView<Game> databaseTable;
    @FXML
    private TableColumn<Game, String> nameColumn;
    @FXML
    private TableColumn<Game, String> genreColumn;
    @FXML
    private TableColumn<Game, Double> priceColumn;
    @FXML
    private Button addGameButton;
    @FXML
    private Button backButton;

    private final LibraryController libraryController = new LibraryController();
    private UserBase user;

    public void initialize() {
        databaseTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configureTableColumns();
        loadGenres();

        searchButton.setOnAction(e -> refreshGameDatabaseByName(searchField.getText().trim()));
        filterButton.setOnAction(e -> {
            String selectedGenre = genreComboBox.getValue();
            String search = searchField.getText().trim();
            if ("All".equalsIgnoreCase(selectedGenre)) {
                refreshGameDatabase();
            } else if (!searchField.getText().trim().isEmpty()){
                refreshGameDatabaseByGenreAndName(selectedGenre, search);
            }else{
                refreshGameDatabaseByGenre(selectedGenre);
            }
        });

        addGameButton.setOnAction(e -> {
            addGame();
            refreshGameDatabase();
        });
        backButton.setOnAction(e -> {
            back();
            refreshGameDatabase();
        });
    }

    public void setUser(UserBase user) {
        this.user = user;
    }

    private void loadGenres(){
        genreComboBox.getItems().clear();
        genreComboBox.getItems().add("All");
        genreComboBox.getItems().addAll(libraryController.getAllGenres());
        genreComboBox.getSelectionModel().selectFirst();
    }


    private void configureTableColumns() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        genreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenre()));
        priceColumn
                .setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        refreshGameDatabase();
    }

    private void refreshGameDatabase() {
        List<Game> games = libraryController.getAllGames();
        databaseTable.setItems(FXCollections.observableArrayList(games));
    }

    private void refreshGameDatabaseByName(String keyword) {
        List<Game> games = libraryController.getGamesByNameUser(this.user.getId(), keyword);
        databaseTable.setItems(FXCollections.observableArrayList(games));
    }

    private void refreshGameDatabaseByGenre(String genre) {
        List<Game> games = libraryController.getGamesByGenreUser(this.user.getId(), genre);
        databaseTable.setItems(FXCollections.observableArrayList(games));
    }

    private void refreshGameDatabaseByGenreAndName(String genre, String name) {
        List<Game> games = libraryController.getGamesByGenreAndNameUser(this.user.getId(), genre, name);
        databaseTable.setItems(FXCollections.observableArrayList(games));
    }

    private void addGame() {
        Game selectedGame = databaseTable.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            try {
                boolean success = libraryController.addGameToLibrary(this.user.getId(), selectedGame.getName());
                showAlert(success ? "Game added to library!" : "Game already in library or not found.", success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            } catch (Exception ex) {
                showAlert(ex.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Please select a game to add.", Alert.AlertType.WARNING);
        }
    }

    private void back() {
        try {
            Stage currentStage = (Stage) databaseTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user/user_dashboard.fxml"));
            Parent userDashboard = loader.load();

            UserDashboardView controller = loader.getController();
            controller.setUser(this.user);

            Scene userScene = new Scene(userDashboard);
            currentStage.setScene(userScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
