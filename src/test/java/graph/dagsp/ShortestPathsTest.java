package graph.dagsp;

import graph.Graph;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ShortestPathsTest {
    
    @Test
    public void testShortestPathLinear() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);
        
        ShortestPaths sp = new ShortestPaths(graph);
        ShortestPaths.PathResult result = sp.shortestPaths(0);
        
        assertEquals(0, result.distances[0]);
        assertEquals(2, result.distances[1]);
        assertEquals(5, result.distances[2]);
        
        List<Integer> path = sp.reconstructPath(result.predecessors, 0, 2);
        assertEquals(3, path.size());
        assertEquals(0, path.get(0));
        assertEquals(1, path.get(1));
        assertEquals(2, path.get(2));
    }
    
    @Test
    public void testShortestPathDiamond() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 4);
        
        ShortestPaths sp = new ShortestPaths(graph);
        ShortestPaths.PathResult result = sp.shortestPaths(0);
        
        assertEquals(0, result.distances[0]);
        assertEquals(6, result.distances[3]);
        
        List<Integer> path = sp.reconstructPath(result.predecessors, 0, 3);
        assertTrue(path.contains(0), "Path should start with 0");
        assertTrue(path.contains(3), "Path should end with 3");
        // Both paths have same length (6), so either path through 1 or 2 is valid
        assertTrue(path.contains(1) || path.contains(2), 
                  "Path should go through either vertex 1 or vertex 2");
        assertEquals(3, path.size(), "Path should have 3 vertices");
    }
    
    @Test
    public void testLongestPathLinear() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);
        
        ShortestPaths sp = new ShortestPaths(graph);
        ShortestPaths.PathResult result = sp.longestPaths(0);
        
        assertEquals(0, result.distances[0]);
        assertEquals(2, result.distances[1]);
        assertEquals(5, result.distances[2]);
    }
    
    @Test
    public void testLongestPathDiamond() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 4);
        
        ShortestPaths sp = new ShortestPaths(graph);
        ShortestPaths.PathResult result = sp.longestPaths(0);
        
        assertEquals(0, result.distances[0]);
        assertEquals(6, result.distances[3]);
    }
    
    @Test
    public void testCriticalPath() {
        Graph graph = new Graph(6, true);
        graph.addEdge(0, 1, 3);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 2);
        graph.addEdge(4, 5, 3);
        
        ShortestPaths sp = new ShortestPaths(graph);
        ShortestPaths.CriticalPathResult result = sp.findCriticalPath();
        
        assertNotNull(result.path);
        assertTrue(result.length > 0);
        assertTrue(result.path.size() > 0);
    }
    
    @Test
    public void testUnreachableVertex() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(2, 3, 3);
        
        ShortestPaths sp = new ShortestPaths(graph);
        ShortestPaths.PathResult result = sp.shortestPaths(0);
        
        assertEquals(0, result.distances[0]);
        assertEquals(5, result.distances[1]);
        assertTrue(result.distances[2] > 1000000);
        assertTrue(result.distances[3] > 1000000);
    }
    
    @Test
    public void testSingleVertex() {
        Graph graph = new Graph(1, true);
        
        ShortestPaths sp = new ShortestPaths(graph);
        ShortestPaths.PathResult result = sp.shortestPaths(0);
        
        assertEquals(0, result.distances[0]);
    }
    
    @Test
    public void testPathReconstruction() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        
        ShortestPaths sp = new ShortestPaths(graph);
        ShortestPaths.PathResult result = sp.shortestPaths(0);
        
        List<Integer> path = sp.reconstructPath(result.predecessors, 0, 3);
        
        assertEquals(4, path.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(i, path.get(i));
        }
    }
}



