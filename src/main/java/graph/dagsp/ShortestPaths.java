package graph.dagsp;

import graph.Graph;
import graph.Edge;
import graph.topo.TopologicalSort;
import metrics.Metrics;
import metrics.MetricsImpl;
import java.util.*;

public class ShortestPaths {
    private final Graph graph;
    private final Metrics metrics;
    
    private static final int INF = Integer.MAX_VALUE / 2;
    
    public ShortestPaths(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }
    
    public PathResult shortestPaths(int source) {
        int n = graph.getVertexCount();
        int[] dist = new int[n];
        int[] pred = new int[n];
        
        Arrays.fill(dist, INF);
        Arrays.fill(pred, -1);
        dist[source] = 0;
        
        metrics.startTimer();
        
        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> topoOrder = topoSort.kahnSort();
        
        if (topoOrder.isEmpty()) {
            metrics.stopTimer();
            return new PathResult(dist, pred);
        }
        
        for (int u : topoOrder) {
            if (dist[u] != INF) {
                for (Edge edge : graph.getNeighbors(u)) {
                    int v = edge.to;
                    int newDist = dist[u] + edge.weight;
                    metrics.incrementOperations();
                    
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        pred[v] = u;
                    }
                }
            }
        }
        
        metrics.stopTimer();
        
        return new PathResult(dist, pred);
    }
    
    public PathResult longestPaths(int source) {
        int n = graph.getVertexCount();
        int[] dist = new int[n];
        int[] pred = new int[n];
        
        Arrays.fill(dist, -INF);
        Arrays.fill(pred, -1);
        dist[source] = 0;
        
        metrics.startTimer();
        
        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> topoOrder = topoSort.kahnSort();
        
        if (topoOrder.isEmpty()) {
            metrics.stopTimer();
            return new PathResult(dist, pred);
        }
        
        for (int u : topoOrder) {
            if (dist[u] != -INF) {
                for (Edge edge : graph.getNeighbors(u)) {
                    int v = edge.to;
                    int newDist = dist[u] + edge.weight;
                    metrics.incrementOperations();
                    
                    if (newDist > dist[v]) {
                        dist[v] = newDist;
                        pred[v] = u;
                    }
                }
            }
        }
        
        metrics.stopTimer();
        
        return new PathResult(dist, pred);
    }
    
    public CriticalPathResult findCriticalPath() {
        int n = graph.getVertexCount();
        int maxLength = -INF;
        int endVertex = -1;
        
        PathResult bestResult = null;
        int bestSource = -1;
        
        for (int source = 0; source < n; source++) {
            PathResult result = longestPaths(source);
            
            for (int v = 0; v < n; v++) {
                if (result.distances[v] > maxLength && result.distances[v] != -INF) {
                    maxLength = result.distances[v];
                    endVertex = v;
                    bestResult = result;
                    bestSource = source;
                }
            }
        }
        
        if (bestResult == null || endVertex == -1) {
            return new CriticalPathResult(new ArrayList<>(), 0);
        }
        
        List<Integer> path = reconstructPath(bestResult.predecessors, bestSource, endVertex);
        
        return new CriticalPathResult(path, maxLength);
    }
    
    public List<Integer> reconstructPath(int[] pred, int source, int dest) {
        List<Integer> path = new ArrayList<>();
        
        if (pred[dest] == -1 && source != dest) {
            return path;
        }
        
        int current = dest;
        while (current != -1) {
            path.add(current);
            if (current == source) {
                break;
            }
            current = pred[current];
        }
        
        Collections.reverse(path);
        
        return path;
    }
    
    public Metrics getMetrics() {
        return metrics;
    }
    
    public static class PathResult {
        public final int[] distances;
        public final int[] predecessors;
        
        public PathResult(int[] distances, int[] predecessors) {
            this.distances = distances;
            this.predecessors = predecessors;
        }
    }
    
    public static class CriticalPathResult {
        public final List<Integer> path;
        public final int length;
        
        public CriticalPathResult(List<Integer> path, int length) {
            this.path = path;
            this.length = length;
        }
    }
}







