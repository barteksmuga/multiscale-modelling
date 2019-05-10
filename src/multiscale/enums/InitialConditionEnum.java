package multiscale.enums;

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
        for(InitialConditionEnum tmp : values()) {
            if (tmp.getName().equals(value)) {
                return tmp;
            }
        }
        return null;
    }
}
