package data;

import graph.Graph;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * Loads graph data from JSON files.
 */
public class GraphDataLoader {
    private final ObjectMapper objectMapper;
    
    public GraphDataLoader() {
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Loads graph data from JSON file.
     * @param filePath path to JSON file
     * @return GraphData object
     * @throws IOException if file cannot be read
     */
    public GraphData loadGraphData(String filePath) throws IOException {
        File file = new File(filePath);
        return objectMapper.readValue(file, GraphData.class);
    }
    
    /**
     * Converts GraphData to Graph object.
     * @param data graph data from JSON
     * @return Graph object
     */
    public Graph toGraph(GraphData data) {
        Graph graph = new Graph(data.getN(), data.isDirected());
        
        for (GraphData.EdgeData edge : data.getEdges()) {
            graph.addEdge(edge.getU(), edge.getV(), edge.getW());
        }
        
        return graph;
    }
    
    /**
     * Loads graph directly from JSON file.
     * Alternative method for convenience.
     * @param filePath path to JSON file
     * @return Graph object
     * @throws IOException if file cannot be read
     */
    public Graph loadGraph(String filePath) throws IOException {
        GraphData data = loadGraphData(filePath);
        return toGraph(data);
    }
}

