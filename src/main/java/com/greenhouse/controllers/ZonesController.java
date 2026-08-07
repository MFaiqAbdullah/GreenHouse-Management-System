package com.greenhouse.controllers;

import com.greenhouse.models.Plant;
import com.greenhouse.models.Zone;
import com.greenhouse.models.GreenhouseState;
import com.greenhouse.utils.AppContext;
import com.greenhouse.utils.Updatable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.util.List;

public class ZonesController implements Updatable {

    @FXML private ListView<Zone> zonesList;
    @FXML private VBox detailPane;
    @FXML private Label detailName;
    @FXML private Label detailDesc;
    
    @FXML private Label lblTemp;
    @FXML private Label lblHum;
    @FXML private Label lblSoil;
    
    @FXML private Label irrigationStatus;
    @FXML private ListView<String> zonePlantsList;

    private Zone selectedZone;

    @FXML
    public void initialize() {
        AppContext.getInstance().registerListener(this);
        List<Zone> zones = AppContext.getInstance().getZoneService().getAllZones();
        zonesList.getItems().setAll(zones);

        zonesList.setCellFactory(param -> new ListCell<Zone>() {
            @Override
            protected void updateItem(Zone item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Zone " + item.getId() + ": " + item.getName());
                }
            }
        });

        zonesList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showZoneDetails(newVal);
            }
        });
    }

    private void showZoneDetails(Zone zone) {
        this.selectedZone = zone;
        detailPane.setVisible(true);
        detailName.setText(zone.getName() + " (ID: " + zone.getId() + ")");
        detailDesc.setText(zone.getDescription());

        GreenhouseState state = AppContext.getInstance().getState();
        lblTemp.setText(String.format("%.1f °C", state.getTemperature()));
        lblHum.setText(String.format("%.1f %%", state.getHumidity()));
        lblSoil.setText(String.format("%.1f %%", state.getSoilMoisture()));

        updateIrrigationStatus();

        List<Plant> plants = AppContext.getInstance().getPlantService().getPlantsByZone(zone.getId());
        zonePlantsList.getItems().clear();
        for (Plant p : plants) {
            zonePlantsList.getItems().add(p.getName() + " (" + p.getSpecies() + ") - " + p.getStatus());
        }
    }

    private void updateIrrigationStatus() {
        if (selectedZone == null) return;
        boolean isRunning = AppContext.getInstance().getIrrigationService().isIrrigating(selectedZone.getId());
        if (isRunning) {
            irrigationStatus.setText("RUNNING");
            irrigationStatus.setStyle("-fx-text-fill: green;");
        } else {
            irrigationStatus.setText("STOPPED");
            irrigationStatus.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleStartIrrigation() {
        if (selectedZone != null) {
            AppContext.getInstance().getIrrigationService().startIrrigation(selectedZone.getId());
            updateIrrigationStatus();
        }
    }

    @FXML
    private void handleStopIrrigation() {
        if (selectedZone != null) {
            AppContext.getInstance().getIrrigationService().stopIrrigation(selectedZone.getId());
            updateIrrigationStatus();
        }
    }

    @Override
    public void updateUI() {
        if (selectedZone != null) {
            showZoneDetails(selectedZone);
        }
    }
}
