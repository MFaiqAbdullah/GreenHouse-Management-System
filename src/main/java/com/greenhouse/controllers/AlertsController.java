package com.greenhouse.controllers;

import com.greenhouse.models.Alert;
import com.greenhouse.utils.AppContext;
import com.greenhouse.utils.Updatable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableRow;

public class AlertsController implements Updatable {

    @FXML private TableView<Alert> alertsTable;
    @FXML private TableColumn<Alert, String> colSeverity;
    @FXML private TableColumn<Alert, String> colResolved;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        AppContext.getInstance().registerListener(this);
        colSeverity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeverity().name()));
        colResolved.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isResolved() ? "RESOLVED" : "ACTIVE"));

        alertsTable.setRowFactory(tv -> new TableRow<Alert>() {
            @Override
            protected void updateItem(Alert item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (!item.isResolved()) {
                    switch (item.getSeverity()) {
                        case CRITICAL:
                            setStyle("-fx-background-color: #ffcccc;");
                            break;
                        case WARNING:
                            setStyle("-fx-background-color: #ffffcc;");
                            break;
                        default:
                            setStyle("");
                    }
                } else {
                    setStyle("-fx-background-color: #ccffcc;"); // Resolved
                }
            }
        });

        refreshTable();
    }

    private void refreshTable() {
        alertsTable.setItems(FXCollections.observableArrayList(AppContext.getInstance().getAlertService().getAllAlerts()));
    }

    @FXML
    private void handleResolve() {
        Alert selected = alertsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Select an alert to resolve.");
            errorLabel.setVisible(true);
            return;
        }
        if (selected.isResolved()) {
            errorLabel.setText("Alert is already resolved.");
            errorLabel.setVisible(true);
            return;
        }
        
        AppContext.getInstance().getAlertService().resolveAlert(selected.getId());
        errorLabel.setVisible(false);
        refreshTable();
    }

    @Override
    public void updateUI() {
        refreshTable();
    }
}
