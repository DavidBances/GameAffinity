package com.gameaffinity.view;

import com.gameaffinity.controller.UserController;

import com.gameaffinity.model.UserBase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyProfileDialog {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField newNameField;
    @FXML
    private TextField newEmailField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private Button updateButton;
    @FXML
    private Button cancelButton;

    private final UserController userController = new UserController();

    private boolean isUpdateDisabled() {
        return emailField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty();
    }

    @FXML
    private void initialize() {

        updateButton.setDisable(isUpdateDisabled());
        emailField.textProperty()
                .addListener((observable, oldValue, newValue) -> updateButton.setDisable(isUpdateDisabled()));
        passwordField.textProperty()
                .addListener((observable, oldValue, newValue) -> updateButton.setDisable(isUpdateDisabled()));

        updateButton.setOnAction(
                event -> {
                    updateProfile(emailField.getText().trim(), passwordField.getText().trim(),
                            newNameField.getText().trim(),
                            newEmailField.getText().trim(),
                            newPasswordField.getText().trim());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    stage.close();
                });

        cancelButton.setOnAction(event -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Updates the user's profile with new information.
     *
     * @param identifier  The unique identifier (e.g., user ID or email) for the
     *                    user.
     * @param password    The current password of the user.
     * @param newName     The new name for the user (optional, can be null).
     * @param newEmail    The new email address for the user (optional, can be
     *                    null).
     * @param newPassword The new password for the user (optional, can be null).
     * @return A message indicating the success or failure of the operation.
     */
    public void updateProfile(String email, String password, String newName, String newEmail,
                              String newPassword) {

        try {
            UserBase authenticated = userController.authenticate(email, password);
            if (authenticated == null) {
                showAlert("Error: Contraseña incorrecta o usuario no encontrado.", "Error", Alert.AlertType.WARNING);
            }

            boolean success = userController.updateProfile(email, password, newName, newEmail,
                    newPassword);
            if (success) {
                showAlert("Perfil actualizado con éxito.", "Exito", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error al actualizar el perfil.", "Error", Alert.AlertType.ERROR);
            }
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage(), "Alerta", Alert.AlertType.WARNING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
