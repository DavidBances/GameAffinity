package com.gameaffinity.view;

import com.gameaffinity.controller.AdminController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminDashboardView {

    @FXML
    private StackPane mainContent;

    @FXML
    private Button manageUsersButton;

    @FXML
    private Button manageGamesButton;

    @FXML
    private Button logoutButton;

    private AdminController adminController = new AdminController();

    @FXML
    public void initialize() {
        manageUsersButton.setOnAction(
                event -> adminController.openUserManagementView(mainContent));
        manageGamesButton.setOnAction(
                event -> adminController.openGamesManagementView(mainContent));
        logoutButton.setOnAction(event -> adminController.logout((Stage) mainContent.getScene().getWindow()));

    }
}
