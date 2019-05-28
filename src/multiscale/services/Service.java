package multiscale.services;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import multiscale.models.Grid;

import static multiscale.constants.WindowConstants.INTERVAL;

public abstract class Service {
    protected Grid grid;
    protected GridPane gridPane;
    protected GridPaneService gridPaneService;
    protected Timeline timeline;

    public Service(Grid grid, GridPane gridPane) {
        this.grid = grid;
        this.gridPane = gridPane;
        this.gridPaneService = new GridPaneService();

        timeline = new Timeline(new KeyFrame(Duration.millis(INTERVAL), event -> {
            nextStep();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void run() {
        timeline.play();
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
