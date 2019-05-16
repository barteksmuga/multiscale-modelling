package multiscale.services;

import javafx.scene.layout.GridPane;
import multiscale.models.Cell;
import multiscale.models.Grid;

public class GridPaneService {

    public void wipeGridPaneData(GridPane gridPane) {
        gridPane.getChildren().removeAll(gridPane.getChildren());
    }

    public void drawArrayOnGridPane(GridPane gridPane, Grid grid) {
        for (int y = 0; y<grid.getHeight(); ++y) {
            for (int x=0; x<grid.getWidth(); ++x) {
                Cell cell = grid. getGrid()[y][x];
                GridPane.setRowIndex(cell, y);
                GridPane.setColumnIndex(cell, x);
                gridPane.getChildren().add(cell);
            }
        }
    }
}
