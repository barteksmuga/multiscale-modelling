package multiscale.constants.enums;

public enum StateEnum {

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
