package com.gameaffinity.view;

import com.gameaffinity.model.UserBase;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

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

        private UserBase user;

        public void setUser(UserBase user){
                this.user = user;
        }

        public void initialize() {
                viewLibraryButton
                                .setOnAction(e ->openLibraryView(user));
                manageFriendsButton.setOnAction(
                                e -> openFriendshipView(
                                                (Stage) manageFriendsButton.getScene().getWindow()));
                viewGameDatabaseButton.setOnAction(
                                e -> openGameDatabaseView(
                                                (Stage) viewGameDatabaseButton.getScene().getWindow()));
                modifyProfileButton.setOnAction(e -> openModifyProfileDialog());
                logoutButton.setOnAction(e -> logout());
        }

        public void openLibraryView(UserBase user) {
                try {
                        Stage currentStage = (Stage) viewLibraryButton.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/library/library_view.fxml"));
                        Parent libraryView = loader.load();

                        LibraryView controller = loader.getController();
                        controller.setUser(user);

                        Scene libraryViewScene = new Scene(libraryView);
                        currentStage.setScene(libraryViewScene);
                } catch (Exception e) {
                        showAlert("ERROR.", "Error", Alert.AlertType.ERROR);
                        e.printStackTrace();
                }
        }

        public void openFriendshipView(Stage currentStage) {
                try {
                        Parent friendshipView = FXMLLoader.load(getClass().getResource("/fxml/friendship/friendship_view.fxml"));
                        Scene friendshipViewScene = new Scene(friendshipView);
                        currentStage.setScene(friendshipViewScene);
                } catch (Exception e) {
                        showAlert("ERROR.", "Error", Alert.AlertType.ERROR);
                        e.printStackTrace();
                }
        }

        public void openGameDatabaseView(Stage currentStage) {
                try {
                        Parent gameDatabaseView = FXMLLoader
                                .load(getClass().getResource("/fxml/gameDatabase/game_database_view.fxml"));
                        Scene gameDatabaseViewScene = new Scene(gameDatabaseView);
                        currentStage.setScene(gameDatabaseViewScene);
                } catch (Exception e) {
                        showAlert("ERROR.", "Error", Alert.AlertType.ERROR);
                        e.printStackTrace();
                }
        }

        public void openModifyProfileDialog() {
                try {
                        // Cargar el FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialogs/modify_profile_dialog.fxml"));
                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.setTitle("Modificar Perfil");
                        stage.setScene(new Scene(root));

                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void logout() {
                try {
                        Stage currentStage = (Stage) logoutButton.getScene().getWindow();
                        Parent loginPane = FXMLLoader.load(getClass().getResource("/fxml/auth/login_panel.fxml"));
                        Scene loginPaneScene = new Scene(loginPane);
                        currentStage.setScene(loginPaneScene);
                } catch (Exception e) {
                        showAlert("ERROR.", "Error", Alert.AlertType.ERROR);
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
