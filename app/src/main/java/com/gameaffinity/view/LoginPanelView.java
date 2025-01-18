package com.gameaffinity.view;

import com.gameaffinity.controller.LoginController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginPanelView {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    private LoginController loginController = new LoginController();

    // Establecer el controlador de Login
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @FXML
    public void initialize() {
        loginButton.setOnAction(
                event -> {
                    loginController.login((Stage) loginButton.getScene().getWindow(), emailField.getText().trim(),
                            passwordField.getText().trim());
                });
        registerButton.setOnAction(event -> loginController.register((Stage) loginButton.getScene().getWindow()));
    }
}
