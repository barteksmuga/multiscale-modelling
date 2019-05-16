package multiscale.models;

import javafx.scene.shape.Rectangle;
import multiscale.constants.enums.StateEnum;

import java.util.Arrays;
import java.util.List;

import static multiscale.constants.WindowConstants.DRAW_GRID_AREA_HEIGHT;
import static multiscale.constants.WindowConstants.DRAW_GRID_AREA_WIDTH;
import static multiscale.constants.WindowConstants.MAX_CELL_SIZE;
import static multiscale.constants.WindowConstants.MIN_CELL_SIZE;

public class Grid {
    private int width;
    private int height;
    private Cell[][] grid;
    private int ruleIndicator;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        initializeGrid();
    }

    public int getRuleIndicator() {
        return ruleIndicator;
    }

    public void setRuleIndicator(int ruleIndicator) {
        this.ruleIndicator = ruleIndicator;
    }

    public void setFirstRow(List<Cell> firstRow) {
        grid[0] = firstRow.toArray(new Cell[0]);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    private void initializeGrid() {
        grid = new Cell[height][width];
        for (int i=0; i<height; ++i) {
            for (int j=0; j<width; ++j) {
                grid[i][j] = buildCell(StateEnum.INACTIVE.getStateValue());
            }
        }
    }

    private Cell buildCell(int state) {
        return new Cell.Builder()
                .withState(state)
                .withRectangle(buildRectangle())
                .build();
    }

    private Rectangle buildRectangle() {
        double cellSize = calculateCellSize(width, DRAW_GRID_AREA_WIDTH);
        return new Rectangle(cellSize, cellSize);
    }

    private double calculateCellSize(int gridDimSize, double areaDimSize) {
        double result = areaDimSize / gridDimSize;
        return tooSmall(result) ? MIN_CELL_SIZE : (tooBig(result) ? MAX_CELL_SIZE : result);
    }

    private boolean tooSmall(double result) {
        return result < MIN_CELL_SIZE;
    }

    private boolean tooBig(double result) {
        return result > MAX_CELL_SIZE;
    }


}
