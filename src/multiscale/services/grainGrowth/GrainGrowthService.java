package multiscale.services.grainGrowth;

import javafx.scene.layout.GridPane;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.Service;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;

public class GrainGrowthService extends Service {

    public GrainGrowthService(Grid grid, GridPane gridPane, NeighbourhoodEnum neighbourhoodEnum) {
        super(grid, gridPane, neighbourhoodEnum);
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
            timeline.stop();
            System.out.println("timeline.stop()");
        }
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
