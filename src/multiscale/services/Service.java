package multiscale.services;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import multiscale.enums.grainGrowth.BoundaryConditionEnum;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.boundaryConditions.AbsorbingBoundaryCondition;
import multiscale.services.boundaryConditions.BoundaryCondition;
import multiscale.services.boundaryConditions.PeriodicalBoundaryCondition;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;
import multiscale.services.grainGrowth.neighbourhoodStrategies.hexagonal.HexagonalNeighbourhoodStrategy;
import multiscale.services.grainGrowth.neighbourhoodStrategies.moore.MooreNeighbourhoodStrategy;
import multiscale.services.grainGrowth.neighbourhoodStrategies.pentagonal.PentagonalNeighbourhoodStrategy;
import multiscale.services.grainGrowth.neighbourhoodStrategies.vonNeumann.VonNeumannNeighbourhoodStrategy;

import java.util.Random;

import static multiscale.constants.WindowConstants.INTERVAL;

public abstract class Service {
    protected Grid grid;
    protected GridPane gridPane;
    protected GridPaneService gridPaneService;
    protected Timeline timeline;
    protected NeighbourhoodStrategy neighbourhoodStrategy;
    protected BoundaryCondition boundaryCondition;

    public Service(Grid grid, GridPane gridPane, NeighbourhoodEnum neighbourhoodEnum,
                   BoundaryConditionEnum boundaryConditionEnum) {
        this.grid = grid;
        this.gridPane = gridPane;
        this.gridPaneService = new GridPaneService();

        initializeBoundaryCondition(boundaryConditionEnum);
        initializeNeighbourhood(neighbourhoodEnum);
        initializeTimeline();
    }

    private void initializeBoundaryCondition(BoundaryConditionEnum boundaryConditionEnum) {
        if (boundaryConditionEnum != null) {
            this.boundaryCondition = getBoundaryConditionInstance(boundaryConditionEnum);
        }
    }

    private BoundaryCondition getBoundaryConditionInstance(BoundaryConditionEnum boundaryConditionEnum) {
        switch (boundaryConditionEnum) {
            case PERIODICAL:
                return new PeriodicalBoundaryCondition(grid.getWidth(), grid.getHeight());
            case ABSORBING:
                return new AbsorbingBoundaryCondition(grid.getWidth(), grid.getHeight());
            default:
                return new PeriodicalBoundaryCondition(grid.getWidth(), grid.getHeight());
        }
    }

    private void initializeNeighbourhood(NeighbourhoodEnum neighbourhoodEnum) {
        if (neighbourhoodEnum != null) {
            this.neighbourhoodStrategy = getNeighbourhoodStrategyInstance(neighbourhoodEnum);
        }
    }

    private NeighbourhoodStrategy getNeighbourhoodStrategyInstance(NeighbourhoodEnum neighbourhoodEnum) {
        switch (neighbourhoodEnum) {
            case VON_NEUMANN:
                return new VonNeumannNeighbourhoodStrategy(grid, boundaryCondition);
            case MOORE:
                return new MooreNeighbourhoodStrategy(grid, boundaryCondition);
            case RADIUS:
//                return
            case HEXAGONAL:
            case HEXAGONAL_LEFT:
            case HEXAGONAL_RIGHT:
                return getHexagonalNeighbourhoodStrategy(neighbourhoodEnum);
            case PENTAGONAL:
                return new PentagonalNeighbourhoodStrategy(grid, boundaryCondition);
            default:
                return new VonNeumannNeighbourhoodStrategy(grid, boundaryCondition);
        }
    }

    private NeighbourhoodStrategy getHexagonalNeighbourhoodStrategy(NeighbourhoodEnum neighbourhoodEnum) {
        boolean isLeft = neighbourhoodEnum.equals(NeighbourhoodEnum.HEXAGONAL_LEFT);
        boolean isRight = neighbourhoodEnum.equals(NeighbourhoodEnum.HEXAGONAL_RIGHT);
        boolean isRandom = neighbourhoodEnum.equals(NeighbourhoodEnum.HEXAGONAL);

        return new HexagonalNeighbourhoodStrategy(grid, boundaryCondition, isLeft, isRight, isRandom);
    }

    private void initializeTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(INTERVAL), event -> nextStep()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void run() {
        System.out.println("timeline.play()");
        timeline.play();
    }

    public void stop() {
        System.out.println("timeline.stop()");
        timeline.stop();
    }

    public Animation.Status getTimelineStatus() {
        return timeline.getStatus();
    }

    protected abstract void nextStep();

    protected void appendToGrid() {
        System.err.println("grid update");
        gridPaneService.drawArrayOnGridPane(gridPane, grid);
    }

    protected int getX(int x) {
        return boundaryCondition.getX(x);
    }

    protected int getY(int y) {
        return boundaryCondition.getY(y);
    }


    protected boolean isOnGrainBound(Cell currentCell) {
        Cell[][] neighbours = neighbourhoodStrategy.getNeighbourMap(currentCell);
        for (Cell[] row: neighbours) {
            for (Cell cell: row) {
                if (cell != null && cell.getState() != currentCell.getState()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected double getRandomDouble(double min, double max) {
        return min + (max - min) * new Random().nextDouble();
    }
}
