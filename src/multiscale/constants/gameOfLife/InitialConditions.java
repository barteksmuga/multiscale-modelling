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
    private static int width;
    private static int height;

    public static ObservableList<String> getGameOfLifeInitialConditionList() {
        return FXCollections.observableArrayList(InitialConditionEnum.getNames());
    }

    public static List<Point> getCondition(InitialConditionEnum key, Grid grid) {
        width = grid.getWidth();
        height = grid.getHeight();
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
            case OSCILLATOR:
                return createOscillatorCondition(x, y);
            default:
                return new ArrayList<>();
        }
    }

    private static List<Point> createFixedCondition(int x, int y) {
        var list = new ArrayList<Point>();
        list.add(new Point(getX(x), y));
        list.add(new Point(getX(x + 1), getY(y + 1)));
        list.add(new Point(getX(x + 2), getY(y + 1)));
        list.add(new Point(getX(x + 3), getY(y)));
        list.add(new Point(getX(x + 2), getY(y - 1)));
        list.add(new Point(getX(x + 1), getY(y - 1)));
        return list;
    }

    private static List<Point> createGliderCondition(int x, int y) {
        var list = new ArrayList<Point>();
        list.add(new Point(getX(x), getY(y)));
        list.add(new Point(getX(x + 1), getY(y)));
        list.add(new Point(getX(x + 1), getY(y + 1)));
        list.add(new Point(getX(x + 2), getY(y - 1)));
        return list;
    }

    private static List<Point> createOscillatorCondition(int x, int y) {
        var list = new ArrayList<Point>();
        list.add(new Point(getX(x), getY(y - 1)));
        list.add(new Point(getX(x), getY(y)));
        list.add(new Point(getX(x), getY(y + 1)));
        return list;
    }

    private static int getX(int x) {
        return getCoordinate(x, width);
    }

    private static int getY(int y) {
        return getCoordinate(y, height);
    }

    private static int getCoordinate(int coord, int maxVal) {
        if (coord > maxVal - 1) {
            coord = 0;
        }
        if (coord < 0) {
            coord = maxVal - 1;
        }
        return coord;
    }
}
