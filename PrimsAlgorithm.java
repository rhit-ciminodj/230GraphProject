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

            return totalWeight;
        }
    }

    public static void main(String[] args) {
        int V = 5;
        Graph graph = new Graph(V);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 3, 6);
        graph.addEdge(1, 2, 3);
        graph.addEdge(1, 3, 8);
        graph.addEdge(1, 4, 5);
        graph.addEdge(2, 4, 7);
        graph.addEdge(3, 4, 9);

        graph.primMSTAndGetWeight();
    }
}
