package com.example.travelapps.sopir;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
class Graph {
    private final Map<String, List<Edge>> adjVertices = new HashMap<>();

    void addVertex(String label) {
        adjVertices.putIfAbsent(label, new ArrayList<>());
    }

    void addEdge(String label1, String label2, int weight) {
        adjVertices.computeIfAbsent(label1, k -> new ArrayList<>()).add(new Edge(label2, weight));
        adjVertices.computeIfAbsent(label2, k -> new ArrayList<>()).add(new Edge(label1, weight)); // Untuk graf tak berarah
    }

    List<Edge> getAdjVertices(String label) {
        return adjVertices.get(label);
    }

    static class Edge {
        String vertex;
        int weight;

        Edge(String vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }
    }

    Map<String, Integer> dijkstra(String start) {
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(edge -> edge.weight));
        Map<String, Integer> distances = new HashMap<>();
        Set<String> visited = new HashSet<>();

        pq.add(new Edge(start, 0));
        distances.put(start, 0);

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            visited.add(current.vertex);

            for (Edge neighbor : adjVertices.get(current.vertex)) {
                if (!visited.contains(neighbor.vertex)) {
                    int newDist = distances.get(current.vertex) + neighbor.weight;
                    if (newDist < distances.getOrDefault(neighbor.vertex, Integer.MAX_VALUE)) {
                        distances.put(neighbor.vertex, newDist);
                        pq.add(new Edge(neighbor.vertex, newDist));
                    }
                }
            }
        }

        return distances;
    }
}