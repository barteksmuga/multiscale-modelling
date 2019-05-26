package multiscale.enums;

public enum StateEnum {

    NOT_SET(-1),
    ACTIVE(1),
    INACTIVE(0);

    StateEnum(int stateValue) {
        this.stateValue = stateValue;
    }

    private int stateValue;

    public int getStateValue() {
        return this.stateValue;
    }
}
