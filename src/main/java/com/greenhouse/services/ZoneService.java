package com.greenhouse.services;

import com.greenhouse.filehandling.FileManager;
import com.greenhouse.models.Zone;
import com.greenhouse.exceptions.InvalidZoneDataException;
import java.util.List;

public class ZoneService {
    private List<Zone> zones;

    public ZoneService() {
        this.zones = FileManager.loadZones();
    }

    public List<Zone> getAllZones() {
        return zones;
    }

    public Zone getZoneById(int id) {
        return zones.stream().filter(z -> z.getId() == id).findFirst().orElse(null);
    }

    public void addZone(Zone zone) {
        if (zones.stream().anyMatch(z -> z.getId() == zone.getId())) {
            throw new InvalidZoneDataException("Duplicate Zone ID: " + zone.getId());
        }
        zones.add(zone);
        FileManager.saveZones(zones);
    }

    public void updateZone(Zone updatedZone) {
        for (int i = 0; i < zones.size(); i++) {
            if (zones.get(i).getId() == updatedZone.getId()) {
                zones.set(i, updatedZone);
                FileManager.saveZones(zones);
                return;
            }
        }
        throw new InvalidZoneDataException("Zone not found for update.");
    }

    public void deleteZone(int id) {
        boolean removed = zones.removeIf(z -> z.getId() == id);
        if (removed) {
            FileManager.saveZones(zones);
        } else {
            throw new InvalidZoneDataException("Zone not found for deletion.");
        }
    }
}
