package com.greenhouse.controllers;

import com.greenhouse.models.Zone;
import com.greenhouse.models.Resource;
import com.greenhouse.models.ResourceType;
import com.greenhouse.utils.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

import com.greenhouse.utils.Updatable;
import java.util.List;

public class IrrigationController implements Updatable {

    @FXML private Label waterLevelLabel;
    @FXML private ProgressBar waterProgress;
    @FXML private ListView<Zone> zoneIrrigationList;

    @FXML
    public void initialize() {
        AppContext.getInstance().registerListener(this);
        updateWaterLevel();

        List<Zone> zones = AppContext.getInstance().getZoneService().getAllZones();
        zoneIrrigationList.getItems().setAll(zones);
        
        zoneIrrigationList.setCellFactory(param -> new ListCell<Zone>() {
            @Override
            protected void updateItem(Zone zone, boolean empty) {
                super.updateItem(zone, empty);
                if (empty || zone == null) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(15);
                    box.setStyle("-fx-alignment: center-left; -fx-padding: 10;");
                    Label nameLbl = new Label(zone.getName() + " (ID: " + zone.getId() + ")");
                    nameLbl.setPrefWidth(200);

                    boolean running = AppContext.getInstance().getIrrigationService().isIrrigating(zone.getId());
                    Label statusLbl = new Label(running ? "RUNNING" : "STOPPED");
                    statusLbl.setStyle(running ? "-fx-text-fill: green; -fx-font-weight: bold;" : "-fx-text-fill: gray;");
                    statusLbl.setPrefWidth(100);

                    Button startBtn = new Button("Start");
                    startBtn.getStyleClass().add("primary-button");
                    startBtn.setDisable(running);
                    
                    Button stopBtn = new Button("Stop");
                    stopBtn.setDisable(!running);

                    startBtn.setOnAction(e -> {
                        AppContext.getInstance().getIrrigationService().startIrrigation(zone.getId());
                        updateWaterLevel(); // Just in case it updates something immediately
                        getListView().refresh();
                    });

                    stopBtn.setOnAction(e -> {
                        AppContext.getInstance().getIrrigationService().stopIrrigation(zone.getId());
                        getListView().refresh();
                    });

                    box.getChildren().addAll(nameLbl, statusLbl, startBtn, stopBtn);
                    setGraphic(box);
                }
            }
        });
    }

    private void updateWaterLevel() {
        Resource water = AppContext.getInstance().getResourceService().getResource(ResourceType.WATER);
        if (water != null) {
            double lvl = water.getQuantity();
            waterLevelLabel.setText(String.format("%.1f L", lvl));
            waterProgress.setProgress(Math.min(1.0, lvl / 5000.0)); // Assume max tank is 5000L
        }
    }

    @Override
    public void updateUI() {
        updateWaterLevel();
        zoneIrrigationList.refresh();
    }
}
