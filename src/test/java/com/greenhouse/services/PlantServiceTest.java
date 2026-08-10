package com.greenhouse.services;

import com.greenhouse.models.Plant;
import com.greenhouse.models.Vegetable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlantServiceTest {

    private PlantService plantService;

    @BeforeEach
    public void setup() {
        plantService = new PlantService();
    }

    @Test
    public void testAddPlant_ValidData() {
        int initialSize = plantService.getAllPlants().size();
        
        // Use an ID we know is large enough to not conflict
        int newId = initialSize + 9999;
        Plant newPlant = new Vegetable(newId, "Test Tomato", "Solanum lycopersicum", 2.0, 22.0, 60.0, 1);
        
        plantService.addPlant(newPlant);
        
        List<Plant> plants = plantService.getAllPlants();
        assertEquals(initialSize + 1, plants.size());
        
        // Clean up
        plantService.deletePlant(newId);
    }
    
    @Test
    public void testUpdatePlant_UpdatesProperly() {
        List<Plant> plants = plantService.getAllPlants();
        if (plants.isEmpty()) return; // Skip if no plants
        
        Plant p = plants.get(0);
        double oldHealth = p.getHealth();
        p.setHealth(50.0); // Modify it
        
        plantService.updatePlant(p);
        
        // Fetch fresh list
        PlantService newServiceInstance = new PlantService();
        Plant fetched = newServiceInstance.getPlantById(p.getId());
        
        assertEquals(50.0, fetched.getHealth());
        
        // Revert
        p.setHealth(oldHealth);
        plantService.updatePlant(p);
    }
}
