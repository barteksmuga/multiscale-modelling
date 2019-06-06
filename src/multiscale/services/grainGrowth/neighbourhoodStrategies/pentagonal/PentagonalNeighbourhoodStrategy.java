package multiscale.services.grainGrowth.neighbourhoodStrategies.pentagonal;

import javafx.scene.paint.Paint;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.boundaryConditions.BoundaryCondition;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;

import java.util.Random;

public class PentagonalNeighbourhoodStrategy extends NeighbourhoodStrategy {
    private static final int LEFT_ID = 0;
    private static final int RIGHT_ID = 1;
    private static final int UP_ID = 2;
    private static final int DOWN_ID = 3;
    private Random random;
    private int option;

    public PentagonalNeighbourhoodStrategy(Grid grid, BoundaryCondition boundaryCondition) {
        super(grid, boundaryCondition);
        random = new Random();
    }

    @Override
    protected void collectNeighbours(int x, int y) {
        Cell current = grid.getGrid()[y][x];
        switch (option) {
            case LEFT_ID:
                int[][] leftNeighbours = {
                        {
                            grid.getGrid()[getY(y-1)][getX(x-1)].getcId(),
                            grid.getGrid()[getY(y-1)][getX(x)].getcId(),
                                -1

                        },
                        {
                                grid.getGrid()[getY(y)][getX(x-1)].getcId(),
                                -1,
                                -1
                        },
                        {
                                grid.getGrid()[getY(y+1)][getX(x-1)].getcId(),
                                grid.getGrid()[getY(y+1)][getX(x)].getcId(),
                                -1
                        },
                };
                addToNeighbourMap(current.getcId(), leftNeighbours);
                return;
            case RIGHT_ID:
                int[][] rightNeighbours = {
                        {
                                -1,
                                grid.getGrid()[getY(y-1)][getX(x+1)].getcId(),
                                grid.getGrid()[getY(y-1)][getX(x)].getcId()

                        },
                        {
                                -1,
                                -1,
                                grid.getGrid()[getY(y)][getX(x+1)].getcId()
                        },
                        {
                                -1,
                                grid.getGrid()[getY(y+1)][getX(x)].getcId(),
                                grid.getGrid()[getY(y+1)][getX(x+1)].getcId(),
                        },
                };
                addToNeighbourMap(current.getcId(), rightNeighbours);
                return;
            case UP_ID:
                int[][] upNeighbours = {
                        {
                                grid.getGrid()[getY(y-1)][getX(x-1)].getcId(),
                                grid.getGrid()[getY(y-1)][getX(x)].getcId(),
                                grid.getGrid()[getY(y-1)][getX(x+1)].getcId()

                        },
                        {
                                grid.getGrid()[getY(y)][getX(x-1)].getcId()
                                -1,
                                grid.getGrid()[getY(y)][getX(x+1)].getcId()
                        },
                        {
                                -1,
                                -1,
                                -1
                        },
                };
                addToNeighbourMap(current.getcId(), upNeighbours);
                return;
            case DOWN_ID:
                int[][] downNeighbours = {
                        {
                                -1,
                                -1,
                                -1
                        },
                        {
                                grid.getGrid()[getY(y)][getX(x-1)].getcId(),
                                -1,
                                grid.getGrid()[getY(y)][getX(x+1)].getcId()

                        },
                        {
                                grid.getGrid()[getY(y-1)][getX(x-1)].getcId(),
                                grid.getGrid()[getY(y-1)][getX(x)].getcId(),
                                grid.getGrid()[getY(y-1)][getX(x+1)].getcId()
                        },
                };
                addToNeighbourMap(current.getcId(), downNeighbours);
                return;
        }
    }

    @Override
    public void countNeighbourStates(int x, int y, Cell[][] localGrid) {
        option = random.nextInt(4);
        switch (option) {
            case LEFT_ID:
                countLeftOrRightOption(x, y, localGrid, true);
                break;
            case RIGHT_ID:
                countLeftOrRightOption(x, y, localGrid, false);
                break;
            case UP_ID:
                countUpOrDownOption(x, y, localGrid, true);
                break;
            case DOWN_ID:
                countUpOrDownOption(x, y, localGrid, false);
                break;
        }
    }

    private void countLeftOrRightOption(int x, int y, Cell[][] localGrid, boolean isLeft) {
        int besideX;
        if (isLeft) {
            besideX = getX(x - 1);
        } else {
            besideX = getX(x + 1);
        }
        int upY = getY(y - 1);
        int downY = getY(y + 1);

        addToMap(localGrid[upY][besideX].getState());
        addToMap(localGrid[upY][x].getState());
        addToMap(localGrid[y][besideX].getState());
        addToMap(localGrid[downY][besideX].getState());
        addToMap(localGrid[downY][x].getState());
    }

    private void countUpOrDownOption(int x, int y, Cell[][] localGrid, boolean isUp) {
        int besideY;
        if (isUp) {
            besideY = getY(y - 1);
        } else {
            besideY = getY(y + 1);
        }
        int leftX = getX(x - 1);
        int rightX = getX(x + 1);

        addToMap(localGrid[y][leftX].getState());
        addToMap(localGrid[besideY][leftX].getState());
        addToMap(localGrid[besideY][x].getState());
        addToMap(localGrid[besideY][rightX].getState());
        addToMap(localGrid[y][rightX].getState());
    }
}
