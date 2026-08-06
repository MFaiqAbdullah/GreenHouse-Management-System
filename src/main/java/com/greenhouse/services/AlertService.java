package com.greenhouse.services;

import com.greenhouse.filehandling.FileManager;
import com.greenhouse.models.Alert;
import java.util.List;
import java.util.stream.Collectors;

public class AlertService {
    private List<Alert> alerts;

    public AlertService() {
        this.alerts = FileManager.loadAlerts();
    }

    public List<Alert> getAllAlerts() {
        return alerts;
    }

    public List<Alert> getActiveAlerts() {
        return alerts.stream().filter(a -> !a.isResolved()).collect(Collectors.toList());
    }

    public void addAlert(Alert alert) {
        alerts.add(alert);
        FileManager.saveAlerts(alerts);
    }

    public void resolveAlert(int id) {
        alerts.stream().filter(a -> a.getId() == id).findFirst().ifPresent(Alert::resolve);
        FileManager.saveAlerts(alerts);
    }
}
