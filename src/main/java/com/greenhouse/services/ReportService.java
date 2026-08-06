package com.greenhouse.services;

import com.greenhouse.filehandling.FileManager;
import com.greenhouse.models.*;

import java.time.LocalDateTime;

public class ReportService {

    public String generateGreenhouseReport(ZoneService zs, PlantService ps, ResourceService rs, AlertService as) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GREENHOUSE REPORT ===\n");
        sb.append("Generated: ").append(LocalDateTime.now()).append("\n\n");
        
        sb.append("Total Zones: ").append(zs.getAllZones().size()).append("\n");
        sb.append("Total Plants: ").append(ps.getAllPlants().size()).append("\n");
        sb.append("Active Alerts: ").append(as.getActiveAlerts().size()).append("\n\n");
        
        sb.append("RESOURCES:\n");
        for (Resource r : rs.getAllResources()) {
            sb.append("- ").append(r.getType()).append(": ").append(r.getQuantity()).append(" ").append(r.getUnit()).append("\n");
        }
        return sb.toString();
    }

    public String generatePlantReport(Plant plant) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PLANT REPORT ===\n");
        sb.append("ID: ").append(plant.getId()).append("\n");
        sb.append("Name: ").append(plant.getName()).append("\n");
        sb.append("Species: ").append(plant.getSpecies()).append("\n");
        sb.append("Status: ").append(plant.getStatus()).append("\n");
        sb.append("Health: ").append(plant.getHealth()).append("%\n");
        sb.append("Growth: ").append(plant.getGrowth()).append("%\n");
        sb.append("Zone ID: ").append(plant.getZoneId()).append("\n");
        sb.append("Care: ").append(plant.getCareInstructions()).append("\n");
        return sb.toString();
    }

    public String generateResourceReport(ResourceService rs) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESOURCE REPORT ===\n");
        sb.append("Generated: ").append(LocalDateTime.now()).append("\n\n");
        for (Resource r : rs.getAllResources()) {
            sb.append(r.getType()).append(": ").append(r.getQuantity()).append(" ").append(r.getUnit()).append("\n");
        }
        return sb.toString();
    }

    public void saveReport(String reportContent, String filename) {
        FileManager.saveReport(reportContent, filename);
    }
}
