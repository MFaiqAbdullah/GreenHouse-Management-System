package com.greenhouse.controllers;

import com.greenhouse.models.Resource;
import com.greenhouse.models.ResourceType;
import com.greenhouse.models.Role;
import com.greenhouse.utils.AppContext;
import com.greenhouse.utils.Updatable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;

public class ResourcesController implements Updatable {
    @FXML private VBox resourceContainer;
    @FXML private VBox adminControls;
    @FXML private ComboBox<ResourceType> typeCombo;
    @FXML private TextField amountField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        AppContext.getInstance().registerListener(this);
        if (AppContext.getInstance().getAuthService().getCurrentUser().getRole() == Role.ADMIN) {
            adminControls.setVisible(true);
            adminControls.setManaged(true);
            typeCombo.getItems().setAll(ResourceType.values());
        }
        refreshResources();
    }

    private void refreshResources() {
        resourceContainer.getChildren().clear();
        List<Resource> resources = AppContext.getInstance().getResourceService().getAllResources();
        
        for (Resource res : resources) {
            VBox card = new VBox(5);
            card.getStyleClass().add("card");
            
            Label title = new Label(res.getType().name());
            title.getStyleClass().add("card-title");
            
            Label value = new Label(String.format("%.1f", res.getQuantity()));
            value.getStyleClass().add("card-value");
            
            ProgressBar bar = new ProgressBar();
            bar.setPrefWidth(300);
            double max = res.getType() == ResourceType.WATER ? 5000.0 : 1000.0;
            bar.setProgress(Math.min(1.0, res.getQuantity() / max));
            
            card.getChildren().addAll(title, value, bar);
            resourceContainer.getChildren().add(card);
        }
    }

    @FXML
    private void handleAddStock() {
        try {
            ResourceType type = typeCombo.getValue();
            if (type == null) throw new Exception("Please select a resource type.");
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) throw new Exception("Amount must be positive.");
            
            AppContext.getInstance().getResourceService().addResource(type, amount);
            errorLabel.setVisible(false);
            amountField.clear();
            refreshResources();
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid amount.");
            errorLabel.setVisible(true);
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    @Override
    public void updateUI() {
        refreshResources();
    }
}
