package multiscale.models;

import javafx.scene.shape.Rectangle;
import multiscale.enums.ModeEnum;
import multiscale.enums.StateEnum;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static multiscale.constants.WindowConstants.DRAW_GRID_AREA_HEIGHT;
import static multiscale.constants.WindowConstants.DRAW_GRID_AREA_WIDTH;
import static multiscale.constants.WindowConstants.MAX_CELL_SIZE;
import static multiscale.constants.WindowConstants.MIN_CELL_SIZE;

public class Grid {
    private int width;
    private int height;
    private Cell[][] grid;
    private int ruleIndicator;
    private ModeEnum mode;
    private int cellCount;
    private boolean isDisplayingState = true;
    private int displayMode = 0;

    public Grid(int width, int height, ModeEnum mode) {
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.cellCount = 0;
        initializeGrid();
    }

    public int getRuleIndicator() {
        return ruleIndicator;
    }

    public void setRuleIndicator(int ruleIndicator) {
        this.ruleIndicator = ruleIndicator;
    }

    public void setRandomCellActive() {
        var random = new Random();
        for (int i = 0; i < (width + height); ++i) {
            int hIndex = random.nextInt(height);
            int wIndex = random.nextInt(width);
            grid[hIndex][wIndex].setState(StateEnum.ACTIVE.getStateValue());
        }
    }

    public Cell getCellByCId(final int cId) {
        Optional<Cell> cell = Arrays.stream(grid)
                .parallel()
                .flatMap(Arrays::stream)
                .filter(current -> current.getcId() == cId)
                .findFirst();
        return cell.orElse(null);
    }

    public void revertView() {
        ++displayMode;
        if (displayMode > 2) {
            displayMode = 0;
        }
        Arrays.stream(grid)
//                .parallel()
                .flatMap(Arrays::stream)
                .forEach(cell -> cell.swapStateAndEnergy(displayMode));
        isDisplayingState = !isDisplayingState;
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

    public int getCellCount() {
        return cellCount;
    }

    private void initializeGrid() {
        grid = new Cell[height][width];
        StateEnum initialState = getInitialState();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                Point point = new Point(j, i);
                grid[i][j] = buildCell(point, initialState.getStateValue());
            }
        }
    }

    private Cell buildCell(Point point, int state) {
        ++cellCount;
        return new Cell
                .Builder()
                .withState(state)
                .withRectangle(buildRectangle())
                .withCoordinates(point)
                .withMode(mode)
                .withEnergy(0)
                .withDislocationDensity(0)
                .isRecrystallized(false)
                .build();
    }

    public StateEnum getInitialState() {
        return mode == ModeEnum.GRAIN_GROWTH ? StateEnum.NOT_SET : StateEnum.INACTIVE;
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
