package multiscale.services.grainGrowth.neighbourhoodStrategies.moore;

import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.boundaryConditions.BoundaryCondition;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;

public class MooreNeighbourhoodStrategy extends NeighbourhoodStrategy {

    public MooreNeighbourhoodStrategy(Grid grid, BoundaryCondition boundaryCondition) {
        super(grid, boundaryCondition);
    }

    @Override
    protected void collectNeighbours(int x, int y) {
        Cell current = grid.getGrid()[getY(y)][getX(x)];
        int[][] neighbours = {
                {
                        grid.getGrid()[getY(y - 1)][getX(x - 1)].getcId(),
                        grid.getGrid()[getY(y - 1)][getX(x)].getcId(),
                        grid.getGrid()[getY(y - 1)][getX(x + 1)].getcId()
                },
                {
                        grid.getGrid()[getY(y)][getX(x - 1)].getcId(),
                        -1,
                        grid.getGrid()[getY(y)][getX(x + 1)].getcId()

                },
                {
                        grid.getGrid()[getY(y + 1)][getX(x - 1)].getcId(),
                        grid.getGrid()[getY(y + 1)][getX(x)].getcId(),
                        grid.getGrid()[getY(y + 1)][getX(x + 1)].getcId()
                },
        };

        addToNeighbourMap(current.getcId(), neighbours);
    }

    @Override
    public void countNeighbourStates(int x, int y, Cell[][] grid) {
        int correctPreviousX = getX(x - 1);
        int correctFollowingX = getX(x + 1);
        int correctPreviousY = getY(y - 1);
        int correctFollowingY = getY(y + 1);

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
