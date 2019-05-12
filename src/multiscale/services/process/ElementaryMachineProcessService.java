package multiscale.services.process;

import multiscale.helpers.RuleDictionary;
import multiscale.models.Cell;
import multiscale.models.GridProperties;
import multiscale.services.tableView.TableViewService;

import java.util.List;

public class ElementaryMachineProcessService extends ProcessService {

    public ElementaryMachineProcessService(TableViewService tableViewService, GridProperties gridProperties) {
        super(tableViewService, gridProperties);
    }

    public void run() {
        setFirstRowValues();
        Cell[][] grid = gridProperties.getGrid();
        List<Integer> rule = getRule(gridProperties.getRuleInd());
        for (int step = 0; step < gridProperties.getGridHeight() - 1; ++step) {
            for (int cell = 0; cell < gridProperties.getGridWidth(); ++cell) {
                int cellCheckSum = getNextCellState(grid, step, cell + 1);
                cellCheckSum += grid[step][cell].getState() << 1;
                cellCheckSum += getPreviousCellState(grid, step, cell - 1) << 2;
                grid[step + 1][cell].setState(rule.get(cellCheckSum));
            }
        }
        setTableViewData(grid);
    }

    private void setFirstRowValues() {
        gridProperties.getGrid()[0] = gridProperties.getFirstRow()[0];
    }

    private int getNextCellState(Cell[][] grid, int step, int cell) {
        cell = getCorrectFollowingX(cell, grid[0].length - 1);
        return grid[step][cell].getState();
    }

    private int getPreviousCellState(Cell[][] grid, int step, int cell) {
        cell = getCorrectPreviousX(cell, grid[0].length-1);
        return grid[step][cell].getState();
    }

    private List<Integer> getRule(int indicator) {
        return RuleDictionary.getRule(indicator);
    }
}
