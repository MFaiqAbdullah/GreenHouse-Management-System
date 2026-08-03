package com.greenhouse.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a distinct area within the greenhouse.
 * Demonstrates Composition by exclusively owning its Sensors, Plants, and IrrigationSystem.
 */
public class Zone implements Monitorable {
    private int id;
    private String name;
    private String description;
    
    private List<Plant> plants;
    private List<Sensor> sensors;
    private IrrigationSystem irrigationSystem;

    public Zone(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        
        // Composition: The Zone initializes and manages the lifecycle of these internal components.
        // If the Zone is destroyed, these are destroyed.
        this.plants = new ArrayList<>();
        this.sensors = new ArrayList<>();
        this.irrigationSystem = new IrrigationSystem(id);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    
    public List<Plant> getPlants() { return plants; }
    public List<Sensor> getSensors() { return sensors; }
    public IrrigationSystem getIrrigationSystem() { return irrigationSystem; }

    public void addPlant(Plant plant) {
        if (plant != null) {
            this.plants.add(plant);
        }
    }

    public void addSensor(Sensor sensor) {
        if (sensor != null) {
            this.sensors.add(sensor);
        }
    }

    @Override
    public String getStatus() {
        return "Zone: " + name + " (Plants: " + plants.size() + ", Sensors: " + sensors.size() + ")";
    }
}
