package multiscale.services.grainGrowth.insertMode;

import multiscale.enums.StateEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;

import java.util.Random;

import static java.lang.Math.floor;

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

    public static void applyHomogeneousInsertMode(Grid grid, int rowGrainNumber, int columnGrainNumber) {
        int rowsDelta = grid.getHeight() / rowGrainNumber;
        int columnDelta = grid.getWidth() / columnGrainNumber;

        if (rowsDelta < 1) {
            System.err.println(String.format("Grid is to small for %d grains in column", rowGrainNumber));
            return;
        }
        if (columnDelta < 1) {
            System.err.println(String.format("Grid is to small for %d grains in row", columnGrainNumber));
            return;
        }
        for(int i=0; i<grid.getHeight(); i += rowsDelta) {
            for (int j=0; j<grid.getWidth(); j += columnDelta) {
                grid.getGrid()[i][j].setAutoState();
            }
        }
    }
}
