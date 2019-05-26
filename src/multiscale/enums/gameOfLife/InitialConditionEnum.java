package multiscale.enums.gameOfLife;

import java.util.ArrayList;
import java.util.List;

public enum InitialConditionEnum {
    FIXED("Niezmienny"),
    GLIDER("Glider"),
    OSCILLATOR("Oscylator"),
    CUSTOM("Ręczna definicja"),
    RANDOM("Losowy");

    private String name;

    InitialConditionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static InitialConditionEnum get(String value) {
        for (var tmp: values()) {
            if (tmp.getName().equals(value)) {
                return tmp;
            }
        }
        return null;
    }

    public static List<String> getNames() {
        var names = new ArrayList<String>();
        for (var rule: values()) {
            names.add(rule.getName());
        }
        return names;
    }
}
