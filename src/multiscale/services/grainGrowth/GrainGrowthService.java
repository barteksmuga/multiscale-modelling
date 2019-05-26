package multiscale.services.grainGrowth;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.Service;
import multiscale.services.grainGrowth.neighbourhoodStrategies.NeighbourhoodStrategy;
import multiscale.services.grainGrowth.neighbourhoodStrategies.vonNeumann.VonNeumannNeighbourhoodStrategy;

public class GrainGrowthService extends Service {

    private Timeline timeline;

    public GrainGrowthService(Grid grid, GridPane gridPane) {
        super(grid, gridPane);
        timeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            nextStep();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    @Override
    public void run() {
        timeline.play();
    }

    private void nextStep() {
        System.out.println("nextStep");
        NeighbourhoodStrategy neighbourhoodStrategy = new VonNeumannNeighbourhoodStrategy(grid);
        Cell[][] localGrid = copyGrid();
        for (int y = 0; y < grid.getHeight(); ++y) {
            for (int x = 0; x < grid.getWidth(); ++x) {
                Cell cell = grid.getGrid()[y][x];
                if (cell.getState() == -1) {
                    int mostFrequentNeighbourState = neighbourhoodStrategy.mostFrequentNeighbourState(x, y, localGrid);
                    cell.setState(mostFrequentNeighbourState);
                }
            }
        }
        appendToGrid();
        if (!isAnyEmptyCellLeft()) {
            timeline.stop();
            System.out.println("timeline.stop()");
        }
    }

    private boolean isAnyEmptyCellLeft() {
        boolean isAnyEmpty = false;
        for (Cell[] row : grid.getGrid()) {
            for (Cell cell : row) {
                if (cell.getState() == -1) {
                    isAnyEmpty = true;
                    break;
                }
            }
        }
        return isAnyEmpty;
    }

    private Cell[][] copyGrid() {
        Cell[][] copy = new Cell[grid.getHeight()][grid.getWidth()];
        for (int i=0; i<grid.getHeight(); ++i) {
            for (int j=0; j<grid.getWidth(); ++j) {
                copy[i][j] = new Cell(grid.getGrid()[i][j]);
            }
        }
        return copy;
    }
}
