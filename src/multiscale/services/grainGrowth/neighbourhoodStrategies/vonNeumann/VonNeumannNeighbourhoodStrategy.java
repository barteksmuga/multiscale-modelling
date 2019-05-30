package multiscale.services.grainGrowth.neighbourhoodStrategies.vonNeumann;

import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.boundaryConditions.BoundaryCondition;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;

public class VonNeumannNeighbourhoodStrategy extends NeighbourhoodStrategy {

    public VonNeumannNeighbourhoodStrategy(Grid grid, BoundaryCondition boundaryCondition) {
        super(grid, boundaryCondition);
    }

    @Override
    public void countNeighbourStates(int x, int y, Cell[][] localGrid) {
        int correctPreviousX = getX(x - 1);
        addToMap(localGrid[y][correctPreviousX].getState());

        int correctFollowingX = getX(x + 1);
        addToMap(localGrid[y][correctFollowingX].getState());

        int correctPreviousY = getY(y - 1);
        addToMap(localGrid[correctPreviousY][x].getState());

        int correctFollowingY = getY(y + 1);
        addToMap(localGrid[correctFollowingY][x].getState());
    }
}
