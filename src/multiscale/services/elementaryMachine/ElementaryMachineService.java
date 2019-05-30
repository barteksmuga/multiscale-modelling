package multiscale.services.elementaryMachine;

import javafx.scene.layout.GridPane;
import multiscale.constants.elementaryMachine.Rules;
import multiscale.enums.grainGrowth.BoundaryConditionEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.Service;

import java.util.List;

public class ElementaryMachineService extends Service {

    public ElementaryMachineService(Grid grid, GridPane gridPane) {
        super(grid, gridPane, null, BoundaryConditionEnum.PERIODICAL);
    }

    @Override
    public void run() {
        nextStep();
    }

    @Override
    protected void nextStep() {
        var ruleMask = getRuleMask(grid.getRuleIndicator());
        for (int step = 0; step < (grid.getHeight() - 1); ++step) {
            for (int cell = 0; cell < grid.getWidth(); ++cell) {
                int cellCheckSum = getNextCellState(grid.getGrid(), step, cell + 1);
                cellCheckSum += grid.getGrid()[step][cell].getState() << 1;
                cellCheckSum += getPreviousCellState(grid.getGrid(), step, cell - 1) << 2;
                grid.getGrid()[step + 1][cell].setState(ruleMask.get(cellCheckSum));
            }
        }
        appendToGrid();
    }

    private List<Integer> getRuleMask(int ruleIndicator) {
        return Rules.getRule(ruleIndicator);
    }

    private int getNextCellState(Cell[][] grid, int step, int cell) {
        cell = getX(cell);
        return grid[step][cell].getState();
    }

    private int getPreviousCellState(Cell[][] grid, int step, int cell) {
        cell = getX(cell);
        return grid[step][cell].getState();
    }
}
