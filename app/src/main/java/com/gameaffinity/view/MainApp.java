package com.gameaffinity.view;

import com.gameaffinity.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
//import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar el archivo FXML de LoginPanel
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/login_panel.fxml"));
            AnchorPane root = loader.load();

            // Configurar la escena
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Game Affinity");
            // primaryStage.getIcons().add(new
            // Image(getClass().getClassLoader().getResource("LevelTrackLogo.png").toExternalForm()));

            // Obtener el controlador de la vista FXML y configurar la lógica
            LoginController loginController = new LoginController();
            System.out.println(loginController);
            LoginPanelView loginPanelController = loader.getController();
            loginPanelController.setLoginController(loginController);

            // Mostrar la ventana
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
