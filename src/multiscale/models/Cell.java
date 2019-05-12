package multiscale.models;

import java.util.concurrent.atomic.AtomicInteger;

public class Cell {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    private final Integer id;
    private int state;

    public Cell() {
        this.id = idGenerator.getAndIncrement();
        setState(0);
    }

    public Cell(int state) {
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
    }

    public void changeState() {
        this.state = this.state == 0 ? 1 : 0;
    }
}
