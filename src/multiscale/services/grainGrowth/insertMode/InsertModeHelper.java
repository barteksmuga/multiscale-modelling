package multiscale.services.grainGrowth.insertMode;

import multiscale.enums.StateEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;

import java.util.Random;

public class InsertModeHelper {

    public static void applyRandomInsertMode(Grid grid, int grainNumber) {
        Random random = new Random();
        Cell[][] localGrid = grid.getGrid();
        boolean getRandomIndexes = false;
        for (int i = 0; i < grainNumber; ++i) {
            do {
                int xIndex = random.nextInt(grid.getWidth());
                int yIndex = random.nextInt(grid.getHeight());

                Cell cell = localGrid[yIndex][xIndex];
                if (cell.getState() != StateEnum.NOT_SET.getStateValue()) {
                    getRandomIndexes = true;
                } else {
                    getRandomIndexes = false;
                    cell.setAutoState();
                }
            } while (getRandomIndexes);
        }
    }
}
