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

public class LibraryView {

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

    private final LibraryController libraryController = new LibraryController();
    private UserBase user;

    public void setUser(UserBase user){
        this.user = user;
        refreshGamesList();
    }

    public void initialize() {
        configureTableColumns();
        loadGenres();

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
            addGame();
            refreshGamesList();
        });
        removeGameButton.setOnAction(e -> {
            removeGame();
            refreshGamesList();
        });
        backButton.setOnAction(e -> {
            back();
            refreshGamesList();
        });
    }

    private void configureTableColumns() {
        // Set up the table columns
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
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

    private void addGame() {
        String gameName = showInputDialog("Enter Game Name to Add:");
        if (gameName != null && !gameName.isEmpty()) {
            try {
                boolean success = libraryController.addGameToLibrary(this.user.getId(), gameName);
                showAlert(success ? "Game added successfully!" : "Failed to add game.", Alert.AlertType.INFORMATION);
                refreshGamesList();
            } catch (Exception ex) {
                showAlert("Error adding game: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void removeGame() {
        Game selectedGame = gamesTable.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            try {
                boolean success = libraryController.removeGame(this.user.getId(), selectedGame.getId());
                showAlert(success ? "Game removed successfully!" : "Failed to remove game.", Alert.AlertType.INFORMATION);
                refreshGamesList();
            } catch (Exception ex) {
                showAlert("Error removing game: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("No game selected.", Alert.AlertType.WARNING);
        }
    }

    private void back() {
        try {
            Stage currentStage = (Stage) gamesTable.getScene().getWindow();
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

    private String showInputDialog(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(message);
        dialog.setTitle("Input");
        return dialog.showAndWait().orElse(null);
    }
}
