package multiscale.model;

public class ProcessRuleProperties {
    private Cell[][] grid;
    private Integer ruleInd;
    private Integer gridWidth;
    private Integer gridHeight;

    public ProcessRuleProperties() {
    }

    public ProcessRuleProperties(Integer gridHeight, Integer gridWidth) {
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        initGrid();
    }

    private void initGrid () {
      this.grid = new Cell[this.gridHeight][this.gridWidth];
        for (int i=0; i<gridHeight; ++i) {
            for (int j=0; j<gridWidth; ++j) {
                grid[i][j] = new Cell();
            }
        }
    }

    public Integer getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(Integer gridWidth) {
        this.gridWidth = gridWidth;
    }

    public Integer getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(Integer gridHeight) {
        this.gridHeight = gridHeight;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    public Integer getRuleInd() {
        return ruleInd;
    }

    public void setRuleInd(Integer ruleInd) {
        this.ruleInd = ruleInd;
    }
}
