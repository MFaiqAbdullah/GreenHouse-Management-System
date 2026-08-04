package com.greenhouse.models;

/**
 * Holds the shared, aggregated state of the greenhouse.
 * 
 * WHY SYNCHRONIZED?
 * This class is intentionally designed to be accessed concurrently by multiple 
 * background threads (SensorMonitoringThread, IrrigationThread, PlantGrowthThread) 
 * as well as the main JavaFX UI thread. By making every getter and setter `synchronized`, 
 * we guarantee that only one thread can read or modify a specific piece of state at any 
 * exact moment. This prevents "race conditions" (e.g., preventing two zones from 
 * simultaneously consuming water and causing the total water level to drop below zero 
 * due to overlapping calculations).
 */
public class GreenhouseState {
    private double temperature;
    private double humidity;
    private double soilMoisture;
    private double waterLevel;

    public GreenhouseState(double initialWaterLevel) {
        this.temperature = 25.0;
        this.humidity = 60.0;
        this.soilMoisture = 50.0;
        this.waterLevel = initialWaterLevel;
    }

    public synchronized double getTemperature() {
        return temperature;
    }

    public synchronized void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public synchronized double getHumidity() {
        return humidity;
    }

    public synchronized void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public synchronized double getSoilMoisture() {
        return soilMoisture;
    }

    public synchronized void setSoilMoisture(double soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public synchronized double getWaterLevel() {
        return waterLevel;
    }

    public synchronized void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }

    /**
     * Safely consumes water from the shared state.
     * @param amount the amount of water to use
     * @return true if successful, false if not enough water.
     */
    public synchronized boolean useWater(double amount) {
        if (this.waterLevel >= amount) {
            this.waterLevel -= amount;
            return true;
        }
        return false;
    }
}
