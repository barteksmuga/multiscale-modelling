package multiscale.models;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import multiscale.enums.ModeEnum;
import multiscale.enums.StateEnum;
import multiscale.services.PaintHelper;

import java.util.concurrent.atomic.AtomicInteger;

import static multiscale.enums.StateEnum.ACTIVE;
import static multiscale.enums.StateEnum.INACTIVE;

public class Cell extends StackPane {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private static int currentState = 0;
    private int state;
    private int cId;
    private Point point;

    private Rectangle rectangle;
    private ModeEnum mode;
    private int[][] neighbourIds;

    public Cell(Cell cell) {
        this.state = cell.getState();
        this.cId = idGenerator.getAndIncrement();
        this.point = cell.getPoint();
        this.rectangle = cell.getRectangle();
        this.mode = cell.getMode();
    }

    private Cell(Builder builder) {
        this.cId = idGenerator.getAndIncrement();
        this.state = builder.state;
        this.rectangle = builder.rectangle;
        this.point = builder.point;
        this.mode = builder.mode;
        initializeRectangle();
        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                leftButtonClicked();
            } else if (event.getButton().equals(MouseButton.SECONDARY)){
                rightButtonClicked();
            }
        });
    }

    public int getcId() {
        return cId;
    }

    public int getState() {
        return state;
    }

    public void setNeighbourIds(int[][] neighbourIds) {
        this.neighbourIds = neighbourIds;
    }

    public int[][] getNeighbourIds() {
        return neighbourIds;
    }

    private void incrementState() {
        ++this.state;
        ++currentState;
        updateRectangle();
    }

    private void revertState() {
        this.state = this.state == INACTIVE.getStateValue() ? ACTIVE.getStateValue() : INACTIVE.getStateValue();
        updateRectangle();
    }

    public void setAutoState() {
        this.state = ++currentState;
        updateRectangle();
    }

    public void setState(int state) {
        this.state = state;
        updateRectangle();
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Point getPoint() {
        return point;
    }

    public ModeEnum getMode() {
        return mode;
    }

    private void updateRectangle() {
        rectangle.setFill(Builder.getPaint(state));
    }

    private void initializeRectangle() {
        rectangle.setStroke(Color.BLACK);
        getChildren().addAll(rectangle);
    }

    private void rightButtonClicked() {
        setState(StateEnum.NOT_SET.getStateValue());
    }

    private void leftButtonClicked() {
        if (mode != ModeEnum.GRAIN_GROWTH) {
            revertState();
        } else {
            incrementState();
        }
    }

    public static class Builder {
        private int state;
        private Rectangle rectangle;
        private Point point;
        private ModeEnum mode;

        public Builder() {}

        public Builder withState (int state) {
            this.state = state;
            return this;
        }

        public Builder withRectangle(Rectangle rectangle) {
            this.rectangle = rectangle;
            this.rectangle.setFill(getPaint(state));
            return this;
        }

        public Builder withCoordinates(Point point) {
            this.point = point;
            return this;
        }

        public Builder withMode(ModeEnum mode) {
            this.mode = mode;
            return this;
        }

        public Cell build() {
            return new Cell(this);
        }

        private static Paint getPaint(int state) {
            Color color = PaintHelper.getColorForState(state);
            return color.deriveColor(1,1,1,1);
        }
    }

}
