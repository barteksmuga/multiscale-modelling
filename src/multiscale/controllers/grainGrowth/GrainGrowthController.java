package multiscale.controllers.grainGrowth;

import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import multiscale.constants.grainGrowth.ChoiceBoxOptions;
import multiscale.enums.ModeEnum;
import multiscale.enums.grainGrowth.BoundaryConditionEnum;
import multiscale.enums.grainGrowth.InsertModeEnum;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Grid;
import multiscale.processors.monteCarlo.MonteCarloDTO;
import multiscale.services.GridPaneService;
import multiscale.services.Service;
import multiscale.services.grainGrowth.GrainGrowthService;
import multiscale.services.grainGrowth.insertMode.InsertModeHelper;

public class GrainGrowthController {

    @FXML Label firstLabel;
    @FXML Label secondLabel;
    @FXML TextField grainNumberField;
    @FXML TextField radiusField;
    @FXML ChoiceBox<String> boundaryConditionChoiceBox;
    @FXML ChoiceBox<String> neighbourhoodChoiceBox;
    @FXML GridPane drawGridArea;
    @FXML Button drawFirstRowButton;
    @FXML Button startSimulationButton;
    @FXML Button stopSimulationButton;
    @FXML Button wipeResultsButton;
    @FXML TextField heightField;
    @FXML TextField widthField;
    @FXML ChoiceBox<String> insertModeChoiceBox;
    @FXML CheckBox monteCarloCheckBox;
    @FXML TextField mcIterationInput;

    private Grid grid;
    private GridPaneService gridPaneService;
    private Service service;
    private MonteCarloDTO monteCarloDTO;

    @FXML
    public void initialize() {
        gridPaneService = new GridPaneService();
        insertModeChoiceBox.setItems(ChoiceBoxOptions.insertModeList());
        boundaryConditionChoiceBox.setItems(ChoiceBoxOptions.boundaryConditionList());
        neighbourhoodChoiceBox.setItems(ChoiceBoxOptions.neighbourhoodList());
        mcIterationInput.setVisible(false);
        initializeInsertModeChoiceBoxListener();
        initializeCheckBox();
        monteCarloDTO = new MonteCarloDTO();

        //todo remove hardcoded options!!
        boundaryConditionChoiceBox.setValue(BoundaryConditionEnum.PERIODICAL.getName());
        neighbourhoodChoiceBox.setValue(NeighbourhoodEnum.VON_NEUMANN.getName());
        insertModeChoiceBox.setValue(InsertModeEnum.CUSTOM.getName());
        widthField.setText("50");
        heightField.setText("50");
    }

    public void start(ActionEvent actionEvent) {
        NeighbourhoodEnum neighbourhoodStrategyEnum = getNeighbourhoodStrategyEnum();
        BoundaryConditionEnum boundaryConditionEnum = getBoundaryConditionEnum();
        if (monteCarloDTO.isProcess()) {
            monteCarloDTO.setIteration(mcIterationInput.getText() == null ? 0 : Integer.valueOf(mcIterationInput.getText()));
        }
        service = new GrainGrowthService(grid, drawGridArea, neighbourhoodStrategyEnum, boundaryConditionEnum,
                monteCarloDTO);
        service.run();
    }

    public void stop(ActionEvent actionEvent) {
        service.stop();
    }

    private BoundaryConditionEnum getBoundaryConditionEnum() {
        String chosenBC = boundaryConditionChoiceBox.getSelectionModel().getSelectedItem();
        BoundaryConditionEnum chosenEnum = BoundaryConditionEnum.get(chosenBC);
        return chosenEnum == null ? BoundaryConditionEnum.PERIODICAL : chosenEnum;
    }

    private void initializeCheckBox() {
        monteCarloCheckBox.selectedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    mcIterationInput.setVisible(newValue);
                    monteCarloDTO.setProcess(newValue);
                });
    }

    public void wipeData(ActionEvent actionEvent) {
        gridPaneService.wipeGridPaneData(drawGridArea);
    }

    public void drawGrid(ActionEvent actionEvent) {
        int gridWidth = Integer.valueOf(widthField.getText());
        int gridHeight = Integer.valueOf(heightField.getText());

        grid = new Grid(gridWidth, gridHeight, ModeEnum.GRAIN_GROWTH);
        if (applyChosenInsertMode()) {
            return;
        }
        gridPaneService.drawArrayOnGridPane(drawGridArea, grid);
    }

    private void initializeInsertModeChoiceBoxListener() {
        insertModeChoiceBox.getSelectionModel()
                .selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    String chosen = insertModeChoiceBox.getItems().get(newValue.intValue());
                    InsertModeEnum insertModeEnum = InsertModeEnum.get(chosen);
                    insertModeEnum = insertModeEnum == null ? InsertModeEnum.CUSTOM : insertModeEnum;
                    switch (insertModeEnum) {
                        case HOMOGENEOUS:
                            firstLabel.setText("wiersze");
                            grainNumberField.setVisible(true);
                            secondLabel.setText("kolumny");
                            radiusField.setVisible(true);
                            break;
                        case RANDOM_WITH_RADIUS:
                            radiusField.setVisible(true);
                            grainNumberField.setVisible(true);
                            firstLabel.setText("Liczba ziaren");
                            secondLabel.setText("Promień");
                            break;
                        case CUSTOM:
                            grainNumberField.setVisible(false);
                            radiusField.setVisible(false);
                            firstLabel.setText("");
                            secondLabel.setText("");
                            break;
                        case RANDOM:
                            firstLabel.setText("Liczba ziaren");
                            grainNumberField.setVisible(true);
                            radiusField.setVisible(false);
                            secondLabel.setText("");
                            break;
                    }
                });
    }

    private boolean applyChosenInsertMode() {
        String insertMode = insertModeChoiceBox.getSelectionModel().getSelectedItem();
        InsertModeEnum insertModeEnum = InsertModeEnum.get(insertMode);
        insertModeEnum = insertModeEnum == null ? InsertModeEnum.CUSTOM : insertModeEnum;
        int grainNumber;
        String result;
        switch (insertModeEnum) {
            case RANDOM:
                grainNumber = Integer.valueOf(grainNumberField.getText());
                InsertModeHelper.applyRandomInsertMode(grid, grainNumber);
                return false;
            case RANDOM_WITH_RADIUS:
//                grainNumber = Integer.valueOf(grainNumberField.getText());
//                int radius = Integer.valueOf(radiusField.getText());
//                result = InsertModeHelper.applyRandomWithRadiusInsertMode(grid, grainNumber, radius);
//                return openAlert(result);
                return true;
            case HOMOGENEOUS:
                int rowGrainNumber = Integer.valueOf(grainNumberField.getText());
                int columnGrainNumber = Integer.valueOf(radiusField.getText());
                result = InsertModeHelper.applyHomogeneousInsertMode(grid, rowGrainNumber, columnGrainNumber);
                return openAlert(result);
        }
        return false;
    }

    private boolean openAlert(String result) {
        if (result != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(result);
            alert.setContentText("Wprowadź inną liczbę ziaren");
            alert.showAndWait();
            return true;
        }
        return false;
    }


    private NeighbourhoodEnum getNeighbourhoodStrategyEnum() {
        String selectedNeighbourhood = neighbourhoodChoiceBox.getSelectionModel().getSelectedItem();
        NeighbourhoodEnum selectedNeighbourhoodEnum = NeighbourhoodEnum.get(selectedNeighbourhood);
        return selectedNeighbourhoodEnum == null ? NeighbourhoodEnum.VON_NEUMANN : selectedNeighbourhoodEnum;
    }
}
