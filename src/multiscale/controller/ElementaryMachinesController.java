package multiscale.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import multiscale.helpers.RuleDictionary;
import multiscale.models.Cell;
import multiscale.models.GridProperties;
import multiscale.services.process.ElementaryMachineProcessService;
import multiscale.services.tableView.TableViewService;

public class ElementaryMachinesController {
    @FXML TableView<ObservableList<Cell>> tableView;
    @FXML Button startButton;
    @FXML Button drawFirstRowButton;
    @FXML ChoiceBox<String> ruleSelect;
    @FXML TextField gridHeight;
    @FXML TextField gridWidth;
    @FXML TextArea infoArea;

    private Integer chosenRule;
    private GridProperties gridProperties;
    private boolean isDrawButtonClicked = false;
    private TableViewService tableViewService;

    @FXML
    public void initialize () {
        tableViewService = new TableViewService(tableView);
        ruleSelect.setItems(RuleDictionary.getElementaryMachineRuleList());
        fillInfoArea();
    }

    public void ruleSelectInput(ActionEvent actionEvent) {
        String choice = ruleSelect.getSelectionModel().getSelectedItem();
        if (choice != null) {
            chosenRule = Integer.valueOf(choice);
        }
    }

    public void startSimulation(ActionEvent actionEvent) {
        if (chosenRule == null || !isDrawButtonClicked) {
            appendTextToInfoArea("\nNarysuj pierwszy wiersz i wybierz aktywne komórki oraz regułę.");
            return;
        }
        lockTableView();
        gridProperties.setRuleInd(chosenRule);
        ElementaryMachineProcessService service = new ElementaryMachineProcessService(tableViewService, gridProperties);
        appendTextToInfoArea("\nSymulacja rozpoczęta...");
        service.run();
        appendTextToInfoArea("...Symulacja zakończona");
    }

    private void lockTableView() {
        tableView.setEditable(false);
    }

    private void fillInfoArea() {
        infoArea.setText("Podaj liczbę kroków i kolumn.\nNastępnie narysuj pierwszy wiersz i wybierz aktywne komórki oraz regułę.\n");
    }

    private void appendTextToInfoArea(String text) {
        infoArea.appendText(text + "\n");
    }

    public void drawFirstRow(ActionEvent actionEvent) {
        gridProperties = createProcessRuleProperties();
        if (isDrawButtonClicked || gridProperties == null) {
            return;
        }
        isDrawButtonClicked = true;
        Cell[][] firstRow = prepareFirstRow();
        gridProperties.setFirstRow(firstRow);
        tableViewService.prepareTableView(gridProperties);
    }

    private Cell[][] prepareFirstRow() {
        Cell [][] firstRow = new Cell[1][gridProperties.getGridWidth()];
        for (int i=0; i<gridProperties.getGridWidth(); ++i) {
            firstRow[0][i] = new Cell();
        }
        return firstRow;
    }

    private GridProperties createProcessRuleProperties() {
        if (!checkRequiredFields()) {
            return null;
        }
        Integer height = Integer.valueOf(gridHeight.getText());
        Integer width = Integer.valueOf(gridWidth.getText());
        return new GridProperties(height, width);
    }

    private boolean checkRequiredFields() {
        return gridHeight.getText() != null && gridWidth.getText() != null;
    }

    public void clearData(ActionEvent actionEvent) {
        clearProperties();
        clearUiFields();
        unlockFlags();
        fillInfoArea();
    }

    private void clearUiFields() {
        gridWidth.setText("");
        gridHeight.setText("");
        tableView.setItems(FXCollections.observableArrayList());
        ObservableList<TableColumn<ObservableList<Cell>, ?>> tableColumns = tableView.getColumns();
        tableView.getColumns().removeAll(tableColumns);
        clearRuleSelection();
    }

    private void clearRuleSelection() {
        ruleSelect.getSelectionModel().clearSelection();
        chosenRule = null;
    }

    private void clearProperties() {
        if (gridProperties == null) {
            return;
        }
        gridProperties.setGridHeight(0);
        gridProperties.setGridWidth(0);
        gridProperties.setRuleInd(null);
        gridProperties.setGrid(new Cell[0][0]);
    }

    private void unlockFlags() {
        isDrawButtonClicked = false;
        tableView.setEditable(true);
    }
}
