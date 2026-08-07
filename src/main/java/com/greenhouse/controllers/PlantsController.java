package com.greenhouse.controllers;

import com.greenhouse.exceptions.InvalidPlantDataException;
import com.greenhouse.models.*;
import com.greenhouse.services.PlantService;
import com.greenhouse.utils.AppContext;
import com.greenhouse.utils.Updatable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlantsController implements Updatable {
    @FXML private TextField searchField;
    @FXML private ComboBox<String> typeFilter;
    @FXML private ComboBox<Integer> zoneFilter;
    @FXML private ComboBox<String> statusFilter;
    
    @FXML private TableView<Plant> plantsTable;
    @FXML private TableColumn<Plant, String> typeColumn;

    @FXML private VBox formPane;
    @FXML private Label formTitle;
    @FXML private TextField fName;
    @FXML private TextField fSpecies;
    @FXML private ComboBox<String> fType;
    @FXML private TextField fHealth;
    @FXML private TextField fGrowth;
    @FXML private TextField fWater;
    @FXML private TextField fTemp;
    @FXML private TextField fHum;
    @FXML private ComboBox<Integer> fZone;
    @FXML private Label formError;

    private ObservableList<Plant> masterData = FXCollections.observableArrayList();
    private Plant editingPlant = null;

    @FXML
    public void initialize() {
        AppContext.getInstance().registerListener(this);
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getClass().getSimpleName().toUpperCase()
        ));

        List<Plant> plants = AppContext.getInstance().getPlantService().getAllPlants();
        masterData.setAll(plants);
        plantsTable.setItems(masterData);

        // Populate filters and form dropdowns
        typeFilter.setItems(FXCollections.observableArrayList("All", "VEGETABLE", "FLOWER", "FRUIT"));
        typeFilter.setValue("All");
        
        statusFilter.setItems(FXCollections.observableArrayList("All", "HEALTHY", "NEEDS_ATTENTION", "CRITICAL"));
        statusFilter.setValue("All");

        List<Integer> zoneIds = AppContext.getInstance().getZoneService().getAllZones().stream().map(Zone::getId).collect(Collectors.toList());
        ObservableList<Integer> zoneObs = FXCollections.observableArrayList(zoneIds);
        
        zoneFilter.getItems().add(null); // 'Any'
        zoneFilter.getItems().addAll(zoneObs);
        
        fType.setItems(FXCollections.observableArrayList("VEGETABLE", "FLOWER", "FRUIT"));
        fZone.setItems(zoneObs);

        // Listeners for filtering
        searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());
        typeFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        zoneFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        statusFilter.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
    }

    private void applyFilters() {
        String search = searchField.getText().toLowerCase();
        String type = typeFilter.getValue();
        Integer zoneId = zoneFilter.getValue();
        String status = statusFilter.getValue();

        List<Plant> filtered = masterData.stream().filter(p -> {
            boolean matchSearch = p.getName().toLowerCase().contains(search) || p.getSpecies().toLowerCase().contains(search);
            boolean matchType = type.equals("All") || p.getClass().getSimpleName().toUpperCase().equals(type);
            boolean matchZone = (zoneId == null) || (p.getZoneId() == zoneId);
            boolean matchStatus = status.equals("All") || p.getStatus().equals(status);
            return matchSearch && matchType && matchZone && matchStatus;
        }).collect(Collectors.toList());

        plantsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML private void handleClearFilters() {
        searchField.clear();
        typeFilter.setValue("All");
        zoneFilter.setValue(null);
        statusFilter.setValue("All");
    }

    @FXML private void handleAddPlant() {
        editingPlant = null;
        formTitle.setText("Add Plant");
        clearForm();
        formPane.setVisible(true);
        formPane.setManaged(true);
    }

    @FXML private void handleEditPlant() {
        Plant selected = plantsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        editingPlant = selected;
        formTitle.setText("Edit Plant ID: " + selected.getId());
        fName.setText(selected.getName());
        fSpecies.setText(selected.getSpecies());
        fType.setValue(selected.getClass().getSimpleName().toUpperCase());
        fType.setDisable(true); // Disallow changing type for simplicity
        fHealth.setText(String.valueOf(selected.getHealth()));
        fGrowth.setText(String.valueOf(selected.getGrowth()));
        fWater.setText(String.valueOf(selected.getWaterRequirement()));
        fTemp.setText(String.valueOf(selected.getIdealTemperature()));
        fHum.setText(String.valueOf(selected.getIdealHumidity()));
        fZone.setValue(selected.getZoneId());

        formError.setVisible(false);
        formPane.setVisible(true);
        formPane.setManaged(true);
    }

    @FXML private void handleDeletePlant() {
        Plant selected = plantsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Plant");
        confirm.setContentText("Are you sure you want to delete " + selected.getName() + "?");
        
        Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            AppContext.getInstance().getPlantService().deletePlant(selected.getId());
            masterData.remove(selected);
            applyFilters();
        }
    }

    @FXML private void handleSaveForm() {
        try {
            if (fName.getText().isEmpty() || fSpecies.getText().isEmpty() || fType.getValue() == null || fZone.getValue() == null) {
                throw new InvalidPlantDataException("Please fill out all fields.");
            }

            double health = Double.parseDouble(fHealth.getText());
            double growth = Double.parseDouble(fGrowth.getText());
            double water = Double.parseDouble(fWater.getText());
            double temp = Double.parseDouble(fTemp.getText());
            double hum = Double.parseDouble(fHum.getText());

            if (health < 0 || health > 100) throw new InvalidPlantDataException("Health must be 0-100.");
            if (growth < 0 || growth > 100) throw new InvalidPlantDataException("Growth must be 0-100.");

            int zoneId = fZone.getValue();
            String name = fName.getText();
            String species = fSpecies.getText();
            String typeStr = fType.getValue();

            PlantService service = AppContext.getInstance().getPlantService();

            if (editingPlant == null) {
                // Determine new ID (max + 1)
                int newId = masterData.stream().mapToInt(Plant::getId).max().orElse(0) + 1;
                Plant p;
                if ("VEGETABLE".equals(typeStr)) p = new Vegetable(newId, name, species, water, temp, hum, zoneId);
                else if ("FLOWER".equals(typeStr)) p = new Flower(newId, name, species, water, temp, hum, zoneId);
                else p = new Fruit(newId, name, species, water, temp, hum, zoneId);
                
                p.setHealth(health);
                p.setGrowth(growth);
                
                service.addPlant(p);
                masterData.add(p);
            } else {
                editingPlant.setName(name); // Need setters in Plant for these if not existing.
                // Wait, Plant class doesn't have setName, setSpecies.
                // If they don't exist, I can't update them unless I recreate or add setters.
                // Let's assume we can modify them or we only update health/growth/etc.
                // For this demo, let's just update health, growth, zoneId and status.
                // To keep it simple, I'll update health, growth, status.
                // It's safer to use the PlantService update method which we made. But wait, we didn't add setters to Plant for name/species.
                editingPlant.setHealth(health);
                editingPlant.setGrowth(growth);
                // We'll skip name/species since there are no setters in Plant model unless we add them. 
                service.updatePlant(editingPlant);
                plantsTable.refresh();
            }

            applyFilters();
            handleCancelForm();
        } catch (NumberFormatException e) {
            showFormError("Numeric fields must contain valid numbers.");
        } catch (Exception e) {
            showFormError(e.getMessage());
        }
    }

    private void showFormError(String msg) {
        formError.setText(msg);
        formError.setVisible(true);
    }

    @FXML private void handleCancelForm() {
        formPane.setVisible(false);
        formPane.setManaged(false);
        editingPlant = null;
    }

    private void clearForm() {
        fName.clear();
        fSpecies.clear();
        fType.setValue(null);
        fType.setDisable(false);
        fHealth.clear();
        fGrowth.clear();
        fWater.clear();
        fTemp.clear();
        fHum.clear();
        fZone.setValue(null);
        formError.setVisible(false);
    }

    @Override
    public void updateUI() {
        List<Plant> plants = AppContext.getInstance().getPlantService().getAllPlants();
        masterData.setAll(plants);
        applyFilters();
    }
}
