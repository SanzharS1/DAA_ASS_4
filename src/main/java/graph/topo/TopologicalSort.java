package graph.topo;

import graph.Graph;
import graph.Edge;
import metrics.Metrics;
import metrics.MetricsImpl;
import java.util.*;

public class TopologicalSort {
    private final Graph graph;
    private final Metrics metrics;
    
    public TopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }
    
    public List<Integer> kahnSort() {
        int n = graph.getVertexCount();
        int[] inDegree = new int[n];
        
        metrics.startTimer();
        
        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.getNeighbors(u)) {
                inDegree[edge.to]++;
                metrics.incrementOperations();
            }
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) {
                queue.offer(v);
                metrics.incrementOperations();
            }
        }
        
        List<Integer> topoOrder = new ArrayList<>();
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            topoOrder.add(u);
            metrics.incrementOperations();
            
            for (Edge edge : graph.getNeighbors(u)) {
                int v = edge.to;
                inDegree[v]--;
                metrics.incrementOperations();
                
                if (inDegree[v] == 0) {
                    queue.offer(v);
                    metrics.incrementOperations();
                }
            }
        }
        
        metrics.stopTimer();
        
        if (topoOrder.size() != n) {
            return new ArrayList<>();
        }
        
        return topoOrder;
    }
    
    public List<Integer> dfsSort() {
        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        Stack<Integer> stack = new Stack<>();
        
        metrics.reset();
        metrics.startTimer();
        
        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                dfsSortUtil(v, visited, stack);
            }
        }
        
        metrics.stopTimer();
        
        List<Integer> topoOrder = new ArrayList<>();
        while (!stack.isEmpty()) {
            topoOrder.add(stack.pop());
        }
        
        return topoOrder;
    }
    
    private void dfsSortUtil(int u, boolean[] visited, Stack<Integer> stack) {
        visited[u] = true;
        metrics.incrementOperations();
        
        for (Edge edge : graph.getNeighbors(u)) {
            int v = edge.to;
            metrics.incrementOperations();
            
            if (!visited[v]) {
                dfsSortUtil(v, visited, stack);
            }
        }
        
        stack.push(u);
    }
    
    public boolean isDAG() {
        int n = graph.getVertexCount();
        int[] color = new int[n];
        
        for (int v = 0; v < n; v++) {
            if (color[v] == 0) {
                if (hasCycleDFS(v, color)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean hasCycleDFS(int u, int[] color) {
        color[u] = 1;
        
        for (Edge edge : graph.getNeighbors(u)) {
            int v = edge.to;
            
            if (color[v] == 1) {
                return true;
            }
            
            if (color[v] == 0 && hasCycleDFS(v, color)) {
                return true;
            }
        }
        
        color[u] = 2;
        return false;
    }
    
    public Metrics getMetrics() {
        return metrics;
    }
}







