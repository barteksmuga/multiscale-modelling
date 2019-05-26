package multiscale.enums.grainGrowth;

import java.util.ArrayList;
import java.util.List;

public enum BoundaryConditionEnum {
    PERIODICAL("Periodyczne"),
    ABSORBING("Absorbujące");

    private String name;

    BoundaryConditionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static BoundaryConditionEnum get(String value) {
        for(var tmp : values()) {
            if (tmp.getName().equals(value)) {
                return tmp;
            }
        }
        return null;
    }

    public static List<String> getNames() {
        var names = new ArrayList<String>();
        for (var condition : values()) {
            names.add(condition.getName());
        }
        return names;
    }
}
