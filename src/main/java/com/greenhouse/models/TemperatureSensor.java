package com.greenhouse.models;

/**
 * Simulates a temperature sensor reading values in Celsius.
 * Demonstrates inheritance by extending Sensor.
 */
public class TemperatureSensor extends Sensor {

    public TemperatureSensor(int id, String name, double initialValue, int zoneId) {
        super(id, name, initialValue, zoneId);
    }

    @Override
    public double readValue() {
        // Drift by up to +/- 0.3 degrees for realistic fluctuation
        double drift = (random.nextDouble() * 0.6) - 0.3;
        value += drift;
        
        // Keep within realistic bounds (e.g., 10°C to 40°C)
        value = Math.max(10.0, Math.min(40.0, value));
        
        return Math.round(value * 10.0) / 10.0; // Round to 1 decimal place
    }

    @Override
    public String getUnit() {
        return "°C";
    }
}
