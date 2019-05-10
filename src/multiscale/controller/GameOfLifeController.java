package multiscale.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
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
import multiscale.enums.InitialConditionEnum;
import multiscale.helpers.InitialConditionDictionary;
import multiscale.helpers.ValuesHelper;
import multiscale.model.Cell;
import multiscale.model.GridProperties;
import multiscale.model.Point;
import multiscale.service.GameOfLifeService;

import java.util.List;

public class GameOfLifeController {
    @FXML Button clearButton;
    @FXML Button drawGridButton;
    @FXML Button startButton;
    @FXML TableView tableView;
    @FXML ChoiceBox<String> initialConditionSelect;
    @FXML TextField gridWidth;
    @FXML TextField gridHeight;
    @FXML TextArea infoArea;

    private String initialCondition;
    private boolean drawButtonClicked = false;
    private GridProperties gridProperties;

    private final String activeClass = "active-cell";
    private final String inactiveClass = "inactive-cell";


    @FXML
    public void initialize() {
        initialConditionSelect.setItems(ValuesHelper.getGameOfLifeInitialConditionList());
    }

    public void startGame() {
        if (!drawButtonClicked || gridProperties == null) {
            return;
        }
        GameOfLifeService service = new GameOfLifeService(tableView, gridProperties);
        service.run();
    }

    public void drawGrid() {
        gridProperties = createGridProperties();
        if (drawButtonClicked || gridProperties == null) {
            return;
        }
        drawButtonClicked = true;
        prepareTableView(gridProperties);
        applyChosenInitialCondition(gridProperties);
        setTableViewData(gridProperties);
    }

    public void clearData() {

    }

    public void initialConditionInput(ActionEvent actionEvent) {
        initialCondition = initialConditionSelect.getSelectionModel().getSelectedItem();
        if (initialCondition.equals(InitialConditionEnum.CUSTOM.getName())) {
            tableView.setEditable(true);
        } else {
            tableView.setEditable(false);
        }
    }

    private void setTableViewData(GridProperties gridProperties) {
        ObservableList<ObservableList<Cell>> data = ValuesHelper.prepareDataList(gridProperties.getGrid());
        tableView.setItems(data);
    }

    private void applyChosenInitialCondition(GridProperties gridProperties) {
        if (initialCondition == null) {
            return;
        }
        gridProperties.setCondition(initialCondition);
        Cell[][] grid = gridProperties.getGrid();
        List<Point> conditionList = InitialConditionDictionary.getCondition(InitialConditionEnum.get(initialCondition), gridProperties);
        for(Point point : conditionList) {
            grid[point.y][point.x].setState(1);
        }
    }

    private void prepareTableView(GridProperties gridProperties) {
        final String [] cellStyles = {activeClass, inactiveClass};
        Cell[][] grid = gridProperties.getGrid();
        for (int i=0; i<gridProperties.getGridWidth(); ++i) {
            //TODO: BUILDER REQUIRED!
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
                        int rowIndex = cell.getIndex();
                        System.out.println("rowIndex " + rowIndex);
                        grid[rowIndex][cellIndex].changeState();
                        cell.getStyleClass().add(grid[rowIndex][cellIndex].getState() == 0 ? inactiveClass : activeClass);
                    }
                });
                return tableCell;
            });
            column.setSortable(Boolean.FALSE);
            //TODO: values in one place!!
            column.setMinWidth(30);
            column.setMaxWidth(30);
            tableView.getColumns().add(column);
        }
    }

    private GridProperties createGridProperties() {
        if (!checkRequiredInputs()) {
            return null;
        }
        //TODO: consider builder class for properties
        Integer height = Integer.valueOf(gridHeight.getText());
        Integer width = Integer.valueOf(gridWidth.getText());
        return new GridProperties(height, width);
    }

    private boolean checkRequiredInputs() {
        return gridHeight.getText() != null && gridWidth.getText() != null && initialCondition != null;
    }
}
