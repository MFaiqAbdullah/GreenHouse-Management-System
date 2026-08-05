package com.greenhouse.services;

import com.greenhouse.filehandling.FileManager;
import com.greenhouse.models.Sensor;
import java.util.List;
import java.util.stream.Collectors;

public class SensorService {
    private List<Sensor> sensors;

    public SensorService() {
        this.sensors = FileManager.loadSensors();
    }

    public List<Sensor> getAllSensors() {
        return sensors;
    }

    public List<Sensor> getSensorsByZone(int zoneId) {
        return sensors.stream().filter(s -> s.getZoneId() == zoneId).collect(Collectors.toList());
    }

    public void addSensor(Sensor sensor) {
        if (sensors.stream().anyMatch(s -> s.getId() == sensor.getId())) {
            throw new IllegalArgumentException("Duplicate Sensor ID");
        }
        sensors.add(sensor);
        FileManager.saveSensors(sensors);
    }

    public void updateSensor(Sensor updatedSensor) {
        for (int i = 0; i < sensors.size(); i++) {
            if (sensors.get(i).getId() == updatedSensor.getId()) {
                sensors.set(i, updatedSensor);
                FileManager.saveSensors(sensors);
                return;
            }
        }
        throw new IllegalArgumentException("Sensor not found for update.");
    }

    public void deleteSensor(int id) {
        boolean removed = sensors.removeIf(s -> s.getId() == id);
        if (removed) {
            FileManager.saveSensors(sensors);
        } else {
            throw new IllegalArgumentException("Sensor not found for deletion.");
        }
    }
}
