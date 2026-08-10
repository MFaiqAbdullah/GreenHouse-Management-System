package com.greenhouse;

import com.greenhouse.filehandling.FileManager;
import com.greenhouse.models.Plant;
import com.greenhouse.models.Vegetable;

import java.util.List;

/**
 * A standalone test class to prove the persistence requirements 
 * set by the course instructor.
 */
public class PersistenceTest {
    public static void main(String[] args) {
        System.out.println("--- Starting Persistence Test ---");
        
        // 1. Load all data
        List<Plant> plants = FileManager.loadPlants();
        System.out.println("Initial Plants loaded: " + plants.size());
        
        // 2. Add one new plant
        int newId = plants.stream().mapToInt(Plant::getId).max().orElse(0) + 1;
        Plant newPlant = new Vegetable(newId, "Persistence Potato", "Potato", 2.0, 15.0, 50.0, 1);
        plants.add(newPlant);
        System.out.println("Added new plant: " + newPlant.getName() + " with ID " + newId);
        
        // 3. Save
        FileManager.savePlants(plants);
        System.out.println("Saved plants to CSV.");
        
        // 4. Reload and confirm
        List<Plant> reloadedPlants = FileManager.loadPlants();
        System.out.println("Reloaded Plants: " + reloadedPlants.size());
        
        boolean found = reloadedPlants.stream().anyMatch(p -> p.getId() == newId && p.getName().equals("Persistence Potato"));
        
        if (found) {
            System.out.println("\nSUCCESS: Persistence proof completed. The new plant was saved and successfully reloaded!");
        } else {
            System.out.println("\nFAILURE: The new plant was not found after reloading.");
        }
    }
}
