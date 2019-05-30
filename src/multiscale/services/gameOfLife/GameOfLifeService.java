package multiscale.services.gameOfLife;

import javafx.scene.layout.GridPane;
import multiscale.enums.StateEnum;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.Service;

public class GameOfLifeService extends Service {

    public GameOfLifeService(Grid grid, GridPane gridPane) {
        super(grid, gridPane, NeighbourhoodEnum.MOORE);
    }

    @Override
    protected void nextStep() {
        System.out.println("nextStep()");
        int gridHeight = grid.getHeight();
        int gridWidth = grid.getWidth();
        Cell[][] baseGrid = grid.getGrid();
        for (int y = 0; y < gridHeight; ++y) {
            for (int x = 0; x < gridWidth; ++x) {
                int activeNeighbourCount = countActiveNeighbour(baseGrid, x, y);
                int updatedState = calculateNewState(baseGrid, x, y, activeNeighbourCount);
                grid.getGrid()[y][x].setState(updatedState);
            }
        }
        appendToGrid();
        if (!continueProcessing()) {
            stop();
        }
    }

    private boolean continueProcessing() {
        boolean cont = false;
        for (Cell[] row : grid.getGrid()) {
            for (Cell cell : row) {
                if (cell.getState() == StateEnum.ACTIVE.getStateValue()) {
                    cont = true;
                }
            }
        }
        return cont;
    }

    private int calculateNewState(Cell[][] grid, int x, int y, int activeNeighbourCount) {
        Cell currentCell = grid[y][x];
        if (currentCell.getState() == 1) {
            if (activeNeighbourCount > 3 || activeNeighbourCount < 2) {
                return 0;
            }
            return 1;
        }
        if (activeNeighbourCount == 3) {
            return 1;
        }
        return 0;
    }

    private int countActiveNeighbour(Cell[][] grid, int x, int y) {
        int count = 0;
        count += countNeighbourAbove(grid, x, y);
        count += countNeighbourBelow(grid, x, y);
        count += countNeighbourBesides(grid, x, y);
        return count;
    }

    private int countNeighbourAbove(Cell[][] grid, int x, int y) {
        int previousX = getCorrectPreviousX((x - 1), grid[0].length - 1);
        int followingX = getCorrectFollowingX((x + 1), grid[0].length - 1);
        int aboveY = (y - 1);
        if (aboveY < 0) {
            aboveY = grid.length - 1;
        }
        return countStatesForProperValues(grid, aboveY, previousX, x, followingX);
    }

    private int countNeighbourBelow(Cell[][] grid, int x, int y) {
        int previousX = getCorrectPreviousX((x - 1), grid[0].length - 1);
        int followingX = getCorrectFollowingX((x + 1), grid[0].length - 1);
        int belowY = (y + 1);
        if (belowY >= grid.length) {
            belowY = 0;
        }
        return countStatesForProperValues(grid, belowY, previousX, x, followingX);
    }

    private int countNeighbourBesides(Cell[][] grid, int x, int y) {
        int previousX = getCorrectPreviousX((x - 1), grid[0].length - 1);
        int followingX = getCorrectFollowingX((x + 1), grid[0].length - 1);
        int count = grid[y][previousX].getState();
        count += grid[y][followingX].getState();
        return count;
    }

    private int countStatesForProperValues(Cell[][] grid, int y, int previousX, int x, int followingX) {
        int count = grid[y][previousX].getState();
        count += grid[y][x].getState();
        count += grid[y][followingX].getState();
        return count;
    }
}
