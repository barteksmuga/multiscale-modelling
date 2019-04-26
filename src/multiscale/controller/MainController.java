package multiscale.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import multiscale.helpers.ValuesHelper;
import multiscale.model.Cell;
import multiscale.model.ProcessRuleProperties;
import multiscale.service.ProcessRuleService;

import java.awt.image.BufferedImage;

public class MainController {
    @FXML TableView tableView;
    @FXML Button startButton;
    @FXML Button drawFirstRowButton;
    @FXML ChoiceBox<String> ruleSelect;
    @FXML TextField gridHeight;
    @FXML TextField gridWidth;

    private GraphicsContext graphicsContext;
    private Integer chosenRule;
    private ProcessRuleProperties properties;
    private boolean isDrawButtonClicked = false;

    public void ruleSelectInput(ActionEvent actionEvent) {
        String choice = ruleSelect.getSelectionModel().getSelectedItem();
        chosenRule = Integer.valueOf(choice);
    }

    public void startSimulation(ActionEvent actionEvent) {
        if (chosenRule == null || !isDrawButtonClicked) {
            return;
        }
        properties.setRuleInd(chosenRule);
        ProcessRuleService service = new ProcessRuleService(properties, tableView);
        service.run();
    }

    public void drawFirstRow(ActionEvent actionEvent) {
        if (isDrawButtonClicked) {
            return;
        }
        properties = createProcessRuleProperties();
        for (int i=0; i<properties.getGridWidth(); ++i) {
            TableColumn<Integer, Cell> column = new TableColumn<>(String.valueOf(i+1));
            column.setCellValueFactory(new PropertyValueFactory<>("id"));
            tableView.getColumns().add(column);
        }
    }

    private ProcessRuleProperties createProcessRuleProperties() {
        if (!checkRequiredFields()) {
            return null;
        }
        isDrawButtonClicked = true;
        Integer height = Integer.valueOf(gridHeight.getText());
        Integer width = Integer.valueOf(gridWidth.getText());
        return new ProcessRuleProperties(height, width);
    }

    private boolean checkRequiredFields() {
        return gridHeight.getText() != null && gridWidth.getText() != null;
    }

    @FXML
    public void initialize () {
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        ruleSelect.setItems(ValuesHelper.getChoiceBoxOptions());
    }
}
