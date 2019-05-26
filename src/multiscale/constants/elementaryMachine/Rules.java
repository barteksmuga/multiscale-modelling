package multiscale.constants.elementaryMachine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import multiscale.enums.elementaryMachine.RuleEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rules {
    private final static Map<Integer, List<Integer>> map = initializeMap();

    public static ObservableList<String> getElementaryMachineRuleList() {
        return FXCollections.observableArrayList(RuleEnum.getNames());
    }

    public static List<Integer> getRule (int index) {
        return map.get(index);
    }

    private static Map<Integer, List<Integer>> initializeMap() {
        var map = new HashMap<Integer, List<Integer>>();
        map.put(30, createRule30());
        map.put(60, createRule60());
        map.put(90, createRule90());
        map.put(120, createRule120());
        map.put(225, createRule225());
        return map;
    }

    private static List<Integer> createRule30() {
        var list = new ArrayList<Integer>();
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
        var list = new ArrayList<Integer>();
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
        var list = new ArrayList<Integer>();
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
        var list = new ArrayList<Integer>();
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
        var list = new ArrayList<Integer>();
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
