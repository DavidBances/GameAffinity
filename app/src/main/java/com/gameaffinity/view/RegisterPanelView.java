package com.gameaffinity.view;

import com.gameaffinity.controller.RegisterController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterPanelView {
    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button createButton;

    @FXML
    private Button backButton;

    private final RegisterController registerController = new RegisterController();

    @FXML
    public void initialize() {
        createButton.setOnAction(
                event -> registerController.register(nameField.getText(), emailField.getText(),
                        passwordField.getText()));
        backButton.setOnAction(event -> registerController.back((Stage) backButton.getScene().getWindow()));
    }
}
