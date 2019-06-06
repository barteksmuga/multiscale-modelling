package multiscale.services;

import javafx.scene.layout.GridPane;
import multiscale.models.Cell;
import multiscale.models.Grid;

import java.util.ArrayList;

public class GridPaneService {

    public void wipeGridPaneData(GridPane gridPane) {
        gridPane.getChildren().clear();
    }

    public void drawArrayOnGridPane(GridPane gridPane, Grid grid) {
//        gridPane.getChildren().clear();
        var toAdd = new ArrayList<Cell>();
        for (int y = 0; y<grid.getHeight(); ++y) {
            for (int x=0; x<grid.getWidth(); ++x) {
                Cell cell = grid.getGrid()[y][x];
                GridPane.setRowIndex(cell, y);
                GridPane.setColumnIndex(cell, x);
                toAdd.add(cell);
            }
        }
        gridPane.getChildren().setAll(toAdd);
    }
}
