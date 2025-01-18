package com.gameaffinity.view;

import com.gameaffinity.controller.UserController;

import javafx.fxml.FXML;
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
                    userController.updateProfile(emailField.getText().trim(), passwordField.getText().trim(),
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
}
