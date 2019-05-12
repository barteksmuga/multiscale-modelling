package multiscale.services.process;

import multiscale.models.Cell;
import multiscale.models.GridProperties;
import multiscale.services.tableView.TableViewService;


abstract class ProcessService {
    TableViewService tableViewService;
    GridProperties gridProperties;

    ProcessService(TableViewService tableViewService, GridProperties gridProperties) {
        this.tableViewService = tableViewService;
        this.gridProperties = gridProperties;
    }

    public abstract void run();

    void setTableViewData(Cell[][] grid) {
        tableViewService.setTableViewData(grid);
    }

    int getCorrectPreviousX(int previousX, int maxValue) {
        if (previousX < 0) {
            previousX = maxValue;
        }
        return previousX;
    }

    int getCorrectFollowingX(int followingX, int maxValue) {
        if (followingX > maxValue) {
            followingX = 0;
        }
        return followingX;
    }
}
