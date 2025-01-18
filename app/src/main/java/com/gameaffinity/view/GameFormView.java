package com.gameaffinity.view;

import com.gameaffinity.model.Game;
import com.gameaffinity.controller.GameManagementController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GameFormView {

    @FXML
    private TextField nameField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField priceField;

    @FXML
    private Button saveButton;

    private Game game;
    private GameManagementController gameManagementController = new GameManagementController();

    @FXML
    private void initialize() {

        if (game != null) {
            System.out.println(game.getName() + " " + game.getGenre() + " " + game.getPrice());
            nameField.setText(game.getName());
            genreField.setText(game.getGenre());
            priceField.setText(String.valueOf(game.getPrice()));
        }

        saveButton.setOnAction(event -> {
            gameManagementController.saveGame(
                    nameField.getText(),
                    genreField.getText(),
                    priceField.getText());
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        });
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
