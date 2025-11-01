package graph.topo;

import graph.Graph;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {
    
    @Test
    public void testSimpleDAG() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        
        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> order = topoSort.kahnSort();
        
        assertEquals(3, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(2));
    }
    
    @Test
    public void testDAGWithMultiplePaths() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);
        
        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> order = topoSort.kahnSort();
        
        assertEquals(4, order.size());
        assertEquals(0, order.get(0));
        assertEquals(3, order.get(3));
    }
    
    @Test
    public void testCyclicGraph() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        
        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> order = topoSort.kahnSort();
        
        assertEquals(0, order.size());
    }
    
    @Test
    public void testIsDAG() {
        Graph dag = new Graph(3, true);
        dag.addEdge(0, 1, 1);
        dag.addEdge(1, 2, 1);
        
        TopologicalSort topoSort1 = new TopologicalSort(dag);
        assertTrue(topoSort1.isDAG());
        
        Graph cyclic = new Graph(3, true);
        cyclic.addEdge(0, 1, 1);
        cyclic.addEdge(1, 2, 1);
        cyclic.addEdge(2, 0, 1);
        
        TopologicalSort topoSort2 = new TopologicalSort(cyclic);
        assertFalse(topoSort2.isDAG());
    }
    
    @Test
    public void testDFSSort() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);
        
        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> order = topoSort.dfsSort();
        
        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(2));
        assertTrue(order.indexOf(1) < order.indexOf(3));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }
    
    @Test
    public void testEmptyGraph() {
        Graph graph = new Graph(0, true);
        
        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> order = topoSort.kahnSort();
        
        assertEquals(0, order.size());
    }
    
    @Test
    public void testSingleVertex() {
        Graph graph = new Graph(1, true);
        
        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> order = topoSort.kahnSort();
        
        assertEquals(1, order.size());
        assertEquals(0, order.get(0));
    }
    
    @Test
    public void testDisconnectedDAG() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);
        
        TopologicalSort topoSort = new TopologicalSort(graph);
        List<Integer> order = topoSort.kahnSort();
        
        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(2) < order.indexOf(3));
    }
}







