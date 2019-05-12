package multiscale.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import multiscale.enums.InitialConditionEnum;
import multiscale.helpers.InitialConditionDictionary;
import multiscale.models.Cell;
import multiscale.models.GridProperties;
import multiscale.models.Point;
import multiscale.services.process.GameOfLifeProcessService;
import multiscale.services.tableView.TableViewService;

import java.util.List;

public class GameOfLifeController {
    @FXML Button clearButton;
    @FXML Button drawGridButton;
    @FXML Button startButton;
    @FXML TableView<ObservableList<Cell>> tableView;
    @FXML ChoiceBox<String> initialConditionSelect;
    @FXML TextField gridWidth;
    @FXML TextField gridHeight;
    @FXML TextArea infoArea;

    private String initialCondition;
    private boolean drawButtonClicked = false;
    private GridProperties gridProperties;
    private TableViewService tableViewService;

    @FXML
    public void initialize() {
        tableViewService = new TableViewService(tableView);
        initialConditionSelect.setItems(InitialConditionDictionary.getGameOfLifeInitialConditionList());
    }

    public void startGame() {
        if (!drawButtonClicked || gridProperties == null) {
            return;
        }
        GameOfLifeProcessService service = new GameOfLifeProcessService(tableViewService, gridProperties);
        service.run();
    }

    public void drawGrid() {
        gridProperties = createGridProperties();
        if (drawButtonClicked || gridProperties == null) {
            return;
        }
        drawButtonClicked = true;
        tableViewService.prepareTableView(gridProperties);
        applyChosenInitialCondition(gridProperties);
        tableViewService.setTableViewData(gridProperties.getGrid());
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


    private GridProperties createGridProperties() {
        if (!checkRequiredInputs()) {
            return null;
        }
        Integer height = Integer.valueOf(gridHeight.getText());
        Integer width = Integer.valueOf(gridWidth.getText());
        return new GridProperties(height, width);
    }

    private boolean checkRequiredInputs() {
        return gridHeight.getText() != null && gridWidth.getText() != null && initialCondition != null;
    }
}
