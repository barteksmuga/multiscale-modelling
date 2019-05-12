package multiscale.services.tableView;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import multiscale.models.Cell;
import multiscale.models.GridProperties;

import static multiscale.helpers.constants.SizeConstants.TABLE_CELL_SIZE;

public class TableViewService {
    private TableView<ObservableList<Cell>> tableView;

    private final String activeClass = "active-cell";
    private final String inactiveClass = "inactive-cell";

    public TableViewService(TableView<ObservableList<Cell>> tableView) {
        this.tableView = tableView;
    }

    public void setTableViewData(Cell[][] grid) {
        ObservableList<ObservableList<Cell>> data = prepareDataList(grid);
        tableView.setItems(data);
    }

    public void prepareTableView(GridProperties gridProperties) {
        final String [] cellStyles = {activeClass, inactiveClass};
        Cell[][] grid = gridProperties.getGrid();
        for (int i=0; i<gridProperties.getGridWidth(); ++i) {
            final int columnIndex = i;
            final TableColumn<ObservableList<Cell>, Integer> column = new TableColumn<>(String.valueOf(columnIndex + 1));
            column.setCellValueFactory(cellValue -> new ReadOnlyObjectWrapper<>(cellValue.getValue().get(columnIndex).getState()));
            column.setCellFactory(cellFactory -> {
                TableCell<ObservableList<Cell>, Integer> tableCell = new TableCell<ObservableList<Cell>, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(" ");
                        getStyleClass().removeAll(cellStyles);
                        if (item != null && item == 1) {
                            getStyleClass().add(activeClass);
                        } else if (!empty){
                            getStyleClass().add(inactiveClass);
                        }
                    }

                };
                tableCell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (tableView.isEditable()) {
                        Cell[][] tempGrid = gridProperties.getFirstRow() != null ? gridProperties.getFirstRow() : grid;
                        TableCell cell = (TableCell) event.getSource();
                        cell.getStyleClass().removeAll(cellStyles);
                        int cellIndex = (int) cell.queryAccessibleAttribute(AccessibleAttribute.COLUMN_INDEX);
                        int rowIndex = cell.getIndex();
                        System.out.println("rowIndex " + rowIndex);
                        tempGrid[rowIndex][cellIndex].changeState();
                        cell.getStyleClass().add(tempGrid[rowIndex][cellIndex].getState() == 0 ? inactiveClass : activeClass);
                    }
                });
                return tableCell;
            });
            column.setSortable(Boolean.FALSE);
            column.setMinWidth(TABLE_CELL_SIZE);
            column.setMaxWidth(TABLE_CELL_SIZE);
            tableView.getColumns().add(column);
        }
        if (gridProperties.getFirstRow() != null) {
            tableView.setItems(prepareDataList(gridProperties.getFirstRow()));
        }
    }

    private ObservableList<ObservableList<Cell>> prepareDataList (Cell[][] grid) {
        ObservableList<ObservableList<Cell>> data = FXCollections.observableArrayList();
        for (Cell [] row : grid) {
            data.add(FXCollections.observableArrayList(row));
        }
        return data;
    }

}
