package com.greenhouse.controllers;

import com.greenhouse.Main;
import com.greenhouse.utils.AppContext;
import com.greenhouse.exceptions.AuthenticationException;
import com.greenhouse.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = AppContext.getInstance().getAuthService().login(username, password);
            errorLabel.setVisible(false);
            
            // Switch to Main Shell
            Main.setRoot("/com/greenhouse/views/MainShell.fxml", "GreenHouse Dashboard - " + user.getRole(), 1000, 700);
        } catch (AuthenticationException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }
}
