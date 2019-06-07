package multiscale.processors.monteCarlo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import multiscale.enums.grainGrowth.BoundaryConditionEnum;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.models.Point;
import multiscale.processors.drx.DrxProcessor;
import multiscale.services.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MonteCarloProcessor extends Service {
    private Map<Integer, Cell> processedCellMap;
    private MonteCarloDTO dto;
    private List<Point> cellsOnGrainBorders;

    private Timeline timeline;
    private static int count = 0;

    public MonteCarloProcessor(Grid grid, GridPane gridPane, NeighbourhoodEnum neighbourhoodEnum,
                               BoundaryConditionEnum boundaryConditionEnum, MonteCarloDTO dto) {
        super(grid, gridPane, neighbourhoodEnum, boundaryConditionEnum);
        this.processedCellMap = new HashMap<>();
        this.dto = dto;
        initializeTimeline(neighbourhoodEnum, boundaryConditionEnum);
        this.cellsOnGrainBorders = new LinkedList<>();
    }

    private void initializeTimeline(NeighbourhoodEnum neighbourhoodEnum, BoundaryConditionEnum boundaryConditionEnum) {
        timeline = new Timeline(new KeyFrame(Duration.millis(500), actionEvent -> nextStep()));
        timeline.setCycleCount(dto.getIteration());

        final var drxProcessor = new DrxProcessor(grid, gridPane, neighbourhoodEnum, boundaryConditionEnum);

        timeline.setOnFinished(event -> {
            drxProcessor.process();
        });
    }

    public void process() {
        timeline.play();
    }

    @Override
    public void nextStep() {
        System.out.println("processIteration : " + ++count);
        initCellOnGrainBorderList();
        int size = cellsOnGrainBorders.size();
        while (processedCellMap.size() < cellsOnGrainBorders.size()) {
            Cell current = getRandomCell();
            Cell[][] neighbours = neighbourhoodStrategy.getNeighbourMap(current);

            double currentCellEnergy = calculateEnergy(current, neighbours);
            if (currentCellEnergy == 0) {
                cellsOnGrainBorders.remove(current.getPoint());
                continue;
            }
            Cell neighbour = getRandomNeighbour(current, neighbours);
            double neighbourEnergy = calculateEnergy(neighbour, neighbours);

            var deltaEnergy = neighbourEnergy - currentCellEnergy;
            var kt = getKtValue();
            var probability = deltaEnergy <= 0 ? 1.0 : Math.exp(-deltaEnergy / kt);
            var shot = getRandomDouble(0.0, 1.0);

            current.setEnergy(currentCellEnergy);
            if (deltaEnergy < 0) {
                current.setState(neighbour.getState());
                current.setEnergy(neighbourEnergy);
                appendToGrid();
            }
        }
        processedCellMap.clear();

    }

    private void initCellOnGrainBorderList() {
        for (Cell[] row: grid.getGrid()) {
            for (Cell cell: row) {
                if (isOnGrainBound(cell)) {
                    cellsOnGrainBorders.add(cell.getPoint());
                }
            }
        }
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

    private double calculateEnergy(Cell current, Cell[][] neighbours) {
        double energy = 0;
        for (Cell[] row: neighbours) {
            for (Cell cell: row) {
                if (cell != null && current.getState() != cell.getState()) {
                    energy++;
                }
            }
        }
        return energy;
    }

    private Cell getRandomCell() {
        boolean isProcessed = false;
        Random random = new Random();
        Cell cell;
        do {
            int index = random.nextInt(cellsOnGrainBorders.size());
            Point point = cellsOnGrainBorders.get(index);
            cell = grid.getGrid()[point.y][point.x];
            isProcessed = addCellToMap(cell);
        } while (!isProcessed);
        cellsOnGrainBorders.remove(cell.getPoint());
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
