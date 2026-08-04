package com.greenhouse.models;

/**
 * Represents the irrigation system for a specific zone.
 */
public class IrrigationSystem implements Controllable {
    private int zoneId;
    private boolean isRunning;
    private double waterUsed;

    public IrrigationSystem(int zoneId) {
        this.zoneId = zoneId;
        this.isRunning = false;
        this.waterUsed = 0.0;
    }

    public int getZoneId() {
        return zoneId;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public double getWaterUsed() {
        return waterUsed;
    }

    public void start() {
        if (!isRunning) {
            this.isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            this.isRunning = false;
        }
    }
    
    public void addWaterUsed(double amount) {
        if (amount > 0) {
            this.waterUsed += amount;
        }
    }
}
