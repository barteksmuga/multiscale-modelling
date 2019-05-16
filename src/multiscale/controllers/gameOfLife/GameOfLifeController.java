package multiscale.controllers.gameOfLife;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import multiscale.constants.InitialConditions;
import multiscale.constants.enums.InitialConditionEnum;
import multiscale.constants.enums.StateEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.GridPaneService;
import multiscale.services.gameOfLife.GameOfLifeService;

public class GameOfLifeController {

    @FXML Button wipeResultsButton;
    @FXML Button drawButton;
    @FXML Button startSimulationButton;
    @FXML TextField gridHeightField;
    @FXML TextField gridWidthField;
    @FXML ChoiceBox<String> initialConditionChoiceBox;
    @FXML GridPane drawGridArea;

    private Grid grid;
    private GridPaneService gridPaneService;

    @FXML
    public void initialize() {
        initialConditionChoiceBox.setItems(InitialConditions.getGameOfLifeInitialConditionList());
        gridPaneService = new GridPaneService();
    }

    public void handleMouseClicked(MouseEvent mouseEvent) {
        //if chosen condition is custom input...
    }

    public void drawGrid(ActionEvent actionEvent) {
        int gridWidth = Integer.valueOf(gridWidthField.getText());
        int gridHeight = Integer.valueOf(gridHeightField.getText());
        grid = new Grid(gridWidth, gridHeight);
        applyChosenCondition();
        draw();
    }

    public void start(ActionEvent actionEvent) throws InterruptedException {
        GameOfLifeService service = new GameOfLifeService(grid, drawGridArea);
        service.run();
    }

    public void wipeData(ActionEvent actionEvent) {
        gridPaneService.wipeGridPaneData(drawGridArea);
    }

    private void draw() {
        gridPaneService.drawArrayOnGridPane(drawGridArea, grid);
    }

    private void applyChosenCondition() {
        var initialCondition = initialConditionChoiceBox.getSelectionModel().getSelectedItem();
        if (initialCondition != null) {
            var condition = InitialConditions.getCondition(InitialConditionEnum.get(initialCondition), grid);
            for (var point : condition) {
                grid.getGrid()[point.y][point.x].setState(StateEnum.ACTIVE.getStateValue());
            }
        }
    }
}
