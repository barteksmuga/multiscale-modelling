package multiscale.services.grainGrowth.neighbourhoodStrategies;

import multiscale.enums.StateEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
import multiscale.models.Point;
import multiscale.services.boundaryConditions.BoundaryCondition;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class NeighbourhoodStrategy {
    protected Grid grid;
    protected Map<Integer, Integer> neighbourMap;
    protected BoundaryCondition boundaryCondition;
    protected Map<Integer, int[][]> neighbourListMap;

    public NeighbourhoodStrategy(Grid grid, BoundaryCondition boundaryCondition) {
        this.grid = grid;
        neighbourMap = new HashMap<>();
        neighbourListMap = new HashMap<>();
        this.boundaryCondition = boundaryCondition;
    }

    public int mostFrequentNeighbourState(int x, int y, Cell[][] localGrid) {
        countNeighbourStates(x, y, localGrid);
        var optionalEntry = neighbourMap.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));
        var max = optionalEntry.isPresent() ? optionalEntry.get().getValue() : 0;

        var entryKeyList = neighbourMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == max)
                .filter(entry -> entry.getKey() != StateEnum.NOT_SET.getStateValue())
                .collect(Collectors.toList());

        clearMap();
        collectNeighbours(x, y);
        return entryKeyList.size() > 0 ? getRandomState(entryKeyList) : StateEnum.NOT_SET.getStateValue();
    }

    private int getRandomState(List<Map.Entry<Integer, Integer>> list) {
        var random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index).getKey();
    }

    public abstract void countNeighbourStates(int x, int y, Cell[][] localGrid);

    public Cell[][] getNeighbourMap(Cell cell) {
        if (!neighbourListMap.containsKey(cell.getcId())) {
            Point cellPoint = cell.getPoint();
            collectNeighbours(cellPoint.x, cellPoint.y);
        }
        int[][] neighbourIds = neighbourListMap.get(cell.getcId());
        Cell[][] cells = new Cell[neighbourIds.length][neighbourIds[0].length];

        for (int i = 0; i < neighbourIds.length; ++i) {
            for (int j = 0; j < neighbourIds[0].length; ++j) {
                if (neighbourIds[i][j] != -1) {
                    cells[i][j] = grid.getCellByCId(neighbourIds[i][j]);
                } else {
                    cells[i][j] = null;
                }
            }
        }

        return cells;
    }

    public void prepareNeighbourhoodMap() {
        for (int y = 0; y < grid.getHeight(); ++y) {
            for (int x = 0; x < grid.getWidth(); ++x) {
                collectNeighbours(x, y);
            }
        }
    }

    protected abstract void collectNeighbours(int x, int y);

    protected void addToNeighbourMap(int cId, int[][] neighbours) {
        neighbourListMap.putIfAbsent(cId, neighbours);
    }

    protected void addToMap(int state) {
        if (state != StateEnum.NOT_SET.getStateValue()) {
            int count = getStateCount(state);
            neighbourMap.put(state, ++count);
        }
    }

    protected int getStateCount(int state) {
        return neighbourMap.getOrDefault(state, 0);
    }

    private void clearMap() {
        neighbourMap.clear();
    }

    protected int getX(int x) {
        return boundaryCondition.getX(x);
    }

    protected int getY(int y) {
        return boundaryCondition.getY(y);
    }
}
