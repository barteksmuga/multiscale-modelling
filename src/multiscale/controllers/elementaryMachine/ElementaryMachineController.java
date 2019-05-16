package multiscale.controllers.elementaryMachine;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import multiscale.constants.Rules;
import multiscale.constants.enums.StateEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.elementaryMachine.ElementaryMachineService;
import multiscale.services.GridPaneService;

import java.util.Arrays;
import java.util.List;

public class ElementaryMachineController {

    @FXML Button wipeResultsButton;
    @FXML Button drawFirstRowButton;
    @FXML Button startSimulationButton;
    @FXML TextField stepCountField;
    @FXML TextField widthField;
    @FXML ChoiceBox<String> ruleChoiceBox;
    @FXML GridPane drawGridArea;

    private Grid grid;
    private GridPaneService gridPaneService;

    @FXML
    public void initialize() {
        ruleChoiceBox.setItems(Rules.getElementaryMachineRuleList());
        gridPaneService = new GridPaneService();
    }

    public void drawFirstRow(ActionEvent actionEvent) {
        int gridWidth = Integer.valueOf(widthField.getText());
        int gridHeight = Integer.valueOf(stepCountField.getText());
        grid = new Grid(gridWidth, gridHeight);
        var firstRowList = getFirstRow();
        drawGridArea.addRow(0, firstRowList.toArray(new Cell[0]));
        grid.setFirstRow(firstRowList);
    }

    public void start(ActionEvent actionEvent) {
        grid.setRuleIndicator(getRuleIndicator());
        ElementaryMachineService service = new ElementaryMachineService(grid, drawGridArea);
        service.run();
    }

    public void wipeData(ActionEvent actionEvent) {
        widthField.setText("");
        stepCountField.setText("");
        gridPaneService.wipeGridPaneData(drawGridArea);
        ruleChoiceBox.getSelectionModel().clearSelection();
    }

    private int getRuleIndicator() {
        String choice = ruleChoiceBox.getSelectionModel().getSelectedItem();
        return Integer.valueOf(choice);
    }

    private List<Cell> getFirstRow() {
        var list = Arrays.asList(grid.getGrid()[0]);
        list.get(4).setState(StateEnum.ACTIVE.getStateValue());
        list.get(14).setState(StateEnum.ACTIVE.getStateValue());
        list.get(24).setState(StateEnum.ACTIVE.getStateValue());
        list.get(34).setState(StateEnum.ACTIVE.getStateValue());
        return list;
    }

    public void handleMouseClicked(MouseEvent mouseEvent) {
        Node clicked = (Node) mouseEvent.getSource();

        if (clicked != drawGridArea) {
            Integer row = GridPane.getRowIndex(clicked);
            Integer column = GridPane.getColumnIndex(clicked);
            System.out.println("Row: " + row + " Column: " + column);
        }
    }
}
