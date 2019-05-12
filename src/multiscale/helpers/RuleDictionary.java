package multiscale.helpers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import multiscale.enums.RuleEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleDictionary {

    private final static Map<Integer, List<Integer>> map = initializeMap();

    public static ObservableList<String> getElementaryMachineRuleList() {
        return FXCollections.observableArrayList(RuleEnum.getNames());
    }

    public static List<Integer> getRule (int index) {
        return map.get(index);
    }

    private static Map<Integer, List<Integer>> initializeMap() {
        Map<Integer, List<Integer>> map = new HashMap<>();
        map.put(30, createRule30());
        map.put(60, createRule60());
        map.put(90, createRule90());
        map.put(120, createRule120());
        map.put(225, createRule225());
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
