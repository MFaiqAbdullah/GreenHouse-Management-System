package com.greenhouse.models;

/**
 * Represents a flowering plant.
 * Demonstrates inheritance by extending Plant.
 */
public class Flower extends Plant {

    public Flower(int id, String name, String species, double waterRequirement, 
                  double idealTemperature, double idealHumidity, int zoneId) {
        super(id, name, species, waterRequirement, idealTemperature, idealHumidity, zoneId);
    }

    @Override
    public void grow(double currentTemp, double currentHum, double currentMoisture) {
        double tempDiff = Math.abs(getIdealTemperature() - currentTemp);
        double humDiff = Math.abs(getIdealHumidity() - currentHum);
        double waterPenalty = currentMoisture < 40.0 ? (40.0 - currentMoisture) * 1.5 : 0;
        
        // Flower is sensitive to temp
        double penalty = (tempDiff * 2) + humDiff + waterPenalty;
        if (penalty < 25) {
            setGrowth(Math.min(100.0, getGrowth() + 1.5));
            setHealth(Math.min(100.0, getHealth() + 1.0));
        } else {
            setHealth(Math.max(0.0, getHealth() - (penalty / 10)));
        }
    }

    @Override
    public String getCareInstructions() {
        return "Deadhead spent blooms regularly to encourage new growth.";
    }
}
