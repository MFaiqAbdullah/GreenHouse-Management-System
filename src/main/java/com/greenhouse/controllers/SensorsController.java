package com.greenhouse.controllers;

import com.greenhouse.models.Sensor;
import com.greenhouse.utils.AppContext;
import com.greenhouse.utils.Updatable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class SensorsController implements Updatable {
    @FXML private TableView<Sensor> sensorsTable;
    @FXML private TableColumn<Sensor, String> colId;
    @FXML private TableColumn<Sensor, String> colName;
    @FXML private TableColumn<Sensor, String> colType;
    @FXML private TableColumn<Sensor, String> colZone;
    @FXML private TableColumn<Sensor, String> colValue;

    @FXML
    public void initialize() {
        AppContext.getInstance().registerListener(this);
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClass().getSimpleName()));
        colZone.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getZoneId())));
        colValue.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f %s", data.getValue().getRawValue(), data.getValue().getUnit())));

        List<Sensor> sensors = AppContext.getInstance().getSensorService().getAllSensors();
        sensorsTable.setItems(FXCollections.observableArrayList(sensors));
    }

    @Override
    public void updateUI() {
        List<Sensor> sensors = AppContext.getInstance().getSensorService().getAllSensors();
        sensorsTable.setItems(FXCollections.observableArrayList(sensors));
    }
}
