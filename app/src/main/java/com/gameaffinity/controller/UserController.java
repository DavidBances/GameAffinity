package com.gameaffinity.controller;

import java.io.IOException;
import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The UserController class manages user-related operations such as updating
 * user profiles in the application.
 *
 * @author Level Track
 * @since 1.0
 */
public class UserController {

    private UserService userService;

    /**
     * Constructs a new UserController and initializes the UserService.
     *
     * @throws Exception if there is an issue initializing the UserService.
     */
    public UserController() {
        try {
            this.userService = new UserService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openLibraryView(Stage currentStage) {
        try {
            Pane libraryView = FXMLLoader.load(getClass().getResource("/fxml/library/library_view.fxml"));
            Scene libraryViewScene = new Scene(libraryView);
            currentStage.setScene(libraryViewScene);
        } catch (Exception e) {
            showAlert("ERROR.", "Error", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void openFriendshipView(Stage currentStage) {
        try {
            Pane friendshipView = FXMLLoader.load(getClass().getResource("/fxml/friendship/friendship_view.fxml"));
            Scene friendshipViewScene = new Scene(friendshipView);
            currentStage.setScene(friendshipViewScene);
        } catch (Exception e) {
            showAlert("ERROR.", "Error", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void openGameDatabaseView(Stage currentStage) {
        try {
            Pane gameDatabaseView = FXMLLoader
                    .load(getClass().getResource("/fxml/gameDatabase/game_database_view.fxml"));
            Scene gameDatabaseViewScene = new Scene(gameDatabaseView);
            currentStage.setScene(gameDatabaseViewScene);
        } catch (Exception e) {
            showAlert("ERROR.", "Error", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void openModifyProfileDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/modify_profile_dialog.fxml"));
            DialogPane dialogPane = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Modificar Perfil");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            UserBase authenticated = userService.authenticate(email, password);
            if (authenticated == null) {
                showAlert("Error: Contraseña incorrecta o usuario no encontrado.", "Error", AlertType.WARNING);
                return;
            }

            if (newName.isEmpty()) {
                newName = authenticated.getName();
            }
            if (newEmail.isEmpty()) {
                newEmail = authenticated.getEmail();
            }
            if (newPassword.isEmpty()) {
                newPassword = authenticated.getPassword();
            }

            boolean success = userService.updateUserProfile(authenticated.getId(), newName, newEmail,
                    newPassword);
            if (success) {
                showAlert("Perfil actualizado con éxito.", "Exito", AlertType.INFORMATION);
            } else {
                showAlert("Error al actualizar el perfil.", "Error", AlertType.ERROR);
                return;
            }
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage(), "Alerta", AlertType.WARNING);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void logout(Stage currentStage) {
        try {
            Pane loginPane = FXMLLoader.load(getClass().getResource("/fxml/auth/login_panel.fxml"));
            Scene loginPaneScene = new Scene(loginPane);
            currentStage.setScene(loginPaneScene);
        } catch (Exception e) {
            showAlert("ERROR.", "Error", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String message, String title, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
