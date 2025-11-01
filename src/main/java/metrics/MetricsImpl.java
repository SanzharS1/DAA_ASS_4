package metrics;

public class MetricsImpl implements Metrics {
    private long operationsCount;
    private long startTime;
    private long endTime;
    private boolean timerRunning;
    
    public MetricsImpl() {
        this.operationsCount = 0;
        this.startTime = 0;
        this.endTime = 0;
        this.timerRunning = false;
    }
    
    @Override
    public void startTimer() {
        this.startTime = System.nanoTime();
        this.timerRunning = true;
    }
    
    @Override
    public void stopTimer() {
        if (timerRunning) {
            this.endTime = System.nanoTime();
            this.timerRunning = false;
        }
    }
    
    @Override
    public void incrementOperations() {
        this.operationsCount++;
    }
    
    @Override
    public void incrementOperations(int count) {
        this.operationsCount += count;
    }
    
    @Override
    public long getOperationsCount() {
        return this.operationsCount;
    }
    
    @Override
    public double getExecutionTimeMs() {
        return getExecutionTimeNs() / 1_000_000.0;
    }
    
    @Override
    public long getExecutionTimeNs() {
        if (timerRunning) {
            return System.nanoTime() - startTime;
        }
        return endTime - startTime;
    }
    
    @Override
    public void reset() {
        this.operationsCount = 0;
        this.startTime = 0;
        this.endTime = 0;
        this.timerRunning = false;
    }
}




