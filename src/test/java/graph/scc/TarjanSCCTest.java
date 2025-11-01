package graph.scc;

import graph.Graph;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {
    
    @Test
    public void testSimpleDAG() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(3, sccs.size());
        assertTrue(tarjan.getMetrics().getOperationsCount() > 0);
    }
    
    @Test
    public void testSingleSCC() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(1, sccs.size());
        assertEquals(3, sccs.get(0).size());
    }
    
    @Test
    public void testMultipleSCCs() {
        Graph graph = new Graph(6, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 2, 1);
        graph.addEdge(4, 5, 1);
        graph.addEdge(5, 4, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(3, sccs.size());
        
        for (List<Integer> scc : sccs) {
            assertEquals(2, scc.size());
        }
    }
    
    @Test
    public void testEmptyGraph() {
        Graph graph = new Graph(0, true);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(0, sccs.size());
    }
    
    @Test
    public void testSingleVertex() {
        Graph graph = new Graph(1, true);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(1, sccs.size());
        assertEquals(1, sccs.get(0).size());
        assertEquals(0, sccs.get(0).get(0));
    }
    
    @Test
    public void testCondensationGraph() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 2, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        tarjan.findSCCs();
        Graph condensation = tarjan.buildCondensationGraph();
        
        assertEquals(2, condensation.getVertexCount());
        assertTrue(condensation.getEdgeCount() >= 1);
    }
    
    @Test
    public void testComplexGraph() {
        Graph graph = new Graph(8, true);
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 4);
        graph.addEdge(3, 1, 1);
        graph.addEdge(4, 5, 2);
        graph.addEdge(5, 6, 5);
        graph.addEdge(6, 7, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertTrue(sccs.size() > 0);
        
        boolean hasNonTrivialSCC = sccs.stream().anyMatch(scc -> scc.size() > 1);
        assertTrue(hasNonTrivialSCC);
    }
}







