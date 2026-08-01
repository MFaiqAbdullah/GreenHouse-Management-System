package com.greenhouse;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Main application class to launch the GreenHouse Management System.
 */
public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/greenhouse/views/Login.fxml"));
            Scene scene = new Scene(root, 400, 300);
            scene.getStylesheets().add(getClass().getResource("/com/greenhouse/css/style.css").toExternalForm());
            primaryStage.setTitle("GreenHouse Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml, String title, int width, int height) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxml));
            Scene scene = new Scene(root, width, height);
            scene.getStylesheets().add(Main.class.getResource("/com/greenhouse/css/style.css").toExternalForm());
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        com.greenhouse.controllers.DashboardController.shutdownSimulation();
        super.stop();
    }
}
