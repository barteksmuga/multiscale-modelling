package multiscale.services.grainGrowth;

import javafx.scene.layout.GridPane;
import multiscale.enums.grainGrowth.BoundaryConditionEnum;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.processors.monteCarlo.MonteCarloDTO;
import multiscale.processors.monteCarlo.MonteCarloProcessor;
import multiscale.services.Service;

public class GrainGrowthService extends Service {
    private NeighbourhoodEnum neighbourhoodEnum;
    private BoundaryConditionEnum boundaryConditionEnum;
    private MonteCarloDTO monteCarloDTO;

    public GrainGrowthService(Grid grid, GridPane gridPane, NeighbourhoodEnum neighbourhoodEnum,
                              BoundaryConditionEnum boundaryConditionEnum, MonteCarloDTO monteCarloDTO) {
        super(grid, gridPane, neighbourhoodEnum, boundaryConditionEnum);
        this.neighbourhoodEnum = neighbourhoodEnum;
        this.boundaryConditionEnum = boundaryConditionEnum;
        this.monteCarloDTO = monteCarloDTO;
    }

    @Override
    protected void nextStep() {
        System.out.println("nextStep");
        Cell[][] localGrid = copyGrid();
        for (int y = 0; y < grid.getHeight(); ++y) {
            for (int x = 0; x < grid.getWidth(); ++x) {
                Cell cell = grid.getGrid()[y][x];
                if (cell.getState() == -1) {
                    int mostFrequentNeighbourState = neighbourhoodStrategy.mostFrequentNeighbourState(x, y, localGrid);
                    cell.setState(mostFrequentNeighbourState);
                }
            }
        }
        appendToGrid();
        if (!isAnyEmptyCellLeft()) {
            stop();
            runMonteCarloIfRequested();
        }
    }

    private void runMonteCarloIfRequested() {
        if (monteCarloDTO.isProcess()) {
            System.out.println("CA finished -> starting monteCarlo");
            runMonteCarloProcessing(monteCarloDTO);
        }
    }

    private void runMonteCarloProcessing(MonteCarloDTO monteCarloDTO) {
        var monteCarloProcessor = new MonteCarloProcessor(grid, gridPane, neighbourhoodEnum, boundaryConditionEnum, monteCarloDTO);
        monteCarloProcessor.process();
    }

    private boolean isAnyEmptyCellLeft() {
        boolean isAnyEmpty = false;
        for (Cell[] row : grid.getGrid()) {
            for (Cell cell : row) {
                if (cell.getState() == -1) {
                    isAnyEmpty = true;
                    break;
                }
            }
        }
        return isAnyEmpty;
    }

    private Cell[][] copyGrid() {
        Cell[][] copy = new Cell[grid.getHeight()][grid.getWidth()];
        for (int i=0; i<grid.getHeight(); ++i) {
            for (int j=0; j<grid.getWidth(); ++j) {
                copy[i][j] = new Cell(grid.getGrid()[i][j]);
            }
        }
        return copy;
    }
}
