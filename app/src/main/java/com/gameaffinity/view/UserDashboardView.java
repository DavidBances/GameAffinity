package com.gameaffinity.view;

import com.gameaffinity.controller.UserController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserDashboardView {

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

        private final UserController userController = new UserController();

        public void initialize() {
                viewLibraryButton
                                .setOnAction(e -> userController
                                                .openLibraryView((Stage) viewLibraryButton.getScene().getWindow()));
                manageFriendsButton.setOnAction(
                                e -> userController.openFriendshipView(
                                                (Stage) manageFriendsButton.getScene().getWindow()));
                viewGameDatabaseButton.setOnAction(
                                e -> userController.openGameDatabaseView(
                                                (Stage) viewGameDatabaseButton.getScene().getWindow()));
                modifyProfileButton.setOnAction(e -> userController.openModifyProfileDialog());
                logoutButton.setOnAction(e -> userController.logout((Stage) logoutButton.getScene().getWindow()));
        }
}
