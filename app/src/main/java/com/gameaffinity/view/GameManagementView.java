package com.gameaffinity.view;

import com.gameaffinity.controller.GameManagementController;
import com.gameaffinity.model.Game;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private Button updateGameButton;
    @FXML
    private Button deleteGameButton;

    private GameManagementController gameManagementController = new GameManagementController();

    @FXML
    public void initialize() {
        // Initialize table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        addGameButton.setOnAction(event -> {
            gameManagementController.openGameFormDialog(null);
            gameManagementController.refreshGameTable(gameTable);
        });

        updateGameButton.setOnAction(event -> {
            gameManagementController.openGameFormDialog(gameTable.getSelectionModel().getSelectedItem());
            gameManagementController.refreshGameTable(gameTable);
        });

        deleteGameButton.setOnAction(event -> {
            gameManagementController.deleteGame(gameTable.getSelectionModel().getSelectedItem());
            gameManagementController.refreshGameTable(gameTable);
        });

        gameManagementController.refreshGameTable(gameTable);
    }
}
