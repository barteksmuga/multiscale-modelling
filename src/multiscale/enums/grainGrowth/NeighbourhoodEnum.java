package multiscale.enums.grainGrowth;

import java.util.ArrayList;
import java.util.List;

public enum NeighbourhoodEnum {
    VON_NEUMANN("von Neumanna"),
    MOORE("Moore'a"),
    PENTAGONAL("Pentagonalne"),
    HEXAGONAL("Heksagonalne"),
    RADIUS("Promień");

    private String name;

    public String getName() {
        return name;
    }

    NeighbourhoodEnum(String name) {
        this.name = name;
    }

    public static NeighbourhoodEnum get(String value) {
        for(var tmp : values()) {
            if (tmp.getName().equals(value)) {
                return tmp;
            }
        }
        return null;
    }

    public static List<String> getNames() {
        var names = new ArrayList<String>();
        for (var rule : values()) {
            names.add(rule.getName());
        }
        return names;
    }
}
