package com.greenhouse.models;

/**
 * Simulates a humidity sensor reading relative humidity as a percentage.
 * Demonstrates inheritance by extending Sensor.
 */
public class HumiditySensor extends Sensor {

    public HumiditySensor(int id, String name, double initialValue, int zoneId) {
        super(id, name, initialValue, zoneId);
    }

    @Override
    public double readValue() {
        // Drift by up to +/- 2.0 % for realistic fluctuation
        double drift = (random.nextDouble() * 4.0) - 2.0;
        value += drift;
        
        // Humidity is between 0% and 100%
        value = Math.max(0.0, Math.min(100.0, value));
        
        return Math.round(value * 10.0) / 10.0;
    }

    @Override
    public String getUnit() {
        return "%";
    }
}
