package com.greenhouse.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the entire greenhouse facility.
 * Demonstrates Aggregation by collecting Zones that can theoretically exist independently.
 */
public class Greenhouse {
    private String name;
    private List<Zone> zones;

    public Greenhouse(String name) {
        this.name = name;
        
        // Aggregation: The Greenhouse holds references to Zones, but a Zone is 
        // conceptually independent and passed in from the outside.
        this.zones = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void addZone(Zone zone) {
        if (zone != null && !zones.contains(zone)) {
            this.zones.add(zone);
        }
    }

    public void removeZone(Zone zone) {
        this.zones.remove(zone);
    }
}
