package multiscale.services;

import javafx.scene.layout.GridPane;
import multiscale.models.Grid;

public abstract class Service {
    protected Grid grid;
    protected GridPane gridPane;
    protected GridPaneService gridPaneService;

    public Service(Grid grid, GridPane gridPane) {
        this.grid = grid;
        this.gridPane = gridPane;
        this.gridPaneService = new GridPaneService();
    }

    public abstract void run() throws InterruptedException;

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
