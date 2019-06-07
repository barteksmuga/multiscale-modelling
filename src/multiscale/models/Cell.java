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
    private static double maxEnergy = 0.0;
    private static double maxDensity = 0.0;

    private int state;
    private int cId;
    private Point point;
    private double energy;
    private Color energyColor = new Color(0, 1, 0, 1);
    private double dislocationDensity;
    private Color dislocationColor = new Color(0.5, 0.3, 0, 1);
    private boolean isRecrystallized;
    private double dislocationGradient;

    private Rectangle rectangle;
    private ModeEnum mode;
    private int[][] neighbourIds;

    public Cell(Cell cell) {
        this.state = cell.getState();
        this.cId = idGenerator.getAndIncrement();
        this.point = cell.getPoint();
        this.rectangle = cell.getRectangle();
        this.mode = cell.getMode();
        this.energy = cell.getEnergy();
        this.energyColor = cell.getEnergyColor();
    }

    private Cell(Builder builder) {
        this.cId = idGenerator.getAndIncrement();
        this.state = builder.state;
        this.rectangle = builder.rectangle;
        this.point = builder.point;
        this.mode = builder.mode;
        this.energy = builder.energy;
        this.isRecrystallized = builder.isRecrystallized;
        this.dislocationDensity = builder.dislocationDensity;
        initializeRectangle();
        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                leftButtonClicked();
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                rightButtonClicked();
            }
        });
    }

    public boolean isRecrystallized() {
        return isRecrystallized;
    }

    public void setRecrystallized(boolean recrystallized) {
        isRecrystallized = recrystallized;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
        if (energy > maxEnergy) {
            maxEnergy = energy;
        }
        if (energy == 0) {
            energyColor = new Color(0, 1, 0, 1);
            return;
        }
        double red = energy / (maxEnergy);
        energyColor = new Color(Math.min(red, 1), 0, 0, 1);
    }

    public void swapStateAndEnergy(int displayMode) {
        if (displayMode == 1) {
            rectangle.setFill(energyColor);
        } else if (displayMode == 2) {
            rectangle.setFill(dislocationColor);
        } else {
            updateRectangle();

        }
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
        if (isRecrystallized) {
            rectangle.setFill(new Color(1, Math.max(dislocationGradient, 0), 0, 1));
            return;
        }
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

    public double getEnergy() {
        return energy;
    }

    public Color getEnergyColor() {
        return energyColor;
    }

    public double getDislocationDensity() {
        return dislocationDensity;
    }

    public void addDislocationDensity(double dislocationDensity) {
        if (dislocationDensity > maxDensity) {
            maxDensity = dislocationDensity;
        }
        this.dislocationDensity += dislocationDensity;
    }

    public void recrystallize() {
        this.isRecrystallized = true;
        dislocationGradient = Math.min((this.dislocationDensity / maxDensity), 1.0);
        this.dislocationDensity = 0;
        updateRectangle();
        this.dislocationColor = new Color(0, Math.max(dislocationGradient, 0), 0, 1);
    }

    public static class Builder {

        private int state;
        private Rectangle rectangle;
        private Point point;
        private ModeEnum mode;
        private double energy;
        private double dislocationDensity;
        private boolean isRecrystallized;

        public Builder() {
        }

        public Builder withState(int state) {
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

        public Builder withEnergy(double energy) {
            this.energy = energy;
            return this;
        }

        public Builder withDislocationDensity(double dislocationDensity) {
            this.dislocationDensity = dislocationDensity;
            return this;
        }

        public Builder isRecrystallized(boolean isRecrystallized) {
            this.isRecrystallized = isRecrystallized;
            return this;
        }

        public Cell build() {
            return new Cell(this);
        }

        private static Paint getPaint(int state) {
            Color color = PaintHelper.getColorForState(state);
            return color.deriveColor(1, 1, 1, 1);
        }
    }

}
