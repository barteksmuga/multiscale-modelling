package multiscale.processors.monteCarlo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import multiscale.enums.grainGrowth.BoundaryConditionEnum;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static multiscale.constants.WindowConstants.INTERVAL;

public class MonteCarloProcessor extends Service {
    private Map<Integer, Cell> processedCellMap;
    private MonteCarloDTO dto;

    private Timeline timeline;

    public MonteCarloProcessor(Grid grid, GridPane gridPane, NeighbourhoodEnum neighbourhoodEnum,
                               BoundaryConditionEnum boundaryConditionEnum, MonteCarloDTO dto) {
        super(grid, gridPane, neighbourhoodEnum, boundaryConditionEnum);
        this.processedCellMap = new HashMap<>();
        this.dto = dto;
        initializeTimeline();
    }

    private void initializeTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(50), actionEvent -> nextStep()));
        timeline.setCycleCount(dto.getIteration());
    }

    public void process() {
        timeline.play();
    }


    @Override
    public void nextStep() {
        System.out.println("processIteration");
        Cell current = getRandomCell();
        Cell[][] neighbours = neighbourhoodStrategy.getNeighbourMap(current);

        double currentCellEnergy = 0;
        calculateEnergy(current, neighbours, currentCellEnergy);

        Cell neighbour = getRandomNeighbour(current, neighbours);
        double neighbourEnergy = 0;
        calculateEnergy(neighbour, neighbours, neighbourEnergy);

        var deltaEnergy = neighbourEnergy - currentCellEnergy;
        var kt = getKtValue();
        var probability = deltaEnergy <= 0 ? 1.0 : Math.exp(-deltaEnergy / kt);
        var shot = getRandomDouble(0.0, 1.0);

        if (shot <= probability) {
            grid.getGrid()[current.getPoint().y][current.getPoint().x].setState(neighbour.getState());
            appendToGrid();
        }

    }

    private double getRandomDouble(double min, double max) {
        return min + (max - min) * new Random().nextDouble();
    }

    private double getKtValue() {
        return getRandomDouble(0.1, 6.0);
    }

    private Cell getRandomNeighbour(Cell current, Cell[][] neighbours) {
        Random random = new Random();
        Cell anyNeighbour;
        do {
            int y = random.nextInt(neighbours.length);
            int x = random.nextInt(neighbours[0].length);

            anyNeighbour = neighbours[y][x];
        } while (anyNeighbour == null);

        return anyNeighbour;
    }

    private void calculateEnergy(Cell current, Cell[][] neighbours, double energy) {
        for(Cell[] row : neighbours) {
            for (Cell cell : row) {
                if (cell != null && current.getState() != cell.getState()) {
                    energy++;
                }
            }
        }
    }

    private Cell getRandomCell() {
        boolean isProcessed = false;
        Random random = new Random();
        Cell cell;
        do {
            int x = random.nextInt(grid.getWidth());
            int y = random.nextInt(grid.getHeight());
            cell = grid.getGrid()[y][x];
            isProcessed = addCellToMap(cell);
        } while (!isProcessed);
        return cell;
    }

    private boolean addCellToMap(Cell cell) {
        if (!processedCellMap.containsKey(cell.getcId())) {
            processedCellMap.put(cell.getcId(), cell);
            return true;
        }
        return false;
    }
}
