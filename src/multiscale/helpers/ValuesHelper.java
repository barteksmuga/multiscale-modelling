package multiscale.helpers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import multiscale.model.Cell;

import static multiscale.enums.InitialConditionEnum.CUSTOM;
import static multiscale.enums.InitialConditionEnum.FIXED;
import static multiscale.enums.InitialConditionEnum.GLIDER;
import static multiscale.enums.InitialConditionEnum.OSCILLATOR;
import static multiscale.enums.InitialConditionEnum.RANDOM;

public class ValuesHelper {

    public static ObservableList<String> getElementaryMachineRuleList() {
        return FXCollections.observableArrayList("30", "60", "90", "120", "225");
    }

    public static ObservableList<String> getGameOfLifeInitialConditionList() {
        return FXCollections.observableArrayList(FIXED.getName(),
                GLIDER.getName(), CUSTOM.getName(), OSCILLATOR.getName(), RANDOM.getName());
    }

    public static ObservableList<ObservableList<Cell>> prepareDataList (Cell[][] grid) {
        ObservableList<ObservableList<Cell>> data = FXCollections.observableArrayList();
        for (Cell [] row : grid) {
            data.add(FXCollections.observableArrayList(row));
        }
        return data;
    }
}
