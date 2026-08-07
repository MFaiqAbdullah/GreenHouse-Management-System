package com.greenhouse.controllers;

import com.greenhouse.models.Plant;
import com.greenhouse.models.PlantStatus;
import com.greenhouse.models.Zone;
import com.greenhouse.models.GreenhouseState;
import com.greenhouse.utils.AppContext;
import com.greenhouse.utils.Updatable;
import com.greenhouse.threads.SensorMonitoringThread;
import com.greenhouse.threads.IrrigationThread;
import com.greenhouse.threads.PlantGrowthThread;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardController implements Updatable {

    @FXML private Label totalPlantsLabel;
    @FXML private Label healthyPlantsLabel;
    @FXML private Label activeAlertsLabel;
    
    @FXML private Label tempLabel;
    @FXML private Label humidityLabel;
    @FXML private Label waterLabel;
    
    @FXML private FlowPane zonesPane;

    @FXML private Label sensorStatus;
    @FXML private Label irrigationStatus;
    @FXML private Label plantStatus;

    private static ExecutorService executor;
    private static SensorMonitoringThread sensorTask;
    private static IrrigationThread irrigationTask;
    private static PlantGrowthThread plantTask;
    private static boolean isSimulating = false;

    @FXML
    public void initialize() {
        AppContext.getInstance().registerListener(this);
        initZones();
        updateUI();
    }
    
    private void initZones() {
        zonesPane.getChildren().clear();
        List<Zone> zones = AppContext.getInstance().getZoneService().getAllZones();
        for (Zone zone : zones) {
            VBox zoneCard = new VBox();
            zoneCard.getStyleClass().add("card");
            zoneCard.setSpacing(5);
            
            Label nameLbl = new Label(zone.getName());
            nameLbl.getStyleClass().add("card-title");
            
            Label descLbl = new Label(zone.getDescription());
            
            zoneCard.getChildren().addAll(nameLbl, descLbl);
            zonesPane.getChildren().add(zoneCard);
        }
    }

    @Override
    public void updateUI() {
        AppContext context = AppContext.getInstance();
        
        List<Plant> plants = context.getPlantService().getAllPlants();
        long healthyCount = plants.stream().filter(p -> p.getPlantStatus() == PlantStatus.HEALTHY).count();
        
        totalPlantsLabel.setText(String.valueOf(plants.size()));
        healthyPlantsLabel.setText(String.valueOf(healthyCount));
        
        int alertsCount = context.getAlertService().getActiveAlerts().size();
        activeAlertsLabel.setText(String.valueOf(alertsCount));

        GreenhouseState state = context.getState();
        tempLabel.setText(String.format("%.1f °C", state.getTemperature()));
        humidityLabel.setText(String.format("%.1f %%", state.getHumidity()));
        
        com.greenhouse.models.Resource water = context.getResourceService().getResource(com.greenhouse.models.ResourceType.WATER);
        if (water != null) {
            waterLabel.setText(String.format("%.1f L", water.getQuantity()));
        } else {
            waterLabel.setText("0.0 L");
        }

        if (isSimulating) {
            sensorStatus.setText("RUNNING"); sensorStatus.setStyle("-fx-text-fill: green;");
            irrigationStatus.setText("RUNNING"); irrigationStatus.setStyle("-fx-text-fill: green;");
            plantStatus.setText("RUNNING"); plantStatus.setStyle("-fx-text-fill: green;");
        } else {
            sensorStatus.setText("STOPPED"); sensorStatus.setStyle("-fx-text-fill: gray;");
            irrigationStatus.setText("STOPPED"); irrigationStatus.setStyle("-fx-text-fill: gray;");
            plantStatus.setText("STOPPED"); plantStatus.setStyle("-fx-text-fill: gray;");
        }
    }

    @FXML private void startSimulation() {
        if (isSimulating) return;
        isSimulating = true;
        
        executor = Executors.newFixedThreadPool(3);
        sensorTask = new SensorMonitoringThread();
        irrigationTask = new IrrigationThread();
        plantTask = new PlantGrowthThread();
        
        executor.submit(sensorTask);
        executor.submit(irrigationTask);
        executor.submit(plantTask);
        
        updateUI();
    }

    @FXML private void stopSimulation() {
        if (!isSimulating) return;
        isSimulating = false;
        
        if (sensorTask != null) sensorTask.stop();
        if (irrigationTask != null) irrigationTask.stop();
        if (plantTask != null) plantTask.stop();
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
        
        updateUI();
    }

    public static void shutdownSimulation() {
        isSimulating = false;
        if (sensorTask != null) sensorTask.stop();
        if (irrigationTask != null) irrigationTask.stop();
        if (plantTask != null) plantTask.stop();
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }
}
