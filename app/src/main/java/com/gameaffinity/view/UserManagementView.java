package com.gameaffinity.view;

import com.gameaffinity.model.UserBase;
import com.gameaffinity.controller.UserManagementController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;

public class UserManagementView {

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

    private UserManagementController userManagementController = new UserManagementController();

    @FXML
    private void initialize() {
        // Configurar las columnas de la tabla
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        roleColumn.setCellFactory(column -> {
            ComboBoxTableCell<UserBase, String> cell = new ComboBoxTableCell<>(
                    FXCollections.observableArrayList("ADMINISTRATOR", "MODERATOR", "REGULAR_USER"));
            return cell;
        });

        roleColumn.setOnEditCommit(event -> {
            UserBase user = event.getRowValue();
            String newRole = event.getNewValue();
            userManagementController.updateUserRole(user, newRole);
            userManagementController.refreshUserTable(userTable);
        });

        deleteUserButton.setOnAction(
                event -> {
                    userManagementController.deleteUser(userTable.getSelectionModel().getSelectedItem());
                    userManagementController.refreshUserTable(userTable);
                });

        userManagementController.refreshUserTable(userTable);
    }
}
