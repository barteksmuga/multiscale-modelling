package multiscale.services.boundaryConditions;

public class PeriodicalBoundaryCondition extends BoundaryCondition {

    public PeriodicalBoundaryCondition(int maxWidth, int maxHeight) {
        super(maxWidth, maxHeight);
    }

    @Override
    protected int getCoordinateValue(int coordinate, int maxValue) {
        if (coordinate < 0) {
            coordinate = maxValue;
        }
        if (coordinate > maxValue) {
            coordinate = 0;
        }
        return coordinate;
    }
}
