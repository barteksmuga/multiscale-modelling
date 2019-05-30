package multiscale.services.grainGrowth.neighbourhoodStrategies;

import multiscale.enums.StateEnum;
import multiscale.models.Cell;
import multiscale.models.Grid;
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

    public NeighbourhoodStrategy(Grid grid, BoundaryCondition boundaryCondition) {
        this.grid = grid;
        neighbourMap = new HashMap<>();
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
        return entryKeyList.size() > 0 ? getRandomState(entryKeyList) : StateEnum.NOT_SET.getStateValue();
    }

    private int getRandomState(List<Map.Entry<Integer, Integer>> list) {
        var random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index).getKey();
    }

    public abstract void countNeighbourStates(int x, int y, Cell[][] localGrid);

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
