package com.example.swartz;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.*;

public class Calculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // Create a graph
        Graph graph = new Graph(5);

        // Example edges
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 1, 3);
        graph.addEdge(2, 3, 9);
        graph.addEdge(2, 4, 2);
        graph.addEdge(3, 4, 4);

        int source = 0;
        int destination = 4;

        int numTolls = graph.calculateTolls(source, destination);
        TextView textView = findViewById(R.id.textView);

        if (numTolls != -1) {
            textView.setText("Number of tolls between source and destination: " + numTolls);
        } else {
            textView.setText("Destination is not reachable from the source.");
        }
    }

    public class Graph {
        private int V;
        private LinkedList<Node>[] adjList;

        public Graph(int V) {
            this.V = V;
            adjList = new LinkedList[V];
            for (int i = 0; i < V; i++) {
                adjList[i] = new LinkedList<>();
            }
        }

        public void addEdge(int source, int destination, int toll) {
            adjList[source].add(new Node(destination, toll));
        }

        public int calculateTolls(int source, int destination) {
            boolean[] visited = new boolean[V];
            int[] tolls = new int[V];
            Arrays.fill(tolls, Integer.MAX_VALUE);

            tolls[source] = 0;

            PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.toll));
            pq.add(new Node(source, 0));

            while (!pq.isEmpty()) {
                int u = pq.poll().vertex;
                visited[u] = true;

                for (Node neighbor : adjList[u]) {
                    int v = neighbor.vertex;
                    int toll = neighbor.toll;

                    if (!visited[v] && tolls[u] + toll < tolls[v]) {
                        tolls[v] = tolls[u] + toll;
                        pq.add(new Node(v, tolls[v]));
                    }
                }
            }

            return tolls[destination] != Integer.MAX_VALUE ? tolls[destination] : -1; // Return -1 if destination is not reachable
        }

        private class Node {
            int vertex;
            int toll;

            Node(int vertex, int toll) {
                this.vertex = vertex;
                this.toll = toll;
            }
        }
    }
}
