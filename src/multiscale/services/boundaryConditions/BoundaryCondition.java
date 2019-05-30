package multiscale.services.boundaryConditions;

public abstract class BoundaryCondition {
    protected int maxWidth;
    protected int maxHeight;

    public BoundaryCondition(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth - 1;
        this.maxHeight = maxHeight - 1;
    }

    public int getX(int x) {
        return getCoordinateValue(x, maxWidth);
    }

    public int getY(int y) {
        return getCoordinateValue(y, maxHeight);
    }

    protected abstract int getCoordinateValue(int coordinate, int maxValue);
}
