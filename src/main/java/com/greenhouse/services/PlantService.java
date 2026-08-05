package com.greenhouse.services;

import com.greenhouse.filehandling.FileManager;
import com.greenhouse.models.Plant;
import com.greenhouse.exceptions.InvalidPlantDataException;
import java.util.List;
import java.util.stream.Collectors;

public class PlantService {
    private List<Plant> plants;

    public PlantService() {
        this.plants = FileManager.loadPlants();
    }

    public List<Plant> getAllPlants() {
        return plants;
    }

    public Plant getPlantById(int id) {
        return plants.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public List<Plant> getPlantsByZone(int zoneId) {
        return plants.stream().filter(p -> p.getZoneId() == zoneId).collect(Collectors.toList());
    }

    public void addPlant(Plant plant) {
        validatePlant(plant);
        if (plants.stream().anyMatch(p -> p.getId() == plant.getId())) {
            throw new InvalidPlantDataException("Duplicate Plant ID: " + plant.getId());
        }
        plants.add(plant);
        FileManager.savePlants(plants);
    }

    public void updatePlant(Plant updatedPlant) {
        validatePlant(updatedPlant);
        for (int i = 0; i < plants.size(); i++) {
            if (plants.get(i).getId() == updatedPlant.getId()) {
                plants.set(i, updatedPlant);
                FileManager.savePlants(plants);
                return;
            }
        }
        throw new InvalidPlantDataException("Plant not found for update.");
    }

    public void deletePlant(int id) {
        boolean removed = plants.removeIf(p -> p.getId() == id);
        if (removed) {
            FileManager.savePlants(plants);
        } else {
            throw new InvalidPlantDataException("Plant not found for deletion.");
        }
    }

    private void validatePlant(Plant plant) {
        if (plant.getHealth() < 0.0 || plant.getHealth() > 100.0) {
            throw new InvalidPlantDataException("Health must be between 0 and 100.");
        }
        if (plant.getWaterRequirement() < 0) {
            throw new InvalidPlantDataException("Water requirement cannot be negative.");
        }
    }
}
