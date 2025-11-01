package graph.scc;

import graph.Graph;
import graph.Edge;
import metrics.Metrics;
import metrics.MetricsImpl;
import java.util.*;

public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;
    
    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private int time;
    
    private List<List<Integer>> sccs;
    
    public TarjanSCC(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }
    
    public List<List<Integer>> findSCCs() {
        int n = graph.getVertexCount();
        
        disc = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        sccs = new ArrayList<>();
        time = 0;
        
        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
        
        metrics.startTimer();
        
        for (int v = 0; v < n; v++) {
            if (disc[v] == -1) {
                tarjanDFS(v);
            }
        }
        
        metrics.stopTimer();
        
        return sccs;
    }
    
    private void tarjanDFS(int u) {
        disc[u] = low[u] = time++;
        stack.push(u);
        onStack[u] = true;
        
        metrics.incrementOperations();
        
        for (Edge edge : graph.getNeighbors(u)) {
            int v = edge.to;
            metrics.incrementOperations();
            
            if (disc[v] == -1) {
                tarjanDFS(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        
        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                onStack[v] = false;
                scc.add(v);
            } while (v != u);
            
            sccs.add(scc);
        }
    }
    
    public Graph buildCondensationGraph() {
        if (sccs == null || sccs.isEmpty()) {
            findSCCs();
        }
        
        int numSccs = sccs.size();
        Graph condensation = new Graph(numSccs, true);
        
        int[] vertexToScc = new int[graph.getVertexCount()];
        for (int sccIndex = 0; sccIndex < sccs.size(); sccIndex++) {
            for (int vertex : sccs.get(sccIndex)) {
                vertexToScc[vertex] = sccIndex;
            }
        }
        
        Set<String> addedEdges = new HashSet<>();
        for (int u = 0; u < graph.getVertexCount(); u++) {
            int sccU = vertexToScc[u];
            for (Edge edge : graph.getNeighbors(u)) {
                int v = edge.to;
                int sccV = vertexToScc[v];
                
                if (sccU != sccV) {
                    String edgeKey = sccU + "-" + sccV;
                    if (!addedEdges.contains(edgeKey)) {
                        condensation.addEdge(sccU, sccV, edge.weight);
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }
        
        return condensation;
    }
    
    public List<List<Integer>> getSCCs() {
        if (sccs == null) {
            findSCCs();
        }
        return sccs;
    }
    
    public Metrics getMetrics() {
        return metrics;
    }
}




