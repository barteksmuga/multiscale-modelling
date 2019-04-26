package multiscale.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Cell {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private final Integer id;
    private int state;
    private String color;

    public Cell() {
        this.id = idGenerator.getAndIncrement();
        setState(0);
    }

    public Cell(short state) {
        this.id = idGenerator.getAndIncrement();
        setState(state);
    }

    public Integer getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        this.color = state == 0 ? "czerwony" : "zółty";
    }

    public String getColor() {
        return color;
    }
}
