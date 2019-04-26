package multiscale.service;

import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import multiscale.helpers.RuleDictionary;
import multiscale.model.Cell;
import multiscale.model.ProcessRuleProperties;

import javafx.scene.control.TableView;

import java.util.Arrays;
import java.util.List;

public class ProcessRuleService {
    private ProcessRuleProperties properties;
    private TableView tableView;

    private final int CELL_SIZE = 10;
    private final int CANVAS_WIDTH = 700;


    public ProcessRuleService(ProcessRuleProperties properties, TableView tableView) {
        this.properties = properties;
        this.tableView = tableView;
    }

    public void run() {
        initialize();
        Cell[][] grid = properties.getGrid();
        List<Integer> rule = getRule(properties.getRuleInd());
        grid[0][0].setState(1);
        grid[0][1].setState(1);
        for (int step = 0; step < properties.getGridHeight() - 1; ++step) {
            for (int cell = 0; cell < properties.getGridWidth(); ++cell) {
                int cellCheckSum = getNextCellState(grid, step, cell + 1);
                cellCheckSum += grid[step][cell].getState() << 1;
                cellCheckSum += getPreviousCellState(grid, step, cell - 1) << 2;
                grid[step + 1][cell].setState(rule.get(cellCheckSum));
            }
            tableView.setItems(FXCollections.observableArrayList(grid[step]));
        }
    }

    private int getNextCellState(Cell[][] grid, int step, int cell) {
        if (cell >= grid[step].length) {
            return grid[step][0].getState();
        }
        return grid[step][cell].getState();
    }

    private int getPreviousCellState(Cell[][] grid, int step, int cell) {
        if (cell < 0) {
            int maxCellNumber = grid[step].length - 1;
            return grid[step][maxCellNumber].getState();
        }
        return grid[step][cell].getState();
    }

    private void initialize() {
//        graphicsContext.setFill(Color.WHITE);
//        graphicsContext.fillRect(0,0, 700, 500);
    }

    private List<Integer> getRule(int indicator) {
        return RuleDictionary.getRule(indicator);
    }
}
