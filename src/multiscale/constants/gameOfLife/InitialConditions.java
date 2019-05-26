package multiscale.constants.gameOfLife;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import multiscale.enums.gameOfLife.InitialConditionEnum;
import multiscale.models.Grid;
import multiscale.models.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InitialConditions {
    private static Random random = new Random();

    public static ObservableList<String> getGameOfLifeInitialConditionList() {
        return FXCollections.observableArrayList(InitialConditionEnum.getNames());
    }

    public static List<Point> getCondition(InitialConditionEnum key, Grid grid) {
        return createChosenCondition(key, grid);
    }

    private static List<Point> createChosenCondition(InitialConditionEnum key, Grid grid) {
        int x = random.nextInt(grid.getWidth());
        int y = random.nextInt(grid.getHeight());
        switch (key) {
            case FIXED:
                return createFixedCondition(x, y);
            case GLIDER:
                return createGliderCondition(x, y);
            case RANDOM:
                return createRandomCondition(x, y);
            case OSCILLATOR:
                return createOscillatorCondition(x, y);
            default:
                return new ArrayList<>();
        }
    }

    private static List<Point> createFixedCondition(int x, int y) {
        var list = new ArrayList<Point>();
//        list.add(new Point(x, y));
//        list.add(new Point(x + 1, y + 1));
//        list.add(new Point(x + 2, y + 1));
//        list.add(new Point(x + 3, y));
//        list.add(new Point(x + 2, y - 1));
//        list.add(new Point(x + 1, y - 1));

        list.add(new Point(3, 6));
        list.add(new Point(3 + 1, 6 + 1));
        list.add(new Point(3 + 2, 6 + 1));
        list.add(new Point(3 + 3, 6));
        list.add(new Point(3 + 2, 6 - 1));
        list.add(new Point(3 + 1, 6 - 1));

        return list;
    }

    private static List<Point> createGliderCondition(int x, int y) {
        var list = new ArrayList<Point>();
        list.add(new Point(3, 6));
        list.add(new Point(3 + 1, 6));
        list.add(new Point(3 + 1, 6 + 1));
        list.add(new Point(3 + 2, 6 - 1));
        return list;
    }

    private static List<Point> createRandomCondition(int x, int y) {
        var list = new ArrayList<Point>();
        return list;
    }

    private static List<Point> createOscillatorCondition(int x, int y) {
        var list = new ArrayList<Point>();
        list.add(new Point(x, y - 1));
        list.add(new Point(x, y));
        list.add(new Point(x, y + 1));
        return list;
    }
}
