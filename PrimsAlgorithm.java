import java.util.*;

public class PrimsAlgorithm {

    static class Edge {
        int target;
        int weight;

        public Edge(int target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    static class Node {
        int vertex;
        int key;

        public Node(int vertex, int key) {
            this.vertex = vertex;
            this.key = key;
        }
    }

    static class Graph {
        int vertices;
        List<List<Edge>> adjacencyList;

        public Graph(int vertices) {
            this.vertices = vertices;
            adjacencyList = new ArrayList<>();
            for (int i = 0; i < vertices; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }

        public void addEdge(int source, int target, int weight) {
            adjacencyList.get(source).add(new Edge(target, weight));
            adjacencyList.get(target).add(new Edge(source, weight)); // Because the graph is undirected
        }

        public int primMSTAndGetWeight() {
            boolean[] mstSet = new boolean[vertices];
            int[] key = new int[vertices];
            int[] parent = new int[vertices];

            PriorityQueue<Node> pq = new PriorityQueue<>(vertices, Comparator.comparingInt(n -> n.key));
            Arrays.fill(key, Integer.MAX_VALUE);
            key[0] = 0;
            parent[0] = -1;
            pq.add(new Node(0, 0));

            int totalWeight = 0;

            while (!pq.isEmpty()) {
                Node node = pq.poll();
                int u = node.vertex;

                if (mstSet[u]) continue;
                mstSet[u] = true;
                totalWeight += key[u];

                for (Edge edge : adjacencyList.get(u)) {
                    int v = edge.target;
                    int weight = edge.weight;

                    if (!mstSet[v] && weight < key[v]) {
                        key[v] = weight;
                        pq.add(new Node(v, key[v]));
                        parent[v] = u;
                    }
                }
            }
//            System.out.println("\nEdges in the Minimum Spanning Tree:");
//            for (int i = 1; i < vertices; i++) {
//                System.out.println(parent[i] + " - " + i + " (weight: " + key[i] + ")");
//            }

            return totalWeight;
        }

        
        public void printAdjacencyList() {
            for (int i = 0; i < vertices; i++) {
                System.out.print("Vertex " + i + ":");
                for (Edge edge : adjacencyList.get(i)) {
                    System.out.print(" -> (" + edge.target + ", weight: " + edge.weight + ")");
                }
                System.out.println();
            }
        }
    }
    
    public static Graph generateRandomGraph(int vertices, int edgeCount, int maxWeight) {
        Random rand = new Random();
        Graph graph = new Graph(vertices);

        // Make sure the graph is connected: form a spanning tree first
        for (int i = 1; i < vertices; i++) {
            int target = rand.nextInt(i);
            int weight = rand.nextInt(maxWeight) + 1;
            graph.addEdge(i, target, weight);
        }

        // Add random edges to reach desired edge count
        int addedEdges = vertices - 1;
        while (addedEdges < edgeCount) {
            int u = rand.nextInt(vertices);
            int v = rand.nextInt(vertices);
            if (u != v) {
                int weight = rand.nextInt(maxWeight) + 1;
                graph.addEdge(u, v, weight);
                addedEdges++;
            }
        }
        return graph;
    }
    
    public static void runPerformanceTest(int vertices, int edgeCount) {
        int maxWeight = 100;
        Graph graph = generateRandomGraph(vertices, edgeCount, maxWeight);

        long startTime = System.nanoTime();
        int mstWeight = graph.primMSTAndGetWeight();
        long endTime = System.nanoTime();

        double durationMs = (endTime - startTime) / 1e6;
        System.out.printf("Vertices: %d, Edges: %d, MST Weight: %d, Time: %.2f ms\n",
                vertices, edgeCount, mstWeight, durationMs);
    }


    public static void main(String[] args) {
//    	System.out.println("Graph 1: small sparse graph");
//        int V = 8;
//        Graph graph = new Graph(V);
//        graph.addEdge(0, 3, 3);
//        graph.addEdge(0, 1, 3);
//        graph.addEdge(0, 4, 7);
//        graph.addEdge(2, 4, 8);
//        graph.addEdge(2, 5, 7);
//        graph.addEdge(2, 6, 6);
//        graph.addEdge(3, 5, 1);
//        graph.addEdge(5, 6, 6);
//        graph.addEdge(5, 7, 6);
//        graph.addEdge(6, 7, 7);
//        graph.printAdjacencyList();
//
//        System.out.println("The minimum weight: "+graph.primMSTAndGetWeight());
//        
//        System.out.println("Graph 2: fully connected graph");
//        int V2 = 6;
//        graph = new Graph(V2);
//        graph.addEdge(0, 2, 3);
//        graph.addEdge(0, 4, 5);
//        graph.addEdge(0, 5, 1);
//        graph.addEdge(1, 4, 4);
//        graph.addEdge(1, 5, 8);
//        graph.addEdge(2, 3, 6);
//        graph.addEdge(2, 4, 8);
//        graph.addEdge(4, 5, 10);
//        graph.addEdge(1, 0, 1);
//        graph.addEdge(1, 2, 2);
//        graph.addEdge(2, 5, 5);
//        graph.addEdge(3, 4, 2);
//        graph.addEdge(1, 3, 7);
//        graph.addEdge(3, 5, 6);
//        graph.addEdge(3, 0, 8);
//        graph.printAdjacencyList();
//        System.out.println("The minimum weight: "+graph.primMSTAndGetWeight());
        
        System.out.println("Running performance tests...\n");

        runPerformanceTest(100, 100);
        // Small test
        runPerformanceTest(100, 200);    // Sparse
        runPerformanceTest(100, 1000);   // Dense

        // Medium test
        runPerformanceTest(500, 1000);
        runPerformanceTest(500, 10000);

        // Large test
        runPerformanceTest(1000, 3000);
        runPerformanceTest(1000, 20000);

        // Very large test (optional if memory allows)
         runPerformanceTest(5000, 10000);
         runPerformanceTest(5000, 40000);

        
    }
}
