package multiscale.services.process;

import multiscale.models.Cell;
import multiscale.models.GridProperties;
import multiscale.services.tableView.TableViewService;

public class GameOfLifeProcessService extends ProcessService {

    public GameOfLifeProcessService(TableViewService tableViewService, GridProperties gridProperties) {
        super(tableViewService, gridProperties);
    }

    public void run() {
        Cell[][] grid = gridProperties.getGrid();
        int count = 0;
        while (!shouldStopSimulation(grid)) {
            updateGridData(grid);
            if (count > 5000) {
                System.out.println("count " + count);
                setTableViewData(grid);
                count = 0;
            }
            ++count;
        }
    }

    private boolean shouldStopSimulation(Cell[][] grid) {
        int active = 0;
        int inactive = 0;
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.getState() == 1) {
                    ++active;
                } else {
                    ++inactive;
                }
            }
        }
        int max = grid.length * grid[0].length;
        return (active == 0 && inactive == max) || (active == max && inactive == 0);
    }

    private void updateGridData(Cell[][] grid) {
        for (int y = 0; y < gridProperties.getGridHeight(); ++y) {
            for (int x = 0; x < gridProperties.getGridWidth(); ++x) {
                int activeNeighbourCount = countActiveNeighbour(grid, x, y);
                int updatedState = calculateNewState(grid, x, y, activeNeighbourCount);
                grid[y][x].setState(updatedState);
            }
        }
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
            aboveY = grid.length-1;
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
