package multiscale.processors.monteCarlo;

public class MonteCarloDTO {
    private boolean process = false;
    private int iteration;

    public MonteCarloDTO() { }

    public MonteCarloDTO(boolean process, int iteration) {
        this.process = process;
        this.iteration = iteration;
    }

    public boolean isProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        this.process = process;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }
}
