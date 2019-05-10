package multiscale.helpers;

import multiscale.enums.InitialConditionEnum;
import multiscale.model.GridProperties;
import multiscale.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InitialConditionDictionary {
    private static Random random = new Random();

    public static List<Point> getCondition(InitialConditionEnum key, GridProperties gridProperties) {
        return createChosenCondition(key, gridProperties);
    }

    private static List<Point> createChosenCondition(InitialConditionEnum key, GridProperties gridProperties) {
        int x = random.nextInt(gridProperties.getGridWidth());
        int y = random.nextInt(gridProperties.getGridHeight());
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
        List<Point> list = new ArrayList<>();
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
        List<Point> list = new ArrayList<>();
        list.add(new Point(3, 6));
        list.add(new Point(3 + 1, 6));
        list.add(new Point(3 + 1, 6 + 1));
        list.add(new Point(3 + 2, 6 - 1));
        return list;
    }

    private static List<Point> createRandomCondition(int x, int y) {
        List<Point> list = new ArrayList<>();
        return list;
    }

    private static List<Point> createOscillatorCondition(int x, int y) {
        List<Point> list = new ArrayList<>();
        list.add(new Point(x, y - 1));
        list.add(new Point(x, y));
        list.add(new Point(x, y + 1));
        return list;
    }

}
