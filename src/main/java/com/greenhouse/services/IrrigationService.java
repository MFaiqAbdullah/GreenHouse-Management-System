package com.greenhouse.services;

import com.greenhouse.models.Zone;

public class IrrigationService {
    private ZoneService zoneService;

    public IrrigationService(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    public void startIrrigation(int zoneId) {
        Zone zone = zoneService.getZoneById(zoneId);
        if (zone != null && zone.getIrrigationSystem() != null) {
            zone.getIrrigationSystem().start();
        } else {
            throw new IllegalArgumentException("Invalid zone or missing irrigation system.");
        }
    }

    public void stopIrrigation(int zoneId) {
        Zone zone = zoneService.getZoneById(zoneId);
        if (zone != null && zone.getIrrigationSystem() != null) {
            zone.getIrrigationSystem().stop();
        }
    }

    public boolean isIrrigating(int zoneId) {
        Zone zone = zoneService.getZoneById(zoneId);
        if (zone != null && zone.getIrrigationSystem() != null) {
            return zone.getIrrigationSystem().isRunning();
        }
        return false;
    }
}
