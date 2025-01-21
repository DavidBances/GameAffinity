package com.gameaffinity.view;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.controller.UserManagementController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.Stage;

public class UserManagementView {

    @FXML
    public Button backButton;

    @FXML
    private TableView<UserBase> userTable;

    @FXML
    private TableColumn<UserBase, String> nameColumn;

    @FXML
    private TableColumn<UserBase, String> emailColumn;

    @FXML
    private TableColumn<UserBase, String> roleColumn;

    @FXML
    private Button deleteUserButton;

    private final UserManagementController userManagementController = new UserManagementController();

    @FXML
    private void initialize() {
        // Configurar las columnas de la tabla
        userTable.setEditable(true);
        roleColumn.setEditable(true);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        roleColumn.setCellFactory(column -> new ComboBoxTableCell<>(
                FXCollections.observableArrayList("ADMINISTRATOR", "MODERATOR", "REGULAR_USER")));

        roleColumn.setOnEditCommit(event -> {
            UserBase user = event.getRowValue();
            String newRole = event.getNewValue();
            if (userManagementController.updateUserRole(user, newRole)){
                showAlert("Rol modificado con éxito.", "Exito", Alert.AlertType.INFORMATION);
            }else{
                showAlert("Failed to modify role.", "Error", Alert.AlertType.ERROR);
            }
            refreshUserTable();
        });

        deleteUserButton.setOnAction(
                event -> {
                    deleteUser(userTable.getSelectionModel().getSelectedItem());
                    refreshUserTable();
                });

        backButton.setOnAction(event -> back());

        refreshUserTable();
    }

    public void refreshUserTable() {
        userTable.getItems().clear();
        userTable.getItems().addAll(userManagementController.getAllUsers());
    }

    public void deleteUser(UserBase user){
        if (user != null) {
            boolean confirmed = showConfirmationDialog("Are you sure you want to delete this user?");
            if (confirmed) {
                boolean deleted = userManagementController.deleteUser(user);
                if (deleted) {
                    showAlert("User deleted successfully!", "Exito", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Failed to delete user.", "Error", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Please select a user to delete.", "Alerta", Alert.AlertType.WARNING);
        }
    }

    public void back(){
        try{
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            Parent adminDashboard = FXMLLoader.load(getClass().getResource("/fxml/admin/admin_dashboard.fxml"));
            Scene adminScene = new Scene(adminDashboard);
            currentStage.setScene(adminScene);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String message) {
        // Mostrar un cuadro de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        return alert.showAndWait().get() == ButtonType.OK;
    }
}
