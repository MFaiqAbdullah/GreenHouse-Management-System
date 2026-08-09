package com.greenhouse.controllers;

import com.greenhouse.models.Role;
import com.greenhouse.models.User;
import com.greenhouse.models.Admin;
import com.greenhouse.models.Staff;
import com.greenhouse.services.AuthenticationService;
import com.greenhouse.utils.AppContext;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class UsersController {

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> colRole;
    
    @FXML private VBox formPane;
    @FXML private TextField fUsername;
    @FXML private PasswordField fPassword;
    @FXML private ComboBox<Role> fRole;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        User currentUser = AppContext.getInstance().getAuthService().getCurrentUser();
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            throw new SecurityException("Staff cannot access User Management.");
        }

        colRole.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole().name()));
        fRole.setItems(FXCollections.observableArrayList(Role.values()));

        refreshTable();
    }

    private void refreshTable() {
        usersTable.setItems(FXCollections.observableArrayList(AppContext.getInstance().getAuthService().getAllUsers()));
    }

    @FXML private void handleAdd() {
        fUsername.clear();
        fPassword.clear();
        fRole.setValue(null);
        errorLabel.setVisible(false);
        formPane.setVisible(true);
        formPane.setManaged(true);
    }

    @FXML private void handleDelete() {
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        if (selected.getId() == AppContext.getInstance().getAuthService().getCurrentUser().getId()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setContentText("You cannot delete yourself!");
            alert.show();
            return;
        }

        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Delete user " + selected.getUsername() + "?");
        Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            AppContext.getInstance().getAuthService().deleteUser(selected.getId());
            refreshTable();
        }
    }

    @FXML private void handleSave() {
        String user = fUsername.getText();
        String pass = fPassword.getText();
        Role role = fRole.getValue();

        if (user.isEmpty() || pass.isEmpty() || role == null) {
            errorLabel.setText("All fields required.");
            errorLabel.setVisible(true);
            return;
        }

        AuthenticationService auth = AppContext.getInstance().getAuthService();
        for (User u : auth.getAllUsers()) {
            if (u.getUsername().equals(user)) {
                errorLabel.setText("Username already exists.");
                errorLabel.setVisible(true);
                return;
            }
        }

        int newId = auth.getAllUsers().stream().mapToInt(User::getId).max().orElse(0) + 1;
        User newUser = (role == Role.ADMIN) ? new Admin(newId, user, pass) : new Staff(newId, user, pass);
        auth.addUser(newUser);

        handleCancel();
        refreshTable();
    }

    @FXML private void handleCancel() {
        formPane.setVisible(false);
        formPane.setManaged(false);
    }
}
