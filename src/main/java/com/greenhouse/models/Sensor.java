package com.greenhouse.models;

import java.util.Random;

/**
 * Represents a generic environmental sensor in the greenhouse.
 * Demonstrates abstraction and encapsulation.
 */
public abstract class Sensor implements Monitorable {
    private int id;
    private String name;
    protected double value;
    protected Random random;
    private int zoneId;

    public Sensor(int id, String name, double initialValue, int zoneId) {
        this.id = id;
        this.name = name;
        this.value = initialValue;
        this.zoneId = zoneId;
        this.random = new Random();
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getRawValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getZoneId() {
        return zoneId;
    }

    /**
     * Abstract method to read the current value. Subclasses implement their own drift logic.
     * Demonstrates polymorphism when iterating through a list of generic Sensors.
     */
    public abstract double readValue();

    /**
     * Abstract method to get the unit of measurement (e.g., "°C", "%").
     */
    public abstract String getUnit();

    @Override
    public String getStatus() {
        return name + ": " + readValue() + " " + getUnit();
    }
}
