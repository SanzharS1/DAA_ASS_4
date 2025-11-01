package graph;

import java.util.*;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adjList;
    private final Map<Integer, Integer> nodeWeights;
    
    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adjList = new ArrayList<>();
        this.nodeWeights = new HashMap<>();
        
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }
    }
    
    public void addEdge(int u, int v, int weight) {
        adjList.get(u).add(new Edge(u, v, weight));
        if (!directed) {
            adjList.get(v).add(new Edge(v, u, weight));
        }
    }
    
    public void setNodeWeight(int node, int weight) {
        nodeWeights.put(node, weight);
    }
    
    public int getNodeWeight(int node) {
        return nodeWeights.getOrDefault(node, 0);
    }
    
    public int getVertexCount() {
        return n;
    }
    
    public List<Edge> getNeighbors(int u) {
        return adjList.get(u);
    }
    
    public List<List<Edge>> getAdjList() {
        return adjList;
    }
    
    public boolean isDirected() {
        return directed;
    }
    
    public int getEdgeCount() {
        int count = 0;
        for (List<Edge> edges : adjList) {
            count += edges.size();
        }
        return directed ? count : count / 2;
    }
    
    public Graph reverse() {
        Graph reversed = new Graph(n, directed);
        for (int u = 0; u < n; u++) {
            for (Edge edge : adjList.get(u)) {
                reversed.addEdge(edge.to, edge.from, edge.weight);
            }
        }
        for (Map.Entry<Integer, Integer> entry : nodeWeights.entrySet()) {
            reversed.setNodeWeight(entry.getKey(), entry.getValue());
        }
        return reversed;
    }
}




