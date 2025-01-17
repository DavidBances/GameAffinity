package com.gameaffinity.view;

import com.gameaffinity.controller.RegisterController;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterPanelController {
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
        createButton.setOnAction(event -> handleCreateAccount());
        backButton.setOnAction(event -> handleBack());
    }

    private void handleCreateAccount() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (registerController.register(name, email, password)) {
            // Lógica al crear la cuenta exitosamente
            System.out.println("Account created successfully.");
        } else {
            // Manejo de errores al crear la cuenta
            System.out.println("Error creating account.");
        }
    }

    private void handleBack() {
        // Lógica para manejar el botón "Back"
        System.out.println("Back button clicked.");
    }
}
