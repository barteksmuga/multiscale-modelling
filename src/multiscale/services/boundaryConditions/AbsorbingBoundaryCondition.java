package multiscale.services.boundaryConditions;

public class AbsorbingBoundaryCondition extends BoundaryCondition {

    public AbsorbingBoundaryCondition(int maxWidth, int maxHeight) {
        super(maxWidth, maxHeight);
    }

    @Override
    protected int getCoordinateValue(int coordinate, int maxValue) {
        if (coordinate < 0) {
            coordinate = 0;
        }
        if (coordinate > maxValue) {
            coordinate = maxValue;
        }
        return coordinate;
    }
}
