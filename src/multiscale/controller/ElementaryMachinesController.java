package multiscale.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import multiscale.helpers.ValuesHelper;
import multiscale.model.Cell;
import multiscale.model.ElementaryMachineProperties;
import multiscale.service.ElementaryMachineService;

public class ElementaryMachinesController {
    @FXML TableView tableView;
    @FXML Button startButton;
    @FXML Button drawFirstRowButton;
    @FXML ChoiceBox<String> ruleSelect;
    @FXML TextField gridHeight;
    @FXML TextField gridWidth;
    @FXML TextArea infoArea;

    private Integer chosenRule;
    private ElementaryMachineProperties properties;
    private boolean isDrawButtonClicked = false;

    private final String activeClass = "active-cell";
    private final String inactiveClass = "inactive-cell";

    @FXML
    public void initialize () {
        ruleSelect.setItems(ValuesHelper.getChoiceBoxOptions());
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
        properties.setRuleInd(chosenRule);
        ElementaryMachineService service = new ElementaryMachineService(properties, tableView);
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
        final String [] cellStyles = {activeClass, inactiveClass};
        properties = createProcessRuleProperties();
        if (isDrawButtonClicked || properties == null) {
            return;
        }
        isDrawButtonClicked = true;
        Cell [][] firstRow = new Cell[1][properties.getGridWidth()];
        for (int i=0; i<properties.getGridWidth(); ++i) {
            final int columnIndex = i;
            final TableColumn<ObservableList<Cell>, Integer> column = new TableColumn<>(String.valueOf(columnIndex + 1));
            column.setCellValueFactory(cellValue -> new ReadOnlyObjectWrapper<>(cellValue.getValue().get(columnIndex).getState()));
            column.setCellFactory(cellFactory -> {
                TableCell<ObservableList<Cell>, Integer> tableCell = new TableCell<ObservableList<Cell>, Integer>() {
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

                };
                tableCell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (tableView.isEditable()) {
                        TableCell cell = (TableCell) event.getSource();
                        cell.getStyleClass().removeAll(cellStyles);
                        int cellIndex = (int) cell.queryAccessibleAttribute(AccessibleAttribute.COLUMN_INDEX);
                        firstRow[0][cellIndex].changeState();
                        cell.getStyleClass().add(firstRow[0][cellIndex].getState() == 0 ? inactiveClass : activeClass);
                    }
                });
                return tableCell;
            });
            column.setSortable(Boolean.FALSE);
            column.setMinWidth(30);
            column.setMaxWidth(30);
            tableView.getColumns().add(column);
            firstRow[0][columnIndex] = new Cell();
        }
        ObservableList<ObservableList<Cell>> data = FXCollections.observableArrayList();
        for (Cell [] row : firstRow) {
            data.add(FXCollections.observableArrayList(row));
        }
        tableView.setItems(data);
        properties.setFirstRow(firstRow);
    }

    private ElementaryMachineProperties createProcessRuleProperties() {
        if (!checkRequiredFields()) {
            return null;
        }
        Integer height = Integer.valueOf(gridHeight.getText());
        Integer width = Integer.valueOf(gridWidth.getText());
        return new ElementaryMachineProperties(height, width);
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
        ObservableList<TableColumn> tableColumns = tableView.getColumns();
        tableView.getColumns().removeAll(tableColumns);
        clearRuleSelection();
    }

    private void clearRuleSelection() {
        ruleSelect.getSelectionModel().clearSelection();
        chosenRule = null;
    }

    private void clearProperties() {
        if (properties == null) {
            return;
        }
        properties.setGridHeight(0);
        properties.setGridWidth(0);
        properties.setRuleInd(null);
        properties.setGrid(new Cell[0][0]);
    }

    private void unlockFlags() {
        isDrawButtonClicked = false;
        tableView.setEditable(true);
    }
}
