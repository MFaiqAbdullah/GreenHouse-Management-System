package com.greenhouse.controllers;

import com.greenhouse.Main;
import com.greenhouse.models.Role;
import com.greenhouse.models.User;
import com.greenhouse.utils.AppContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainShellController {

    @FXML private Label roleLabel;
    @FXML private Button usersButton;
    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        User user = AppContext.getInstance().getAuthService().getCurrentUser();
        if (user != null) {
            roleLabel.setText("Logged in as: " + user.getRole());
            if (user.getRole() == Role.STAFF) {
                usersButton.setVisible(false);
                usersButton.setManaged(false);
            }
        }
        
        showDashboard();
    }

    @FXML
    private void showDashboard() {
        loadView("/com/greenhouse/views/Dashboard.fxml");
    }

    @FXML
    private void showPlants() {
        loadView("/com/greenhouse/views/Plants.fxml");
    }

    @FXML
    private void showZones() {
        loadView("/com/greenhouse/views/Zones.fxml");
    }

    @FXML
    private void showSensors() {
        loadView("/com/greenhouse/views/Sensors.fxml");
    }

    @FXML
    private void showIrrigation() {
        loadView("/com/greenhouse/views/Irrigation.fxml");
    }

    @FXML
    private void showResources() {
        loadView("/com/greenhouse/views/Resources.fxml");
    }

    @FXML
    private void showAlerts() {
        loadView("/com/greenhouse/views/Alerts.fxml");
    }

    @FXML
    private void showReports() {
        loadView("/com/greenhouse/views/Reports.fxml");
    }

    @FXML
    private void showUsers() {
        if (AppContext.getInstance().getAuthService().getCurrentUser().getRole() == Role.ADMIN) {
            loadView("/com/greenhouse/views/Users.fxml");
        }
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        AppContext.getInstance().getAuthService().logout();
        Main.setRoot("/com/greenhouse/views/Login.fxml", "GreenHouse Management System - Login", 400, 300);
    }
}
