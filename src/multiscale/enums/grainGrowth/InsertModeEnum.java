package multiscale.enums.grainGrowth;

import java.util.ArrayList;
import java.util.List;

public enum InsertModeEnum {
    RANDOM_WITH_RADIUS("Losowy z promieniem"),
    RANDOM("Losowy"),
    CUSTOM("Ręczny"),
    HOMOGENEOUS("Jednorodny");

    private String name;

    public String getName() {
        return name;
    }

    InsertModeEnum(String name) {
        this.name = name;
    }

    public static InsertModeEnum get(String value) {
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
