package com.example.socialnetwork.service.utils;

import java.util.ArrayList;

/**
 * Class that maintains a graph
 */
public class Graph {
    int V;
    ArrayList<ArrayList<Integer>> adj;

    public Graph(int v) {
        this.V = v;
        this.adj = new ArrayList<>();

        for (int i = 0; i < V; i++)
            adj.add(i , new ArrayList<>());
    }

    /**
     * Adds an edge to the graph
     * @param s - the first extremity of the edge
     * @param d - the second extremity of the edge
     *
     */
    public void addEdge(int s, int d) {
        adj.get(s).add(d);
        adj.get(d).add(s);
    }

    /**
     * Clasic dfs traversal
     * @param node - the starting node of the traversal
     * @param visited - auxilliary array to maintain the visited nodes
     */
    private void dfs(int node, boolean[] visited) {
        visited[node] = true;
        for (int next : adj.get(node))
            if (!visited[next])
                dfs(next, visited);
    }

    /**
     *
     * @param validID - validID[i] = true if there's a node with this id
     *                in the graph, false otherwise
     * @return the number of connected components in the graph
     */
    public int connectedComponents(boolean[] validID) {
        int numberOfConnectedComponents = 0;
        boolean[] visited = new boolean[V];
        for (int node = 0; node < V; node++) {
            if (!visited[node] && validID[node]) {
                numberOfConnectedComponents++;
                dfs(node, visited);
            }
        }

        return numberOfConnectedComponents;
    }
}
