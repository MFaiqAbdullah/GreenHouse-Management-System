package com.greenhouse.threads;

import com.greenhouse.models.Sensor;
import com.greenhouse.models.GreenhouseState;
import com.greenhouse.services.SensorService;
import com.greenhouse.utils.AppContext;

import java.util.List;

public class SensorMonitoringThread implements Runnable {
    /* 
     * WHY IMPLEMENT RUNNABLE INSTEAD OF EXTENDING THREAD?
     * Implementing Runnable separates the task (the work to be done) from the 
     * thread management (how it is executed). This allows us to use an ExecutorService 
     * (thread pool) to manage execution, improving performance, resource management, 
     * and flexibility, which is a Java best practice.
     */
    
    private volatile boolean running = true;
    
    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(2000);
                
                AppContext ctx = AppContext.getInstance();
                SensorService sensorService = ctx.getSensorService();
                GreenhouseState state = ctx.getState();
                
                List<Sensor> allSensors = sensorService.getAllSensors();
                
                double tempSum = 0; int tempCount = 0;
                double humSum = 0; int humCount = 0;
                double soilSum = 0; int soilCount = 0;
                
                for (Sensor s : allSensors) {
                    double val = s.readValue(); // Triggers drift
                    String type = s.getClass().getSimpleName();
                    if (type.contains("Temperature")) { tempSum += val; tempCount++; }
                    else if (type.contains("Humidity")) { humSum += val; humCount++; }
                    else if (type.contains("Soil")) { soilSum += val; soilCount++; }
                }
                
                if (tempCount > 0) state.setTemperature(tempSum / tempCount);
                if (humCount > 0) state.setHumidity(humSum / humCount);
                if (soilCount > 0) state.setSoilMoisture(soilSum / soilCount);
                
                // Notify UI to update
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
