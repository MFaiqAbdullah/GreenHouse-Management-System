package com.greenhouse.threads;

import com.greenhouse.models.Plant;
import com.greenhouse.models.PlantStatus;
import com.greenhouse.models.Alert;
import com.greenhouse.models.AlertSeverity;
import com.greenhouse.models.AlertType;
import com.greenhouse.utils.AppContext;

import java.util.List;

public class PlantGrowthThread implements Runnable {
    /* 
     * WHY IMPLEMENT RUNNABLE INSTEAD OF EXTENDING THREAD?
     * Better practice: Separates the task logic from thread lifecycle management.
     */
    
    private volatile boolean running = true;
    
    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(3000); // Check plant growth every 3 seconds
                
                AppContext ctx = AppContext.getInstance();
                List<Plant> plants = ctx.getPlantService().getAllPlants();
                
                for (Plant p : plants) {
                    // Let the polymorphic method calculate growth/health
                    p.grow(ctx.getState().getTemperature(), ctx.getState().getHumidity(), ctx.getState().getSoilMoisture());
                    
                    // Check if it crossed into CRITICAL
                    if (p.getPlantStatus() == PlantStatus.CRITICAL) {
                        String msg = "Plant " + p.getName() + " is in CRITICAL condition!";
                        boolean alreadyAlerted = ctx.getAlertService().getActiveAlerts().stream()
                                .anyMatch(a -> a.getMessage().equals(msg));
                        if (!alreadyAlerted) {
                            int alertId = ctx.getAlertService().getAllAlerts().size() + 1;
                            Alert alert = new Alert(alertId, msg, AlertType.PLANT_HEALTH, AlertSeverity.CRITICAL);
                            ctx.getAlertService().addAlert(alert);
                        }
                    }
                    
                    // Update the plant in the service so it saves to CSV
                    ctx.getPlantService().updatePlant(p);
                }
                
                ctx.notifyListeners();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                // Ignore exceptions
            }
        }
    }
}
