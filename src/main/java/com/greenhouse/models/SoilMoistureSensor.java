package com.greenhouse.models;

/**
 * Simulates a soil moisture sensor.
 * Demonstrates inheritance by extending Sensor.
 */
public class SoilMoistureSensor extends Sensor {

    public SoilMoistureSensor(int id, String name, double initialValue, int zoneId) {
        super(id, name, initialValue, zoneId);
    }

    @Override
    public double readValue() {
        // Soil moisture tends to slowly dry out (negative drift bias) unless watered.
        // For a simple realistic simulation, we drift it down slightly more often than up.
        double drift = (random.nextDouble() * 0.7) - 0.5; 
        value += drift;
        
        // Moisture is between 0% and 100%
        value = Math.max(0.0, Math.min(100.0, value));
        
        return Math.round(value * 10.0) / 10.0;
    }

    @Override
    public String getUnit() {
        return "%";
    }
}
