package com.gameaffinity.controller;

import java.util.List;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.service.UserService;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

public class UserManagementController {

    private UserService userService;

    public UserManagementController() {
        try {
            this.userService = new UserService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshUserTable(TableView<UserBase> userTable) {
        userTable.getItems().clear();
        userTable.getItems().addAll(this.getAllUsers());
    }

    /**
     * Retrieves a list of all users from the database.
     *
     * @return A list of all users. Returns {@code null} if an error occurs.
     */
    public List<UserBase> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            showAlert("ERROR.", "Error", AlertType.ERROR);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Assigns a new role to a user.
     *
     * @param adminId The ID of the admin making the change.
     * @param userId  The ID of the user whose role is being updated.
     * @param newRole The new role to be assigned to the user.
     */
    public void updateUserRole(UserBase user, String newRole) {
        try {
            userService.updateUserRole(user.getId(), newRole);
            user.setRole(newRole);
            showAlert("El rol ha sido cambiado correctamente.", "Exito", AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("ERROR.", "Error", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param userId The ID of the user to be deleted.
     * @return {@code true} if the user was deleted successfully, {@code false}
     *         otherwise.
     */
    public void deleteUser(UserBase user) {
        if (user != null) {
            boolean confirmed = showConfirmationDialog("Are you sure you want to delete this user?");
            if (confirmed) {
                boolean deleted = userService.deleteUser(user.getId());
                if (deleted) {
                    showAlert("User deleted successfully!", "Exito", AlertType.INFORMATION);
                } else {
                    showAlert("Failed to delete user.", "Error", AlertType.ERROR);
                }
            }
        } else {
            showAlert("Please select a user to delete.", "Alerta", AlertType.WARNING);
        }
    }

    private void showAlert(String message, String title, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String message) {
        // Mostrar un cuadro de confirmación
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }
}
