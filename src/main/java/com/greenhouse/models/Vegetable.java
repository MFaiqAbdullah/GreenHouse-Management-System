package com.greenhouse.models;

/**
 * Represents a vegetable crop.
 * Demonstrates inheritance by extending Plant.
 */
public class Vegetable extends Plant {

    public Vegetable(int id, String name, String species, double waterRequirement, 
                     double idealTemperature, double idealHumidity, int zoneId) {
        super(id, name, species, waterRequirement, idealTemperature, idealHumidity, zoneId);
    }

    @Override
    public void grow(double currentTemp, double currentHum, double currentMoisture) {
        double tempDiff = Math.abs(getIdealTemperature() - currentTemp);
        double humDiff = Math.abs(getIdealHumidity() - currentHum);
        double waterPenalty = currentMoisture < 40.0 ? (40.0 - currentMoisture) * 2 : 0;
        
        // Vegetable is sensitive to water
        double penalty = tempDiff + humDiff + waterPenalty;
        if (penalty < 25) {
            setGrowth(Math.min(100.0, getGrowth() + 2.0));
            setHealth(Math.min(100.0, getHealth() + 1.0));
        } else {
            setHealth(Math.max(0.0, getHealth() - (penalty / 10)));
        }
    }

    @Override
    public String getCareInstructions() {
        return "Ensure deep watering and harvest immediately when mature.";
    }
}
