package com.greenhouse.models;

/**
 * Simulates a light sensor reading lux values.
 * Demonstrates inheritance by extending Sensor.
 */
public class LightSensor extends Sensor {

    public LightSensor(int id, String name, double initialValue, int zoneId) {
        super(id, name, initialValue, zoneId);
    }

    @Override
    public double readValue() {
        // Light can fluctuate more significantly due to cloud cover (e.g., +/- 500 lux)
        double drift = (random.nextDouble() * 1000.0) - 500.0;
        value += drift;
        
        // Realistic light bounds (e.g., 0 to 100,000 lux)
        value = Math.max(0.0, Math.min(100000.0, value));
        
        return Math.round(value);
    }

    @Override
    public String getUnit() {
        return "lux";
    }
}
