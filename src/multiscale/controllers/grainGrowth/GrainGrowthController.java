package multiscale.controllers.grainGrowth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import multiscale.constants.grainGrowth.ChoiceBoxOptions;
import multiscale.enums.ModeEnum;
import multiscale.enums.grainGrowth.BoundaryConditionEnum;
import multiscale.enums.grainGrowth.InsertModeEnum;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Grid;
import multiscale.services.GridPaneService;
import multiscale.services.grainGrowth.GrainGrowthService;
import multiscale.services.grainGrowth.insertMode.InsertModeHelper;

public class GrainGrowthController {

    @FXML TextField grainNumberField;
    @FXML TextField radiusField;
    @FXML ChoiceBox<String> boundaryConditionChoiceBox;
    @FXML ChoiceBox<String> neighbourhoodChoiceBox;
    @FXML GridPane drawGridArea;
    @FXML Button drawFirstRowButton;
    @FXML Button startSimulationButton;
    @FXML Button wipeResultsButton;
    @FXML TextField heightField;
    @FXML TextField widthField;
    @FXML ChoiceBox<String> insertModeChoiceBox;

    private Grid grid;
    private GridPaneService gridPaneService;

    @FXML
    public void initialize() {
        gridPaneService = new GridPaneService();
        radiusField.setDisable(true);
        insertModeChoiceBox.setItems(ChoiceBoxOptions.insertModeList());
        boundaryConditionChoiceBox.setItems(ChoiceBoxOptions.boundaryConditionList());
        neighbourhoodChoiceBox.setItems(ChoiceBoxOptions.neighbourhoodList());
        initializeInsertModeChoiceBoxListener();

        //todo remove hardcoded options!!
        boundaryConditionChoiceBox.setValue(BoundaryConditionEnum.PERIODICAL.getName());
        neighbourhoodChoiceBox.setValue(NeighbourhoodEnum.VON_NEUMANN.getName());
        insertModeChoiceBox.setValue(InsertModeEnum.CUSTOM.getName());
        widthField.setText("50");
        heightField.setText("50");
    }

    public void start(ActionEvent actionEvent) {
        NeighbourhoodEnum neighbourhoodStrategy = getNeighbourhoodStrategyEnum();
        var service = new GrainGrowthService(grid, drawGridArea, neighbourhoodStrategy);
        service.run();
    }


    public void wipeData(ActionEvent actionEvent) {
        gridPaneService.wipeGridPaneData(drawGridArea);
    }

    public void drawGrid(ActionEvent actionEvent) {
        int gridWidth = Integer.valueOf(widthField.getText());
        int gridHeight = Integer.valueOf(heightField.getText());

        grid = new Grid(gridWidth, gridHeight, ModeEnum.GRAIN_GROWTH);
        applyChosenInsertMode();
        gridPaneService.drawArrayOnGridPane(drawGridArea, grid);
    }

    private void initializeInsertModeChoiceBoxListener() {
        insertModeChoiceBox.getSelectionModel()
                .selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    String chosen = insertModeChoiceBox.getItems().get(newValue.intValue());
                    if (chosen.equals(InsertModeEnum.RANDOM_WITH_RADIUS.getName())) {
                        radiusField.setDisable(false);
                        grainNumberField.setDisable(false);
                    } else if (chosen.equals(InsertModeEnum.CUSTOM.getName())) {
                        grainNumberField.setDisable(true);
                        radiusField.setDisable(true);
                    } else {
                        grainNumberField.setDisable(false);
                        radiusField.setDisable(true);
                    }
                });
    }

    private void applyChosenInsertMode() {
        String insertMode = insertModeChoiceBox.getSelectionModel().getSelectedItem();
        InsertModeEnum insertModeEnum = InsertModeEnum.get(insertMode);
        insertModeEnum = insertModeEnum == null ? InsertModeEnum.CUSTOM : insertModeEnum;
        switch (insertModeEnum) {
            case RANDOM:
                int grainNumber = Integer.valueOf(grainNumberField.getText());
                InsertModeHelper.applyRandomInsertMode(grid, grainNumber);
                break;
            case CUSTOM:
                break;
            case RANDOM_WITH_RADIUS:
                break;
            case HOMOGENEOUS:
                break;
        }
    }


    private NeighbourhoodEnum getNeighbourhoodStrategyEnum() {
        String selectedNeighbourhood = neighbourhoodChoiceBox.getSelectionModel().getSelectedItem();
        NeighbourhoodEnum selectedNeighbourhoodEnum = NeighbourhoodEnum.get(selectedNeighbourhood);
        return selectedNeighbourhoodEnum == null ? NeighbourhoodEnum.VON_NEUMANN : selectedNeighbourhoodEnum;
    }
}
