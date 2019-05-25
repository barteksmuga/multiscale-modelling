package multiscale.services;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class PaintHelper {
    private static Random random = new Random();

    private static List<Color> basicColors = initBasicColors();
    private static Map<Integer, Color> colorMap = initMapWithBasicColors();
    private static Set<Color> usedColors = initSetWithBasicColors();

    private static Map<Integer, Color> initMapWithBasicColors() {
        var map = new HashMap<Integer, Color>();
        for (int i = 0; i < basicColors.size(); ++i) {
            map.put(i, basicColors.get(i));
        }
        return map;
    }

    public static Color getColorForState(int state) {
        return colorMap.containsKey(state) ? colorMap.get(state) : addColorForState(state);
    }

    public static Color addColorForState(int state) {
        if (colorMap.containsKey(state)) {
            return colorMap.get(state);
        }
        Color newColor = getNewColor();
        colorMap.put(state, newColor);
        return newColor;
    }

    private static Color getNewColor() {
        Color color;
        do {
            float red = random.nextFloat();
            float green = random.nextFloat();
            float blue = random.nextFloat();
            color = new Color(red, green, blue, 1);
        } while (!usedColors.add(color));
        return color;
    }

    private static Set<Color> initSetWithBasicColors() {
        return new HashSet<>(basicColors);
    }

    private static List<Color> initBasicColors() {
        var list = new ArrayList<Color>();
        list.add(Color.GREEN);
        list.add(Color.RED);
        return list;
    }
}
