package multiscale.services.grainGrowth.neighbourhoodStrategies.hexagonal;

import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.boundaryConditions.BoundaryCondition;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;

import java.util.Random;

public class HexagonalNeighbourhoodStrategy extends NeighbourhoodStrategy {
    private Random random;

    public HexagonalNeighbourhoodStrategy(Grid grid, BoundaryCondition boundaryCondition) {
        super(grid, boundaryCondition);
        random = new Random();
    }

    @Override
    public void countNeighbourStates(int x, int y, Cell[][] localGrid) {
        int option = random.nextInt(2);
        boolean left = option == 0;
        countLeftOrRightOption(x, y, localGrid, left);
    }

    private void countLeftOrRightOption(int x, int y, Cell[][] localGrid, boolean isLeft) {
        int upY = getY(y - 1);
        int downY = getY(y + 1);
        int leftX = getX(x - 1);
        int rightX = getX(x + 1);

        addToMap(localGrid[y][leftX].getState());
        addToMap(localGrid[y][rightX].getState());
        addToMap(localGrid[upY][x].getState());
        addToMap(localGrid[downY][x].getState());
        if (isLeft) {
            addToMap(localGrid[downY][leftX].getState());
            addToMap(localGrid[upY][rightX].getState());
        } else {
            addToMap(localGrid[downY][rightX].getState());
            addToMap(localGrid[upY][leftX].getState());
        }
    }
}
