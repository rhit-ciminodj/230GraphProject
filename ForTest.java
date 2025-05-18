import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
    
    @Test(timeout = 10000)
    public void testChainGraph_10000Nodes_MSTWeightIsCorrect() {
        int V = 10000;
        PrimsAlgorithm.Graph graph = new PrimsAlgorithm.Graph(V);

        // Create a chain: 0-1-2-...-9999, each edge weight = 1
        for (int i = 0; i < V - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        int expectedMSTWeight = V - 1; // 9999
        int actualMSTWeight = graph.primMSTAndGetWeight();

        assertEquals("MST weight should be exactly 9999 for a chain graph of 10000 nodes", expectedMSTWeight, actualMSTWeight);
    }
    
    @Test(timeout = 10000)
    public void testGridGraph_10000Nodes_MSTWeightIsCorrect() {
        int rows = 100;
        int cols = 100;
        int V = rows * cols;
        PrimsAlgorithm.Graph graph = new PrimsAlgorithm.Graph(V);

        // Connect grid (right and down only) with weight = 1
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int current = r * cols + c;
                // Connect to right neighbor
                if (c < cols - 1) {
                    int right = r * cols + (c + 1);
                    graph.addEdge(current, right, 1);
                }
                // Connect to bottom neighbor
                if (r < rows - 1) {
                    int down = (r + 1) * cols + c;
                    graph.addEdge(current, down, 1);
                }
            }
        }

        int expectedMSTWeight = V - 1; // For any connected graph, MST has (V-1) edges
        int actualMSTWeight = graph.primMSTAndGetWeight();

        assertEquals("MST weight should be 9999 for 100x100 grid with weight 1", expectedMSTWeight, actualMSTWeight);
    }
    
    @Test(timeout = 15000)
    public void testGridGraph_HorizontalFirst_CorrectMSTWeight() {
        int rows = 100;
        int cols = 100;
        int V = rows * cols;
        PrimsAlgorithm.Graph graph = new PrimsAlgorithm.Graph(V);

        int weight = 1;

        // Step 1: Add horizontal edges first (left to right)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols - 1; c++) {
                int u = r * cols + c;
                int v = u + 1;
                graph.addEdge(u, v, weight++);
            }
        } // all the horizontal weights are 1,2,3,4,5,6...

        // Step 2: Add vertical edges (top to bottom)
        for (int r = 0; r < rows - 1; r++) {
            for (int c = 0; c < cols; c++) {
                int u = r * cols + c;
                int v = u + cols;
                graph.addEdge(u, v, weight++);
            }
        }//all the vertical weights are 9901,9902,9903....

        int expectedMSTWeight = 9901*9900/2+(9901+19701)*99/2; // = 1465299   connect all the horizontal edges and connect (0,0), (0,1) (0,2) (0,3) which edges' weights are 9901,10001,10101...
        int actualMSTWeight = graph.primMSTAndGetWeight();

        assertEquals("Exact MST weight for horizontal-first grid is expected", expectedMSTWeight, actualMSTWeight);
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
