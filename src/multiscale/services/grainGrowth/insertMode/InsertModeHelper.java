package multiscale.services.grainGrowth.insertMode;

import multiscale.enums.StateEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.models.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class InsertModeHelper {
    private static Random random = new Random();
    private static boolean[][] statesMap;
    private static boolean test = false;

    public static void applyRandomInsertMode(Grid grid, int grainNumber) {
        Cell[][] localGrid = grid.getGrid();
        boolean getRandomIndexes;
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

    public static String applyHomogeneousInsertMode(Grid grid, int rowGrainNumber, int columnGrainNumber) {
        int rowsDelta = grid.getHeight() / rowGrainNumber;
        int columnDelta = grid.getWidth() / columnGrainNumber;

        if (rowsDelta < 1) {
            return String.format("Zbyt mała siatka na %d ziaren w pionie", rowGrainNumber);
        }
        if (columnDelta < 1) {
            return String.format("Zbyt mała siatka na %d ziaren w poziomie", columnGrainNumber);
        }
        for (int i = 0; i < grid.getHeight(); i += rowsDelta) {
            for (int j = 0; j < grid.getWidth(); j += columnDelta) {
                grid.getGrid()[i][j].setAutoState();
            }
        }
        return null;
    }
}
