package Relation;

import org.omg.CORBA.NVList;

import java.util.ArrayList;

public class ClosureGraph {

    // No. of vertices in graph
    private final int vertices;

    // adjacency list
    private ArrayList<Integer>[] adjList;

    // To store transitive closure
    private final boolean[][] tc;

    // Constructor
    public ClosureGraph(int vertices) {

        // initialise vertex count
        this.vertices = vertices;
        this.tc = new boolean[this.vertices][this.vertices];

        // initialise adjacency list
        initAdjList();
    }

    // utility method to initialise adjacency list
    @SuppressWarnings("unchecked")
    private void initAdjList() {

        adjList = new ArrayList[vertices];
        for (int i = 0; i < vertices; i++) {
            adjList[i] = new ArrayList<>();
        }
    }

    // add edge from u to v
    public void addEdge(int u, int v) {

        // Add v to u's list.
        adjList[u].add(v);
    }

    // The function to find transitive
    // closure. It uses
    // recursive DFSUtil()
    @Deprecated
    public void transitiveClosure() {

        // Call the recursive helper
        // function to print DFS
        // traversal starting from all
        // vertices one by one
        for (int i = 0; i < vertices; i++) {
            dfsUtil(i, i);
        }

//        for (int i = 0; i < vertices; i++) {
//            tc[i][i] = false;
//        }

    }

    public boolean[][] transitiveClosure(boolean[][] src) {

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (src[i][j]) {
                    addEdge(i, j);
                }
            }
        }

        // Call the recursive helper
        // function to print DFS
        // traversal starting from all
        // vertices one by one
        for (int i = 0; i < vertices; i++) {
            for (int adj : adjList[i]) {
                if (!tc[i][adj]) {
                    dfsUtil(i, adj);
                }
            }
        }

        return tc;

    }

    public boolean[][] getTc() {
        return tc;
    }

    // A recursive DFS traversal
    // function that finds
    // all reachable vertices for s
    private void dfsUtil(int s, int v) {

        // Mark reachability from
        // s to v as true.
        tc[s][v] = true;


        // Find all the vertices reachable
        // through v
        for (int adj : adjList[v]) {
            if (!tc[s][adj]) {
                dfsUtil(s, adj);
            }
        }
    }

    public void printRelationsMatrix() {
        System.out.println(this.getClass().getName());
        int n = vertices;
        for (int i = 0; i < n + 1; i++) {
            System.out.printf("%d\t", i - 1);
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.printf("%d\t", i);
            for (int j = 0; j < n; j++) {
                if (tc[i][j]) {
                    System.out.print("1\t");
                } else {
                    System.out.print("0\t");
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    // Driver Code
    public static void main(String[] args) {

        // Create a graph given
        // in the above diagram
        ClosureGraph g = new ClosureGraph(4);

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 3);
        System.out.println("Transitive closure " +
                "matrix is");

        g.transitiveClosure();

    }
}
