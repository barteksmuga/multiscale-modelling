package multiscale.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleDictionary {

    private final static Map<Integer, List<Integer>> map = initializeMap();

    public static List<Integer> getRule (int index) {
        return map.get(index);
    }


    private static Map<Integer, List<Integer>> initializeMap() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        List<Integer> rule30 = createRule30();
        map.put(30, rule30);
        List<Integer> rule60 = createRule60();
        map.put(60, rule60);
        List<Integer> rule90 = createRule90();
        map.put(90, rule90);
        List<Integer> rule120 = createRule120();
        map.put(120, rule120);
        List<Integer> rule225 = createRule225();
        map.put(225, rule225);
        return map;
    }

    private static List<Integer> createRule30() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(0);
        list.add(0);
        list.add(0);
        return list;
    }

    private static List<Integer> createRule60() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(0);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(0);
        list.add(0);
        return list;
    }
    private static List<Integer> createRule90() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(0);
        list.add(1);
        list.add(1);
        list.add(0);
        list.add(1);
        list.add(0);
        return list;
    }
    private static List<Integer> createRule120() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(0);
        return list;
    }
    private static List<Integer> createRule225() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(1);
        list.add(1);
        list.add(1);
        return list;
    }
}
