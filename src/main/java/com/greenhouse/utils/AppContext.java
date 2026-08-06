package com.greenhouse.utils;

import com.greenhouse.services.*;
import com.greenhouse.models.GreenhouseState;
import com.greenhouse.filehandling.FileManager;
import com.greenhouse.models.Resource;
import com.greenhouse.models.ResourceType;

public class AppContext {
    private static AppContext instance = new AppContext();

    private AuthenticationService authService;
    private PlantService plantService;
    private ZoneService zoneService;
    private SensorService sensorService;
    private IrrigationService irrigationService;
    private ResourceService resourceService;
    private AlertService alertService;
    private ReportService reportService;
    private GreenhouseState state;
    
    private java.util.List<Updatable> uiListeners = new java.util.ArrayList<>();

    private AppContext() {
        authService = new AuthenticationService();
        plantService = new PlantService();
        zoneService = new ZoneService();
        sensorService = new SensorService();
        irrigationService = new IrrigationService(zoneService);
        resourceService = new ResourceService();
        alertService = new AlertService();
        reportService = new ReportService();

        // Initialize state with actual water level from ResourceService if available
        Resource water = resourceService.getResource(ResourceType.WATER);
        double waterLevel = (water != null) ? water.getQuantity() : 0.0;
        state = new GreenhouseState(waterLevel);
    }

    public static AppContext getInstance() { return instance; }

    public AuthenticationService getAuthService() { return authService; }
    public PlantService getPlantService() { return plantService; }
    public ZoneService getZoneService() { return zoneService; }
    public SensorService getSensorService() { return sensorService; }
    public IrrigationService getIrrigationService() { return irrigationService; }
    public ResourceService getResourceService() { return resourceService; }
    public AlertService getAlertService() { return alertService; }
    public ReportService getReportService() { return reportService; }
    public GreenhouseState getState() { return state; }
    
    public void registerListener(Updatable u) {
        if (!uiListeners.contains(u)) uiListeners.add(u);
    }
    
    public void unregisterListener(Updatable u) {
        uiListeners.remove(u);
    }
    
    public void notifyListeners() {
        javafx.application.Platform.runLater(() -> {
            for (Updatable u : uiListeners) {
                u.updateUI();
            }
        });
    }
}
