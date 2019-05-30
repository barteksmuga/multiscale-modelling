package multiscale.services;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import multiscale.enums.grainGrowth.BoundaryConditionEnum;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Grid;
import multiscale.services.boundaryConditions.AbsorbingBoundaryCondition;
import multiscale.services.boundaryConditions.BoundaryCondition;
import multiscale.services.boundaryConditions.PeriodicalBoundaryCondition;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;
import multiscale.services.grainGrowth.neighbourhoodStrategies.moore.MooreNeighbourhoodStrategy;
import multiscale.services.grainGrowth.neighbourhoodStrategies.pentagonal.PentagonalNeighbourhoodStrategy;
import multiscale.services.grainGrowth.neighbourhoodStrategies.vonNeumann.VonNeumannNeighbourhoodStrategy;

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
//                return
            case PENTAGONAL:
                return new PentagonalNeighbourhoodStrategy(grid, boundaryCondition);
            default:
                return new VonNeumannNeighbourhoodStrategy(grid, boundaryCondition);
        }
    }

    private void initializeTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(INTERVAL), event -> {
            nextStep();
        }));
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

    protected abstract void nextStep();

    protected void appendToGrid() {
        gridPaneService.drawArrayOnGridPane(gridPane, grid);
    }

    protected int getX(int x) {
        return boundaryCondition.getX(x);
    }

    protected int getY(int y) {
        return boundaryCondition.getY(y);
    }
}
