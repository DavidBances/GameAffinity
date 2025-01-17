package com.gameaffinity.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserDashboardController {

    @FXML
    private Button viewLibraryButton;
    @FXML
    private Button manageFriendsButton;
    @FXML
    private Button viewGameDatabaseButton;
    @FXML
    private Button modifyProfileButton;
    @FXML
    private Button logoutButton;

    private int userId;
    private String userRole;
    private Stage primaryStage;

    public void initialize(int userId, String userRole, Stage primaryStage) {
        this.userId = userId;
        this.userRole = userRole;
        this.primaryStage = primaryStage;

        setupListeners();
    }

    private void setupListeners() {
        viewLibraryButton.setOnAction(e -> openLibraryView());
        manageFriendsButton.setOnAction(e -> openFriendshipView());
        viewGameDatabaseButton.setOnAction(e -> openGameDatabaseView());
        modifyProfileButton.setOnAction(e -> openModifyProfileDialog());
        logoutButton.setOnAction(e -> logout());
    }

    private void openLibraryView() {
        // Lógica para cambiar la vista a la biblioteca del usuario
    }

    private void openFriendshipView() {
        // Lógica para abrir la gestión de amigos
    }

    private void openGameDatabaseView() {
        // Lógica para mostrar la base de datos de juegos
    }

    private void openModifyProfileDialog() {
        // Lógica para abrir un diálogo para modificar el perfil
    }

    private void logout() {
        // Lógica para cerrar sesión
    }
}
