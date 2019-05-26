package multiscale.constants.grainGrowth;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import multiscale.enums.grainGrowth.BoundaryConditionEnum;
import multiscale.enums.grainGrowth.InsertModeEnum;
import multiscale.enums.grainGrowth.NeighbourhoodEnum;

import java.util.List;

public class ChoiceBoxOptions {

    public static ObservableList<String> insertModeList() {
        return getObservableList(InsertModeEnum.getNames());
    }

    public static ObservableList<String> boundaryConditionList() {
        return getObservableList(BoundaryConditionEnum.getNames());
    }

    public static ObservableList<String> neighbourhoodList() {
        return getObservableList(NeighbourhoodEnum.getNames());
    }

    private static ObservableList<String> getObservableList(List<String> list) {
        return FXCollections.observableArrayList(list);
    }
}
