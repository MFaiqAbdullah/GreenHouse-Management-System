package com.greenhouse.models;

import java.time.LocalDate;

/**
 * Represents a generic plant in the greenhouse.
 * This abstract class demonstrates abstraction and encapsulation.
 */
public abstract class Plant implements Monitorable {
    private int id;
    private String name;
    private String species;
    private double health;
    private double growth;
    private double waterRequirement;
    private double idealTemperature;
    private double idealHumidity;
    private int zoneId;
    private LocalDate datePlanted;
    private PlantStatus status;

    public Plant(int id, String name, String species, double waterRequirement, 
                 double idealTemperature, double idealHumidity, int zoneId) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.health = 100.0; // Starts fully healthy
        this.growth = 0.0;   // Starts at 0% growth
        this.waterRequirement = waterRequirement;
        this.idealTemperature = idealTemperature;
        this.idealHumidity = idealHumidity;
        this.zoneId = zoneId;
        this.datePlanted = LocalDate.now();
        this.status = PlantStatus.HEALTHY;
    }

    // Getters and setters (encapsulation)
    public int getId() { return id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSpecies() { return species; }
    
    public double getHealth() { return health; }
    public void setHealth(double health) { 
        this.health = health; 
        updateStatusBasedOnHealth();
    }
    
    public double getGrowth() { return growth; }
    public void setGrowth(double growth) { this.growth = growth; }
    
    public double getWaterRequirement() { return waterRequirement; }
    public double getIdealTemperature() { return idealTemperature; }
    public double getIdealHumidity() { return idealHumidity; }
    
    public int getZoneId() { return zoneId; }
    public void setZoneId(int zoneId) { this.zoneId = zoneId; }
    
    public LocalDate getDatePlanted() { return datePlanted; }
    
    public PlantStatus getPlantStatus() { return status; }
    
    @Override
    public String getStatus() { return status.toString(); }

    public void setStatus(PlantStatus status) { this.status = status; }

    private void updateStatusBasedOnHealth() {
        if (this.health >= 80) this.status = PlantStatus.HEALTHY;
        else if (this.health >= 40) this.status = PlantStatus.NEEDS_ATTENTION;
        else this.status = PlantStatus.CRITICAL;
    }

    /**
     * Abstract method defining how the plant grows over time.
     * Demonstrates polymorphism when called on different subclasses.
     */
    public abstract void grow(double currentTemp, double currentHum, double currentMoisture);

    /**
     * Abstract method returning specific care instructions based on the plant type.
     */
    public abstract String getCareInstructions();
}
