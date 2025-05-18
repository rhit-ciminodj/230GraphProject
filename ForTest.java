import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class ForTest {

    @BeforeClass
    public static void setup() {
        System.out.println("== Prim's Algorithm JUnit 4 Test ==");
    }

    // Utility method to run MST and return total weight
    private int runPrimAndGetTotalWeight(PrimsAlgorithm.Graph graph) {
        return graph.primMSTAndGetWeight();
    }

    @Test
    public void testSmallFixedGraph() {
        PrimsAlgorithm.Graph graph = new PrimsAlgorithm.Graph(5);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 3, 6);
        graph.addEdge(1, 2, 3);
        graph.addEdge(1, 3, 8);
        graph.addEdge(1, 4, 5);
        graph.addEdge(2, 4, 7);
        graph.addEdge(3, 4, 9);

        int totalWeight = runPrimAndGetTotalWeight(graph);

        // Expected MST: 0-1(2), 1-2(3), 1-4(5), 0-3(6) = 2+3+5+6 = 16
        assertEquals(16, totalWeight);
    }
    
    @Test
    public void testSmallSparseGraph() {
        PrimsAlgorithm.Graph graph = new PrimsAlgorithm.Graph(5);
        graph.addEdge(0, 1, 4);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 3);
        graph.addEdge(3, 4, 2); // Total of 4 edges = V - 1 (sparse but connected)

        int totalWeight = runPrimAndGetTotalWeight(graph);

        // MST: The graph is already a tree, so all edges are included
        // Total weight: 4 + 1 + 3 + 2 = 10
        assertEquals(10, totalWeight);
    }
    
    @Test
    public void testSmallFullyConnectedGraph() {
        PrimsAlgorithm.Graph graph = new PrimsAlgorithm.Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 4);
        graph.addEdge(0, 3, 3);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 6);

        int totalWeight = runPrimAndGetTotalWeight(graph);

        // MST: 0-1(1), 1-2(2), 0-3(3) = 6
        assertEquals(6, totalWeight);
    }
    
    @Test
    public void testTriangleGraph() {
        PrimsAlgorithm.Graph graph = new PrimsAlgorithm.Graph(3);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(0, 2, 3);

        int totalWeight = runPrimAndGetTotalWeight(graph);

        // Expected MST: 0-1(1), 1-2(2) = 1 + 2 = 3
        assertEquals(3, totalWeight);
    }
    
    @Test
    public void testSquareWithDiagonal() {
        PrimsAlgorithm.Graph graph = new PrimsAlgorithm.Graph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 0, 1);
        graph.addEdge(0, 2, 10); // Diagonal with higher weight

        int totalWeight = runPrimAndGetTotalWeight(graph);

        // Expected MST: 0-1(1), 1-2(1), 2-3(1) = 3
        assertEquals(3, totalWeight);
    }

    @Test(timeout = 5000)
    public void testMediumRandomGraphPerformance() {
        int nodes = 100;
        int edges = 300;
        PrimsAlgorithm.Graph graph = generateRandomGraph(nodes, edges);
        runPrimAndGetTotalWeight(graph);
    }

    @Test(timeout = 10000)
    public void testLargeSparseGraphPerformance() {
        int nodes = 1000;
        int edges = 1500;
        PrimsAlgorithm.Graph graph = generateRandomGraph(nodes, edges);
        runPrimAndGetTotalWeight(graph);
    }

    @Test(timeout = 15000)
    public void testLargeDenseGraphPerformance() {
        int nodes = 300;
        PrimsAlgorithm.Graph graph = new PrimsAlgorithm.Graph(nodes);
        Random rand = new Random();

        for (int u = 0; u < nodes; u++) {
            for (int v = u + 1; v < nodes; v++) {
                int weight = rand.nextInt(100) + 1;
                graph.addEdge(u, v, weight);
            }
        }

        runPrimAndGetTotalWeight(graph);
    }

    // Utility to generate a sparse or medium-sized graph
    private PrimsAlgorithm.Graph generateRandomGraph(int nodes, int edges) {
        PrimsAlgorithm.Graph graph = new PrimsAlgorithm.Graph(nodes);
        Random rand = new Random();

        for (int i = 0; i < edges; i++) {
            int u = rand.nextInt(nodes);
            int v = rand.nextInt(nodes);
            while (u == v) v = rand.nextInt(nodes);
            int weight = rand.nextInt(100) + 1;
            graph.addEdge(u, v, weight);
        }

        return graph;
    }
}
