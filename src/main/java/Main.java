import data.GraphDataLoader;
import data.GraphData;
import graph.Graph;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.ShortestPaths;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        if (args.length > 0) {
            processGraphFile(args[0]);
        } else {
            processAllDatasets();
        }
    }
    
    private static void processAllDatasets() {
        File dataDir = new File("data");
        
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            System.out.println("Error: 'data' directory not found!");
            return;
        }
        
        File[] files = dataDir.listFiles((dir, name) -> name.endsWith(".json"));
        
        if (files == null || files.length == 0) {
            System.out.println("No JSON files found in data directory!");
            return;
        }
        
        System.out.println("Processing " + files.length + " datasets\n");
        
        for (File file : files) {
            processGraphFile(file.getPath());
            System.out.println();
        }
    }
    
    private static void processGraphFile(String filePath) {
        System.out.println("File: " + filePath);
        
        try {
            GraphDataLoader loader = new GraphDataLoader();
            GraphData graphData = loader.loadGraphData(filePath);
            Graph graph = loader.toGraph(graphData);
            
            // Verify weight model
            String weightModel = graphData.getWeightModel();
            if (weightModel != null && !weightModel.equals("edge")) {
                System.out.println("Warning: weight model is " + weightModel);
            }
            
            System.out.println("Vertices: " + graph.getVertexCount() + ", Edges: " + graph.getEdgeCount());
            
            // SCC detection
            System.out.println("\n1. SCC Detection:");
            TarjanSCC tarjanSCC = new TarjanSCC(graph);
            tarjanSCC.findSCCs();
            List<List<Integer>> sccs = tarjanSCC.getSCCs();
            System.out.println("Total SCCs: " + sccs.size());
            
            for (int i = 0; i < sccs.size(); i++) {
                List<Integer> scc = sccs.get(i);
                System.out.println("  SCC " + i + " (size " + scc.size() + "): " + scc);
            }
            
            System.out.println("Operations: " + tarjanSCC.getMetrics().getOperationsCount());
            System.out.println("Time: " + String.format("%.3f ms", tarjanSCC.getMetrics().getExecutionTimeMs()));
            
            // Condensation graph
            Graph condensation = tarjanSCC.buildCondensationGraph();
            System.out.println("\nCondensation: " + condensation.getVertexCount() + " nodes, " + 
                             condensation.getEdgeCount() + " edges");
            
            // Topological sort
            System.out.println("\n2. Topological Sort:");
            TopologicalSort topoSort = new TopologicalSort(condensation);
            
            if (topoSort.isDAG()) {
                List<Integer> order = topoSort.kahnSort();
                System.out.println("Topological order: " + order);
                System.out.println("Operations: " + topoSort.getMetrics().getOperationsCount());
                System.out.println("Time: " + String.format("%.3f ms", topoSort.getMetrics().getExecutionTimeMs()));
                
                System.out.println("\nTask order:");
                for (int i = 0; i < order.size(); i++) {
                    int sccIndex = order.get(i);
                    List<Integer> scc = sccs.get(sccIndex);
                    System.out.println("  " + (i+1) + ". SCC " + sccIndex + ": " + scc);
                }
            } else {
                System.out.println("Graph contains cycles");
            }
            
            // DAG shortest/longest paths
            if (condensation.getVertexCount() > 0) {
                System.out.println("\n3. DAG Paths:");
                ShortestPaths sp = new ShortestPaths(condensation);
                
                int source = graphData.getSource() != null ? 
                           findSccContaining(sccs, graphData.getSource()) : 0;
                
                // Shortest paths
                ShortestPaths.PathResult shortestResult = sp.shortestPaths(source);
                System.out.println("\nShortest paths from SCC " + source + ":");
                for (int v = 0; v < shortestResult.distances.length; v++) {
                    if (shortestResult.distances[v] != Integer.MAX_VALUE / 2) {
                        List<Integer> path = sp.reconstructPath(shortestResult.predecessors, source, v);
                        System.out.println("  To SCC " + v + ": distance=" + shortestResult.distances[v] + 
                                         ", path=" + path);
                    }
                }
                System.out.println("Operations: " + sp.getMetrics().getOperationsCount());
                System.out.println("Time: " + String.format("%.3f ms", sp.getMetrics().getExecutionTimeMs()));
                
                // Longest paths
                sp.getMetrics().reset();
                ShortestPaths.PathResult longestResult = sp.longestPaths(source);
                System.out.println("\nLongest paths from SCC " + source + ":");
                for (int v = 0; v < longestResult.distances.length; v++) {
                    if (longestResult.distances[v] != -Integer.MAX_VALUE / 2) {
                        List<Integer> path = sp.reconstructPath(longestResult.predecessors, source, v);
                        System.out.println("  To SCC " + v + ": distance=" + longestResult.distances[v] + 
                                         ", path=" + path);
                    }
                }
                System.out.println("Operations: " + sp.getMetrics().getOperationsCount());
                System.out.println("Time: " + String.format("%.3f ms", sp.getMetrics().getExecutionTimeMs()));
                
                // Critical path
                sp.getMetrics().reset();
                ShortestPaths.CriticalPathResult criticalPath = sp.findCriticalPath();
                System.out.println("\nCritical path:");
                System.out.println("  Path: " + criticalPath.path);
                System.out.println("  Length: " + criticalPath.length);
            }
            
        } catch (IOException e) {
            System.out.println("Error loading graph: " + e.getMessage());
        }
    }
    
    private static int findSccContaining(List<List<Integer>> sccs, int vertex) {
        for (int i = 0; i < sccs.size(); i++) {
            if (sccs.get(i).contains(vertex)) {
                return i;
            }
        }
        return 0;
    }
}



