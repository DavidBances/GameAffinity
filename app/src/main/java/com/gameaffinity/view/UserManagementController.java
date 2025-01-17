package com.gameaffinity.view;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.controller.AdminController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

public class UserManagementController {

    @FXML
    private TableView<UserBase> userTable;

    @FXML
    private TableColumn<UserBase, String> nameColumn;

    @FXML
    private TableColumn<UserBase, String> emailColumn;

    @FXML
    private TableColumn<UserBase, String> roleColumn;

    @FXML
    private Button updateRoleButton;

    @FXML
    private Button deleteUserButton;

    private AdminController adminController = new AdminController();

    @FXML
    private void initialize() {
        // Configurar las columnas de la tabla
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));

        refreshUserTable();
    }

    private void refreshUserTable() {
        // Código para refrescar la tabla con los usuarios

        ObservableList<UserBase> userObservableList = FXCollections.observableArrayList(adminController.getAllUsers());

        userTable.setItems(userObservableList);
    }

    @FXML
    private void handleUpdateRole(ActionEvent event) {
        UserBase selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Mostrar un cuadro de diálogo para cambiar el rol
            ComboBox<String> roleComboBox = new ComboBox<>();
            roleComboBox.getItems().addAll("ADMINISTRATOR", "MODERATOR", "REGULAR_USER");
            roleComboBox.setValue(selectedUser.getRole()); // Valor actual del rol

            // Mostrar el cuadro de diálogo para que el admin seleccione un nuevo rol
            String newRole = roleComboBox.getSelectionModel().getSelectedItem();
            if (newRole != null) {
                adminController.assignUserRole(selectedUser.getId(), newRole);
                refreshUserTable(); // Actualizar tabla
                showAlert("Role updated successfully!", AlertType.INFORMATION);
            }
        }
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        UserBase selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Confirmar eliminación
            boolean confirmed = showConfirmationDialog("Are you sure you want to delete this user?");
            if (confirmed) {
                boolean deleted = adminController.deleteUser(selectedUser.getId());
                if (deleted) {
                    refreshUserTable();
                    showAlert("User deleted successfully!", AlertType.INFORMATION);
                } else {
                    showAlert("Failed to delete user.", AlertType.ERROR);
                }
            }
        }
    }

    private void showAlert(String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
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
