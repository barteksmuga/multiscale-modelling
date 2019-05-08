package multiscale.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import multiscale.helpers.RuleDictionary;
import multiscale.model.Cell;
import multiscale.model.ProcessRuleProperties;

import java.util.List;

public class ProcessRuleService {
    private ProcessRuleProperties properties;
    private TableView tableView;

    public ProcessRuleService(ProcessRuleProperties properties, TableView tableView) {
        this.properties = properties;
        this.tableView = tableView;
    }

    public void run() {
        setFirstRowValues();
        Cell[][] grid = properties.getGrid();
        List<Integer> rule = getRule(properties.getRuleInd());
        for (int step = 0; step < properties.getGridHeight() - 1; ++step) {
            for (int cell = 0; cell < properties.getGridWidth(); ++cell) {
                int cellCheckSum = getNextCellState(grid, step, cell + 1);
                cellCheckSum += grid[step][cell].getState() << 1;
                cellCheckSum += getPreviousCellState(grid, step, cell - 1) << 2;
                grid[step + 1][cell].setState(rule.get(cellCheckSum));
            }
        }
        appendToTableView(grid);
    }

    private void appendToTableView(Cell[][] grid) {
        ObservableList<ObservableList<Cell>> data = FXCollections.observableArrayList();
        for (Cell [] row : grid) {
            data.add(FXCollections.observableArrayList(row));
        }
        tableView.setItems(data);
    }

    private void setFirstRowValues() {
        properties.getGrid()[0] = properties.getFirstRow()[0];
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

    private List<Integer> getRule(int indicator) {
        return RuleDictionary.getRule(indicator);
    }
}
