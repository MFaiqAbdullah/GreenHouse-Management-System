package com.greenhouse.filehandling;

import com.greenhouse.models.*;

import java.io.*;
import java.util.*;

/**
 * Handles all reading and writing of data to permanent CSV/TXT storage.
 * 
 * NOTE: For this academic project, passwords in users.csv are stored in plain text.
 * In a real production environment, passwords MUST be properly hashed and salted 
 * (e.g., using bcrypt) before being saved. This is a deliberate simplification for 
 * the course scope.
 */
public class FileManager {
    private static final String DATA_DIR = "data/";

    // --- USERS ---
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        Set<Integer> seenIds = new HashSet<>();
        File file = new File(DATA_DIR + "users.csv");
        if (!file.exists()) {
            System.out.println("Warning: users.csv missing. Returning empty list.");
            return users;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 4) throw new IllegalArgumentException("Malformed row");
                    int id = Integer.parseInt(parts[0].trim());
                    if (!seenIds.add(id)) {
                        System.out.println("Warning: Skipping duplicate User ID: " + id);
                        continue;
                    }
                    String username = parts[1].trim();
                    String password = parts[2].trim();
                    Role role = Role.valueOf(parts[3].trim().toUpperCase());

                    if (role == Role.ADMIN) users.add(new Admin(id, username, password));
                    else users.add(new Staff(id, username, password));
                } catch (Exception e) {
                    System.out.println("Error parsing User row: '" + line + "' -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users.csv: " + e.getMessage());
        }
        return users;
    }

    public static void saveUsers(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "users.csv"))) {
            bw.write("id,username,password,role\n");
            for (User u : users) {
                bw.write(u.getId() + "," + u.getUsername() + "," + u.getPassword() + "," + u.getRole() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving users.csv: " + e.getMessage());
        }
    }

    // --- ZONES ---
    public static List<Zone> loadZones() {
        List<Zone> zones = new ArrayList<>();
        Set<Integer> seenIds = new HashSet<>();
        File file = new File(DATA_DIR + "zones.csv");
        if (!file.exists()) {
            System.out.println("Warning: zones.csv missing.");
            return zones;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 3) throw new IllegalArgumentException("Malformed row");
                    int id = Integer.parseInt(parts[0].trim());
                    if (!seenIds.add(id)) {
                        System.out.println("Warning: Skipping duplicate Zone ID: " + id);
                        continue;
                    }
                    String name = parts[1].trim();
                    String desc = parts[2].trim();
                    zones.add(new Zone(id, name, desc));
                } catch (Exception e) {
                    System.out.println("Error parsing Zone row: '" + line + "' -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading zones.csv: " + e.getMessage());
        }
        return zones;
    }

    public static void saveZones(List<Zone> zones) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "zones.csv"))) {
            bw.write("id,name,description\n");
            for (Zone z : zones) {
                bw.write(z.getId() + "," + z.getName() + "," + z.getDescription() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving zones.csv: " + e.getMessage());
        }
    }

    // --- PLANTS ---
    public static List<Plant> loadPlants() {
        List<Plant> plants = new ArrayList<>();
        Set<Integer> seenIds = new HashSet<>();
        File file = new File(DATA_DIR + "plants.csv");
        if (!file.exists()) {
            System.out.println("Warning: plants.csv missing.");
            return plants;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 10) throw new IllegalArgumentException("Malformed row");
                    
                    int id = Integer.parseInt(parts[0].trim());
                    if (!seenIds.add(id)) {
                        System.out.println("Warning: Skipping duplicate Plant ID: " + id);
                        continue;
                    }
                    String name = parts[1].trim();
                    String species = parts[2].trim();
                    String type = parts[3].trim().toUpperCase();
                    double health = Double.parseDouble(parts[4].trim());
                    double growth = Double.parseDouble(parts[5].trim());
                    double waterReq = Double.parseDouble(parts[6].trim());
                    double idealTemp = Double.parseDouble(parts[7].trim());
                    double idealHum = Double.parseDouble(parts[8].trim());
                    int zoneId = Integer.parseInt(parts[9].trim());

                    Plant plant = null;
                    switch (type) {
                        case "VEGETABLE":
                            plant = new Vegetable(id, name, species, waterReq, idealTemp, idealHum, zoneId);
                            break;
                        case "FLOWER":
                            plant = new Flower(id, name, species, waterReq, idealTemp, idealHum, zoneId);
                            break;
                        case "FRUIT":
                            plant = new Fruit(id, name, species, waterReq, idealTemp, idealHum, zoneId);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown plant type: " + type);
                    }
                    
                    plant.setHealth(health);
                    plant.setGrowth(growth);
                    plants.add(plant);
                } catch (Exception e) {
                    System.out.println("Error parsing Plant row: '" + line + "' -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading plants.csv: " + e.getMessage());
        }
        return plants;
    }

    public static void savePlants(List<Plant> plants) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "plants.csv"))) {
            bw.write("id,name,species,type,health,growth,waterReq,idealTemp,idealHum,zoneId\n");
            for (Plant p : plants) {
                String type = "";
                if (p instanceof Vegetable) type = "VEGETABLE";
                else if (p instanceof Flower) type = "FLOWER";
                else if (p instanceof Fruit) type = "FRUIT";
                
                bw.write(String.format(Locale.US, "%d,%s,%s,%s,%.1f,%.1f,%.1f,%.1f,%.1f,%d\n",
                        p.getId(), p.getName(), p.getSpecies(), type, p.getHealth(), p.getGrowth(),
                        p.getWaterRequirement(), p.getIdealTemperature(), p.getIdealHumidity(), p.getZoneId()));
            }
        } catch (IOException e) {
            System.out.println("Error saving plants.csv: " + e.getMessage());
        }
    }

    // --- SENSORS ---
    public static List<Sensor> loadSensors() {
        List<Sensor> sensors = new ArrayList<>();
        Set<Integer> seenIds = new HashSet<>();
        File file = new File(DATA_DIR + "sensors.csv");
        if (!file.exists()) {
            System.out.println("Warning: sensors.csv missing.");
            return sensors;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 5) throw new IllegalArgumentException("Malformed row");
                    
                    int id = Integer.parseInt(parts[0].trim());
                    if (!seenIds.add(id)) {
                        System.out.println("Warning: Skipping duplicate Sensor ID: " + id);
                        continue;
                    }
                    String name = parts[1].trim();
                    String type = parts[2].trim().toUpperCase();
                    double initialValue = Double.parseDouble(parts[3].trim());
                    int zoneId = Integer.parseInt(parts[4].trim());

                    switch (type) {
                        case "TEMPERATURE":
                            sensors.add(new TemperatureSensor(id, name, initialValue, zoneId));
                            break;
                        case "HUMIDITY":
                            sensors.add(new HumiditySensor(id, name, initialValue, zoneId));
                            break;
                        case "SOIL_MOISTURE":
                            sensors.add(new SoilMoistureSensor(id, name, initialValue, zoneId));
                            break;
                        case "LIGHT":
                            sensors.add(new LightSensor(id, name, initialValue, zoneId));
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown sensor type: " + type);
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing Sensor row: '" + line + "' -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading sensors.csv: " + e.getMessage());
        }
        return sensors;
    }

    public static void saveSensors(List<Sensor> sensors) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "sensors.csv"))) {
            bw.write("id,name,type,initialValue,zoneId\n");
            for (Sensor s : sensors) {
                String type = "";
                if (s instanceof TemperatureSensor) type = "TEMPERATURE";
                else if (s instanceof HumiditySensor) type = "HUMIDITY";
                else if (s instanceof SoilMoistureSensor) type = "SOIL_MOISTURE";
                else if (s instanceof LightSensor) type = "LIGHT";
                
                bw.write(String.format(Locale.US, "%d,%s,%s,%.1f,%d\n",
                        s.getId(), s.getName(), type, s.readValue(), s.getZoneId()));
            }
        } catch (IOException e) {
            System.out.println("Error saving sensors.csv: " + e.getMessage());
        }
    }

    // --- RESOURCES ---
    public static List<Resource> loadResources() {
        List<Resource> resources = new ArrayList<>();
        File file = new File(DATA_DIR + "resources.csv");
        if (!file.exists()) return resources;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 3) throw new IllegalArgumentException("Malformed row");
                    
                    ResourceType type = ResourceType.valueOf(parts[0].trim().toUpperCase());
                    double quantity = Double.parseDouble(parts[1].trim());
                    String unit = parts[2].trim();
                    resources.add(new Resource(type, quantity, unit));
                } catch (Exception e) {
                    System.out.println("Error parsing Resource row: '" + line + "' -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading resources.csv: " + e.getMessage());
        }
        return resources;
    }

    public static void saveResources(List<Resource> resources) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "resources.csv"))) {
            bw.write("type,quantity,unit\n");
            for (Resource r : resources) {
                bw.write(String.format(Locale.US, "%s,%.1f,%s\n", r.getType(), r.getQuantity(), r.getUnit()));
            }
        } catch (IOException e) {
            System.out.println("Error saving resources.csv: " + e.getMessage());
        }
    }

    // --- ALERTS ---
    public static List<Alert> loadAlerts() {
        List<Alert> alerts = new ArrayList<>();
        Set<Integer> seenIds = new HashSet<>();
        File file = new File(DATA_DIR + "alerts.csv");
        if (!file.exists()) return alerts;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length < 5) throw new IllegalArgumentException("Malformed row");
                    
                    int id = Integer.parseInt(parts[0].trim());
                    if (!seenIds.add(id)) continue;
                    
                    String message = parts[1].trim();
                    AlertType type = AlertType.valueOf(parts[2].trim().toUpperCase());
                    AlertSeverity severity = AlertSeverity.valueOf(parts[3].trim().toUpperCase());
                    boolean resolved = Boolean.parseBoolean(parts[4].trim());

                    Alert alert = new Alert(id, message, type, severity);
                    if (resolved) alert.resolve();
                    alerts.add(alert);
                } catch (Exception e) {
                    System.out.println("Error parsing Alert row: '" + line + "' -> " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading alerts.csv: " + e.getMessage());
        }
        return alerts;
    }

    public static void saveAlerts(List<Alert> alerts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "alerts.csv"))) {
            bw.write("id,message,type,severity,resolved\n");
            for (Alert a : alerts) {
                bw.write(String.format(Locale.US, "%d,%s,%s,%s,%b\n",
                        a.getId(), a.getMessage(), a.getType(), a.getSeverity(), a.isResolved()));
            }
        } catch (IOException e) {
            System.out.println("Error saving alerts.csv: " + e.getMessage());
        }
    }

    // --- SETTINGS (TXT) ---
    public static Map<String, String> loadSettings() {
        Map<String, String> settings = new HashMap<>();
        File file = new File(DATA_DIR + "settings.txt");
        if (!file.exists()) return settings;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading settings.txt: " + e.getMessage());
        }
        return settings;
    }

    public static void saveSettings(Map<String, String> settings) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "settings.txt"))) {
            for (Map.Entry<String, String> entry : settings.entrySet()) {
                bw.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving settings.txt: " + e.getMessage());
        }
    }

    // --- REPORTS ---
    public static void saveReport(String content, String filename) {
        File reportsDir = new File(DATA_DIR + "reports/");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reportsDir.getPath() + "/" + filename))) {
            bw.write(content);
        } catch (IOException e) {
            System.out.println("Error saving report: " + e.getMessage());
        }
    }
}
