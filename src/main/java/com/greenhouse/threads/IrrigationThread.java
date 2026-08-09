package com.greenhouse.threads;

import com.greenhouse.models.Sensor;
import com.greenhouse.models.Zone;
import com.greenhouse.models.ResourceType;
import com.greenhouse.utils.AppContext;

import java.util.List;
import java.util.stream.Collectors;

public class IrrigationThread implements Runnable {
    /* 
     * WHY IMPLEMENT RUNNABLE INSTEAD OF EXTENDING THREAD?
     * Better practice: Separates the task logic from thread lifecycle management.
     */
    
    private volatile boolean running = true;
    private static final double START_THRESHOLD = 40.0;
    private static final double STOP_THRESHOLD = 43.0;
    
    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1500); // Check irrigation every 1.5 seconds
                
                AppContext ctx = AppContext.getInstance();
                List<Zone> zones = ctx.getZoneService().getAllZones();
                
                for (Zone zone : zones) {
                    List<Sensor> zoneSensors = ctx.getSensorService().getSensorsByZone(zone.getId());
                    List<Sensor> soilSensors = zoneSensors.stream()
                        .filter(s -> s.getClass().getSimpleName().contains("Soil"))
                        .collect(Collectors.toList());
                    
                    if (soilSensors.isEmpty()) continue;
                    
                    double avgMoisture = soilSensors.stream().mapToDouble(Sensor::getRawValue).average().orElse(50.0);
                    boolean isIrrigating = ctx.getIrrigationService().isIrrigating(zone.getId());
                    
                    if (!isIrrigating && avgMoisture < START_THRESHOLD) {
                        ctx.getIrrigationService().startIrrigation(zone.getId());
                    } 
                    else if (isIrrigating && avgMoisture >= STOP_THRESHOLD) {
                        ctx.getIrrigationService().stopIrrigation(zone.getId());
                    }
                    
                    if (ctx.getIrrigationService().isIrrigating(zone.getId())) {
                        try {
                            ctx.getResourceService().consumeResource(ResourceType.WATER, 5.0);
                            for (Sensor s : soilSensors) {
                                s.setValue(s.getRawValue() + 2.0); // Boost moisture
                            }
                            ctx.getState().setSoilMoisture(ctx.getState().getSoilMoisture() + 2.0);
                        } catch (com.greenhouse.exceptions.InsufficientResourceException e) {
                            ctx.getIrrigationService().stopIrrigation(zone.getId());
                        }
                    }
                }
                
                ctx.notifyListeners();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                // Ignore exceptions to keep thread running
            }
        }
    }
}
