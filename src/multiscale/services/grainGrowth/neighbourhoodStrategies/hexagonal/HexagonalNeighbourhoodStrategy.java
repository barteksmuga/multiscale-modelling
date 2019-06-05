package multiscale.services.grainGrowth.neighbourhoodStrategies.hexagonal;

import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.boundaryConditions.BoundaryCondition;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;

import java.util.Random;

public class HexagonalNeighbourhoodStrategy extends NeighbourhoodStrategy {
    private Random random;
    private int option;
    private boolean isLeft;
    private boolean isRight;
    private boolean isRandom;

    public HexagonalNeighbourhoodStrategy(Grid grid, BoundaryCondition boundaryCondition, boolean isLeft, boolean isRight, boolean isRandom) {
        super(grid, boundaryCondition);
        this.isLeft = isLeft;
        this.isRandom = isRandom;
        this.isRight = isRight;
        if (isRandom) {
            random = new Random();
        }
    }

    @Override
    protected void collectNeighbours(int x, int y) {

    }

    @Override
    public void countNeighbourStates(int x, int y, Cell[][] localGrid) {
        if (isRandom) {
            option = random.nextInt(2);
            isLeft = option == 0;
        }
        countLeftOrRightOption(x, y, localGrid, isLeft);
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
