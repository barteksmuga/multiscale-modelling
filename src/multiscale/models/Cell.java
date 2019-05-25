package multiscale.models;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import multiscale.constants.enums.StateEnum;
import multiscale.services.PaintHelper;

import java.util.concurrent.atomic.AtomicInteger;

import static multiscale.constants.WindowConstants.DEFAULT_CELL_SIZE;

public class Cell extends StackPane {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private int state;
    private int cId;
    private Point point;

    private Rectangle rectangle;
    private Color color;

    public Cell() {
        this.cId = idGenerator.getAndIncrement();
        this.state = StateEnum.INACTIVE.getStateValue();
        this.rectangle = new Rectangle(DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE, Builder.getPaint(state));
        initializeRectangle();
    }

    private Cell(Builder builder) {
        this.cId = idGenerator.getAndIncrement();
        this.state = builder.state;
        this.rectangle = builder.rectangle;
        this.point = builder.point;

        initializeRectangle();
        setOnMouseClicked(event -> revertState());
    }

    public int getcId() {
        return cId;
    }

    public int getState() {
        return state;
    }

    private void revertState() {
        this.state = this.state == 0 ? 1 : 0;
        updateRectangle();
    }

    public void setState(int state) {
        this.state = state;
        updateRectangle();
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    private void updateRectangle() {
        rectangle.setFill(Builder.getPaint(state));
    }

    private void initializeRectangle() {
        rectangle.setStroke(Color.BLACK);
        getChildren().addAll(rectangle);
    }

    public static class Builder {
        private int state;
        private Rectangle rectangle;
        private Point point;

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

        public Cell build() {
            return new Cell(this);
        }

        private static Paint getPaint(int state) {
            Color color = PaintHelper.getColorForState(state);
            return color.deriveColor(1,1,1,1);
        }

    }
}
