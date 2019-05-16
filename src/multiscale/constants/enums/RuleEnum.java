package multiscale.constants.enums;

import java.util.ArrayList;
import java.util.List;

public enum RuleEnum {
    RULE_30("30"),
    RULE_60("60"),
    RULE_90("90"),
    RULE_120("120"),
    RULE_225("225");

    private String name;

    RuleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static RuleEnum get(String value) {
        for(RuleEnum tmp : values()) {
            if (tmp.getName().equals(value)) {
                return tmp;
            }
        }
        return null;
    }

    public static List<String> getNames() {
        var names = new ArrayList<String>();
        for (RuleEnum rule : values()) {
            names.add(rule.getName());
        }
        return names;
    }
}
