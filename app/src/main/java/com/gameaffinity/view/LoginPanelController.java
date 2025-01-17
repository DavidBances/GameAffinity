package com.gameaffinity.view;

import com.gameaffinity.controller.LoginController;
import com.gameaffinity.model.UserBase;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginPanelController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    private LoginController loginController;

    // Establecer el controlador de Login
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    // Manejar el login
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        try {
            UserBase user = loginController.login(email, password);
            if (user != null) {
                // Redirigir a la siguiente vista según el rol del usuario
                if ("ADMINISTRATOR".equalsIgnoreCase(user.getRole())) {
                    // Cargar el panel de administrador
                } else {
                    // Cargar el panel de usuario
                }
            } else {
                showAlert("Invalid credentials.", "Error", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("An error occurred.", "Error", Alert.AlertType.ERROR);
        }
    }

    // Manejar la creación de cuenta
    @FXML
    private void handleRegister() {
        // Abrir la ventana de registro
    }

    // Mostrar un mensaje de alerta
    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
