package com.greenhouse.models;

/**
 * Represents a fruit-bearing plant.
 * Demonstrates inheritance by extending Plant.
 */
public class Fruit extends Plant {

    public Fruit(int id, String name, String species, double waterRequirement, 
                 double idealTemperature, double idealHumidity, int zoneId) {
        super(id, name, species, waterRequirement, idealTemperature, idealHumidity, zoneId);
    }

    @Override
    public void grow(double currentTemp, double currentHum, double currentMoisture) {
        double tempDiff = Math.abs(getIdealTemperature() - currentTemp);
        double humDiff = Math.abs(getIdealHumidity() - currentHum);
        double waterPenalty = currentMoisture < 40.0 ? (40.0 - currentMoisture) : 0;
        
        // Fruit is hardier
        double penalty = (tempDiff + humDiff + waterPenalty) * 0.8;
        if (penalty < 30) {
            setGrowth(Math.min(100.0, getGrowth() + 1.0));
            setHealth(Math.min(100.0, getHealth() + 0.5));
        } else {
            setHealth(Math.max(0.0, getHealth() - (penalty / 15)));
        }
    }

    @Override
    public String getCareInstructions() {
        return "Prune annually and ensure adequate sunlight for proper fruit ripening.";
    }
}
