package multiscale.processors.drx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import multiscale.enums.grainGrowth.BoundaryConditionEnum;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.services.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.exp;

public class DrxProcessor extends Service {
    private Timeline timeline;
    private static int count = 0;

    //todo: move this to UI
    private double A = 86710969050178.5;
    private double B = 9.41268203527779;
    private double dt = 0.001;
    private double t = 0.0;
    private double previousRo = 0.0;
    private double percent = 0.3;
    private double randomPackPercent = 0.001;
    private double criticalRo = 10.42;
    private double probability = 0.8;

    public DrxProcessor(Grid grid, GridPane gridPane, NeighbourhoodEnum neighbourhoodEnum, BoundaryConditionEnum boundaryConditionEnum) {
        super(grid, gridPane, neighbourhoodEnum, boundaryConditionEnum);
        this.timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> nextStep()));
        this.timeline.setCycleCount((int) (0.02 / dt));
    }

    public void process() {
        timeline.play();
    }

    @Override
    protected void nextStep() {
        System.out.println(String.format("DrxProcessor -> nextStep: %d", ++count));
        t += dt;
        double ro = A / B + (1 - A / B) + exp(-B * t);

        double deltaRo = ro - previousRo;
        previousRo = ro;

        int gridSize = grid.getWidth() * grid.getHeight();
        double averageDislocation = deltaRo / gridSize;
        double dislocationPackForEachCell = averageDislocation * percent;
        double roCritical = criticalRo / gridSize;
        double remainingDislocationPackSize = (1 - percent) * deltaRo;
        double dislocationPackForRandomCell = remainingDislocationPackSize * randomPackPercent;

        double dislocationSum = 0;

        Cell[][] localGrid = grid.getGrid();
        var embryoList = new LinkedList<Cell>();

        for (int y = 0; y < grid.getHeight(); ++y) {
            for (int x = 0; x < grid.getWidth(); ++x) {
                Cell currentCell = localGrid[y][x];
                currentCell.addDislocationDensity(dislocationPackForEachCell);
                dislocationSum += dislocationPackForEachCell;
                if (isEmbryo(currentCell, roCritical)) {
                    embryoList.add(currentCell);
                }
            }
        }

        nucleationProcess(roCritical, remainingDislocationPackSize, dislocationPackForRandomCell, embryoList, dislocationSum);

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("densitySum", true));
            bufferedWriter.append(String.format("t: %f \tdensity: \t %f", t, dislocationSum));
            bufferedWriter.append("\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int y=0; y<grid.getHeight(); ++y) {
            for (int x=0; x<grid.getWidth(); ++x) {
                Cell currentCell = localGrid[y][x];
                boolean isAnyNeighbourRecrystallized = isAnyNeighbourRecrystallized(currentCell);
                if (!isAnyNeighbourRecrystallized) {
                    continue;
                }
                boolean hasHighestDislocationDensity = hasHighestDislocationDensity(currentCell);
                if (!hasHighestDislocationDensity) {
                    continue;
                }
                currentCell.recrystallize();
            }
        }
    }

    private boolean hasHighestDislocationDensity(Cell cell) {
        Cell[][] neighbours = neighbourhoodStrategy.getNeighbourMap(cell);
        for (var row : neighbours) {
            for (var curr : row) {
                if (curr != null && curr.getDislocationDensity() > cell.getDislocationDensity()) {
                    return false;
                }
            }
        }
        return true;
    }

     private boolean isAnyNeighbourRecrystallized(Cell cell) {
        Cell[][] neighbours = neighbourhoodStrategy.getNeighbourMap(cell);
        for (var row : neighbours) {
            for (var curr : row) {
                if (curr != null && curr.isRecrystallized()) {
                    return true;
                }
            }
        }
        return false;
     }

    private void nucleationProcess(double roCritical, double remainingDislocationPackSize, double dislocationPackForRandomCell, LinkedList<Cell> embryoList, double dislocationSum) {
        while (remainingDislocationPackSize > 0) {
            Cell currentCell = getRandomCell();
            var shot = getRandomDouble(0.0, 1.0);
            boolean isOnBound = false;
            boolean addDensity = false;
            if (isOnGrainBound(currentCell)) {
                if (shot < probability) {
                    addDensity = true;
                    isOnBound = true;
                }
            } else {
                if (shot >= probability) {
                    addDensity = true;
                }
            }
            if (!addDensity) {
                continue;
            }

            currentCell.addDislocationDensity(dislocationPackForRandomCell);
            remainingDislocationPackSize -= dislocationPackForRandomCell;
            dislocationSum += dislocationPackForRandomCell;
            if (isOnBound && currentCell.getDislocationDensity() >= roCritical) {
                embryoList.add(currentCell);
            }
            if (remainingDislocationPackSize <= 0) {
                break;
            }
        }
        for (Cell cell : embryoList) {
            cell.recrystallize();
        }
        appendToGrid();
    }

    private boolean isEmbryo(Cell cell, double roCritical) {
        return cell.getDislocationDensity() >= roCritical && isOnGrainBound(cell);
    }

    private Cell getRandomCell() {
        Random random = new Random();
        return grid.getGrid()[random.nextInt(grid.getHeight())][random.nextInt(grid.getWidth())];
    }
}
