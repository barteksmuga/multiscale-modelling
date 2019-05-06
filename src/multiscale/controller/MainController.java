package multiscale.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
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

    private final String activeClass = "active-cell";
    private final String inactiveClass = "inactive-cell";


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
        final String [] cellStyles = {activeClass, inactiveClass};
        properties = createProcessRuleProperties();
        if (isDrawButtonClicked || properties == null) {
            return;
        }
        isDrawButtonClicked = true;
        for (int i=0; i<properties.getGridWidth(); ++i) {
            final int columnIndex = i;
            final TableColumn<ObservableList<Cell>, Integer> column = new TableColumn<>(String.valueOf(columnIndex + 1));
            column.setCellValueFactory(cellValue -> new ReadOnlyObjectWrapper<>(cellValue.getValue().get(columnIndex).getState()));
            column.setCellFactory(cellFactory -> new TableCell<ObservableList<Cell>, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(" ");
                    getStyleClass().removeAll(cellStyles);
                    if (item != null && item == 1) {
                        getStyleClass().add(activeClass);
                    } else if (!empty){
                        getStyleClass().add(inactiveClass);
                    }
                }
            });
            tableView.getColumns().add(column);
        }
    }

    private ProcessRuleProperties createProcessRuleProperties() {
        if (!checkRequiredFields()) {
            return null;
        }
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
