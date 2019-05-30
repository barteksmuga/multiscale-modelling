package multiscale.controllers.gameOfLife;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import multiscale.constants.gameOfLife.InitialConditions;
import multiscale.enums.ModeEnum;
import multiscale.enums.gameOfLife.InitialConditionEnum;
import multiscale.enums.StateEnum;
import multiscale.models.Grid;
import multiscale.services.GridPaneService;
import multiscale.services.Service;
import multiscale.services.gameOfLife.GameOfLifeService;

public class GameOfLifeController {

    @FXML Button wipeResultsButton;
    @FXML Button drawButton;
    @FXML Button startSimulationButton;
    @FXML Button stopSimulationButton;
    @FXML TextField gridHeightField;
    @FXML TextField gridWidthField;
    @FXML ChoiceBox<String> initialConditionChoiceBox;
    @FXML GridPane drawGridArea;

    private Grid grid;
    private GridPaneService gridPaneService;
    private Service service;

    @FXML
    public void initialize() {
        initialConditionChoiceBox.setItems(InitialConditions.getGameOfLifeInitialConditionList());
        gridPaneService = new GridPaneService();
    }

    public void drawGrid(ActionEvent actionEvent) {
        int gridWidth = Integer.valueOf(gridWidthField.getText());
        int gridHeight = Integer.valueOf(gridHeightField.getText());
        grid = new Grid(gridWidth, gridHeight, ModeEnum.GAME_OF_LIFE);
        applyChosenCondition();
        draw();
    }

    public void start(ActionEvent actionEvent) {
        service = new GameOfLifeService(grid, drawGridArea);
        service.run();
    }

    public void stop(ActionEvent actionEvent) {
        service.stop();
    }

    public void wipeData(ActionEvent actionEvent) {
        gridPaneService.wipeGridPaneData(drawGridArea);
    }

    private void draw() {
        gridPaneService.drawArrayOnGridPane(drawGridArea, grid);
    }

    private void applyChosenCondition() {
        var initialCondition = initialConditionChoiceBox.getSelectionModel().getSelectedItem();
        var initialConditionEnum = InitialConditionEnum.get(initialCondition);
        if (!isInitialConditionRandomOrCustom(initialConditionEnum)) {
            var condition = InitialConditions.getCondition(initialConditionEnum, grid);
            for (var point: condition) {
                grid.getGrid()[point.y][point.x].setState(StateEnum.ACTIVE.getStateValue());
            }
        }
        if (initialConditionEnum == InitialConditionEnum.RANDOM) {
            grid.setRandomCellActive();
        }
    }

    private boolean isInitialConditionRandomOrCustom(InitialConditionEnum initialConditionEnum) {
        return initialConditionEnum == InitialConditionEnum.RANDOM || initialConditionEnum == InitialConditionEnum.CUSTOM;
    }
}
