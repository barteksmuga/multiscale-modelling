package multiscale.services.grainGrowth.neighbourhoodStrategies.vonNeumann;

import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;

public class VonNeumannNeighbourhoodStrategy extends NeighbourhoodStrategy {

    public VonNeumannNeighbourhoodStrategy(Grid grid) {
        super(grid);
    }

    @Override
    public void countNeighbourStates(int x, int y, Cell[][] localGrid) {
        int correctPreviousX = getCorrectPreviousX(x - 1);
        addToMap(localGrid[y][correctPreviousX].getState());

        int correctFollowingX = getCorrectFollowingX(x + 1);
        addToMap(localGrid[y][correctFollowingX].getState());

        int correctPreviousY = getCorrectPreviousY(y - 1);
        addToMap(localGrid[correctPreviousY][x].getState());

        int correctFollowingY = getCorrectFollowingY(y + 1);
        addToMap(localGrid[correctFollowingY][x].getState());
    }
}
