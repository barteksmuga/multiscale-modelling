package multiscale.helpers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ValuesHelper {

    public static ObservableList<String> getChoiceBoxOptions() {
        return FXCollections.observableArrayList(
                "30","60","90","120","225"
        );
    }
}
