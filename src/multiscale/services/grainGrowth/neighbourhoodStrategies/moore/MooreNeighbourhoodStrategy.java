package multiscale.services.grainGrowth.neighbourhoodStrategies.moore;

import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;

public class MooreNeighbourhoodStrategy extends NeighbourhoodStrategy {

    public MooreNeighbourhoodStrategy(Grid grid) {
        super(grid);
    }

    @Override
    public void countNeighbourStates(int x, int y, Cell[][] grid) {
        int correctPreviousX = getCorrectPreviousX(x - 1);
        int correctFollowingX = getCorrectFollowingX(x + 1);
        int correctPreviousY = getCorrectPreviousY(y - 1);
        int correctFollowingY = getCorrectFollowingY(y + 1);

        countStatesAbove(grid, x, correctPreviousX, correctFollowingX, correctPreviousY);
        countStatesBelow(grid, x, correctPreviousX, correctFollowingX, correctFollowingY);
        countStatesBeside(grid, y, correctPreviousX, correctFollowingX);
    }

    private void countStatesAbove(Cell[][] grid, int x, int correctPreviousX, int correctFollowingX, int correctPreviousY) {
        addToMap(grid[correctPreviousY][correctPreviousX].getState());
        addToMap(grid[correctPreviousY][x].getState());
        addToMap(grid[correctPreviousY][correctFollowingX].getState());
    }

    private void countStatesBelow(Cell[][] grid, int x, int correctPreviousX, int correctFollowingX, int correctFollowingY) {
        addToMap(grid[correctFollowingY][correctPreviousX].getState());
        addToMap(grid[correctFollowingY][x].getState());
        addToMap(grid[correctFollowingY][correctFollowingX].getState());
    }

    private void countStatesBeside(Cell[][] grid, int y, int correctPreviousX, int correctFollowingX) {
        addToMap(grid[y][correctPreviousX].getState());
        addToMap(grid[y][correctFollowingX].getState());
    }
}