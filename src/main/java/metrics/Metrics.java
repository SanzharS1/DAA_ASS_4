package metrics;

public interface Metrics {
    void startTimer();
    void stopTimer();
    void incrementOperations();
    void incrementOperations(int count);
    long getOperationsCount();
    double getExecutionTimeMs();
    long getExecutionTimeNs();
    void reset();
}




