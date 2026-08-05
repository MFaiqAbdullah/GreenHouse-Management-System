package com.greenhouse.services;

import com.greenhouse.filehandling.FileManager;
import com.greenhouse.models.Resource;
import com.greenhouse.models.ResourceType;
import com.greenhouse.exceptions.InsufficientResourceException;
import java.util.List;

public class ResourceService {
    private List<Resource> resources;

    public ResourceService() {
        this.resources = FileManager.loadResources();
    }

    public synchronized List<Resource> getAllResources() {
        return resources;
    }

    public synchronized Resource getResource(ResourceType type) {
        return resources.stream().filter(r -> r.getType() == type).findFirst().orElse(null);
    }

    public synchronized void addResource(ResourceType type, double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        Resource r = getResource(type);
        if (r != null) {
            r.addQuantity(amount);
            FileManager.saveResources(resources);
        } else {
            // Create new resource entry if it doesn't exist
            String unit = (type == ResourceType.WATER) ? "Liters" : (type == ResourceType.FERTILIZER ? "Kg" : "Packs");
            resources.add(new Resource(type, amount, unit));
            FileManager.saveResources(resources);
        }
    }

    public synchronized void consumeResource(ResourceType type, double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        Resource r = getResource(type);
        if (r == null || !r.consumeQuantity(amount)) {
            throw new InsufficientResourceException("Not enough " + type + " to consume " + amount);
        }
        FileManager.saveResources(resources);
    }
}
