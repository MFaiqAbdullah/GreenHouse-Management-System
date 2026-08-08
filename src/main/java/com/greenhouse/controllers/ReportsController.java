package com.greenhouse.controllers;

import com.greenhouse.services.ReportService;
import com.greenhouse.utils.AppContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.greenhouse.models.Plant;

public class ReportsController {

    @FXML private TextArea reportPreview;
    @FXML private TextField filenameField;
    @FXML private Label statusLabel;
    
    private String currentReportContent = "";

    @FXML
    private void genGreenhouse() {
        AppContext ctx = AppContext.getInstance();
        currentReportContent = ctx.getReportService().generateGreenhouseReport(ctx.getZoneService(), ctx.getPlantService(), ctx.getResourceService(), ctx.getAlertService());
        reportPreview.setText(currentReportContent);
        filenameField.setText("greenhouse_report.txt");
        statusLabel.setText("");
    }

    @FXML
    private void genResources() {
        AppContext ctx = AppContext.getInstance();
        currentReportContent = ctx.getReportService().generateResourceReport(ctx.getResourceService());
        reportPreview.setText(currentReportContent);
        filenameField.setText("resource_report.txt");
        statusLabel.setText("");
    }

    @FXML
    private void genPlants() {
        AppContext ctx = AppContext.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL PLANTS REPORT ===\n\n");
        for (Plant p : ctx.getPlantService().getAllPlants()) {
            sb.append(ctx.getReportService().generatePlantReport(p)).append("\n------------------\n");
        }
        currentReportContent = sb.toString();
        reportPreview.setText(currentReportContent);
        filenameField.setText("plants_report.txt");
        statusLabel.setText("");
    }

    @FXML
    private void saveReport() {
        if (currentReportContent.isEmpty()) {
            statusLabel.setText("No report generated.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        String fname = filenameField.getText();
        if (fname.isEmpty()) {
            statusLabel.setText("Please enter a filename.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        try {
            AppContext.getInstance().getReportService().saveReport(currentReportContent, fname);
            statusLabel.setText("Saved successfully to data/reports/" + fname);
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (Exception e) {
            statusLabel.setText("Error saving: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
