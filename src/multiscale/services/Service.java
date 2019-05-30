package multiscale.services;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Grid;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;
import multiscale.services.grainGrowth.neighbourhoodStrategies.moore.MooreNeighbourhoodStrategy;
import multiscale.services.grainGrowth.neighbourhoodStrategies.vonNeumann.VonNeumannNeighbourhoodStrategy;

import static multiscale.constants.WindowConstants.INTERVAL;
import static multiscale.enums.grainGrowth.NeighbourhoodEnum.HEXAGONAL;
import static multiscale.enums.grainGrowth.NeighbourhoodEnum.MOORE;
import static multiscale.enums.grainGrowth.NeighbourhoodEnum.PENTAGONAL;
import static multiscale.enums.grainGrowth.NeighbourhoodEnum.RADIUS;
import static multiscale.enums.grainGrowth.NeighbourhoodEnum.VON_NEUMANN;

public abstract class Service {
    protected Grid grid;
    protected GridPane gridPane;
    protected GridPaneService gridPaneService;
    protected Timeline timeline;
    protected NeighbourhoodStrategy neighbourhoodStrategy;

    public Service(Grid grid, GridPane gridPane, NeighbourhoodEnum neighbourhoodEnum) {
        this.grid = grid;
        this.gridPane = gridPane;
        this.gridPaneService = new GridPaneService();

        initializeNeighbourhood(neighbourhoodEnum);
        initializeTimeline();
    }

    private void initializeNeighbourhood(NeighbourhoodEnum neighbourhoodEnum) {
        if (neighbourhoodEnum != null) {
            this.neighbourhoodStrategy = getNeighbourhoodStrategyInstance(neighbourhoodEnum);
        }
    }

    private NeighbourhoodStrategy getNeighbourhoodStrategyInstance(NeighbourhoodEnum neighbourhoodEnum) {
        switch (neighbourhoodEnum) {
            case VON_NEUMANN:
                return new VonNeumannNeighbourhoodStrategy(grid);
            case MOORE:
                return new MooreNeighbourhoodStrategy(grid);
            case RADIUS:
//                return
            case HEXAGONAL:
//                return
            case PENTAGONAL:
//                return
            default:
                return new VonNeumannNeighbourhoodStrategy(grid);
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

    protected int getCorrectPreviousX(int previousX, int maxValue) {
        if (previousX < 0) {
            previousX = maxValue;
        }
        return previousX;
    }

    protected int getCorrectFollowingX(int followingX, int maxValue) {
        if (followingX > maxValue) {
            followingX = 0;
        }
        return followingX;
    }
}
