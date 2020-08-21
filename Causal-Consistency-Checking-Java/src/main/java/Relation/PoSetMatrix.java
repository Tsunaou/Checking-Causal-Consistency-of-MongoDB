package Relation;

import CausalLogger.CheckerWithLogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PoSetMatrix implements PoSet, CheckerWithLogger {

    private boolean[][] relations;
    private boolean[][] tc;
    private boolean isClose;  // whether the transitive closure is calculate
    private int size;
    protected Logger logger;
    private boolean NO_LOGGER = false;

    // adjacency list
    private LinkedList<Integer>[] adjList;

    public PoSetMatrix(int size) {
        int n = size + 1;
        this.relations = new boolean[n][n];
        this.tc = new boolean[n][n];
        this.size = n;
        this.isClose = false;
        this.logger = Logger.getLogger(this.getClass().getName());
        this.logger.setLevel(Level.ALL);
        initAdjList();
    }

    // utility method to initialise adjacency list
    @SuppressWarnings("unchecked")
    private void initAdjList() {
        adjList = new LinkedList[size];
        for (int i = 0; i < size; i++) {
            adjList[i] = new LinkedList<Integer>();
        }
    }

    public void addRelation(int a, int b) {
        if (!relations[a][b]) {
            relations[a][b] = true;
            // Add v to u's list.
            adjList[a].add(b);
        }
    }

    @Deprecated
    public void addNewLink(int a, int b) {
        assert (relations[a][b]);
        int n = this.size;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (relations[i][a] && relations[b][j] && !relations[i][j]) {
                    relations[i][j] = true;
                }
            }
        }
    }

    public void calculateTransitiveClosure() {
//        long start = System.currentTimeMillis();
        int n = this.size;
        this.transitiveClosure();
//        for (int k = 0; k < n; k++) {
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < n; j++) {
//                    if (relations[i][j]) {
//                        continue;
//                    }
//                    relations[i][j] = relations[i][k] && relations[k][j];
//                }
//            }
//        }
        // check
//        for (int i = 0; i < n; i++) {
//            assert (!relations[i][i]);
//        }
        isClose = true;
//        long end = System.currentTimeMillis();
//        System.out.println("Closure cost " + (end - start) + "ms");
    }

    public void transitiveClosure() {

        // Call the recursive helper
        // function to print DFS
        // traversal starting from all
        // vertices one by one
        for (int i = 0; i < size; i++) {
            for (int adj : adjList[i]) {
                if (!tc[i][adj]) {
                    dfsUtil(i, adj);
                }
            }
        }

        this.relations = this.tc;
//        this.tc = new boolean[size][size];

    }

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

    public void printRelations() {
        int n = size;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (relations[i][j]) {
                    System.out.printf("(%d, %d), ", i, j);
                    count = count + 1;
                }
            }
        }
        if (count != 0) {
            System.out.println();
        }
//        System.out.println(this.getClass().getName() + " has " + count + " relations");
    }

    public void printRelationsMatrix() {
        System.out.println(this.getClass().getName());
        int n = size;
        for (int i = 0; i < n + 1; i++) {
            System.out.printf("%d\t", i - 1);
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.printf("%d\t", i);
            for (int j = 0; j < n; j++) {
                if (relations[i][j]) {
                    System.out.print("1\t");
                } else {
                    System.out.print("0\t");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean[][] getRelations() {
        if (isClose) {
            return relations;
        } else {
            System.out.println("Transitive closure is not calculated");
            return null;
        }
    }

    public boolean[][] getRelations(boolean force) {
        if (force) {
            return relations;
        } else {
            System.out.println("Transitive closure is not calculated");
            return null;
        }
    }

    public boolean[][] getTc() {
        return tc;
    }

    public void union(PoSetMatrix s1, PoSetMatrix s2) {
        assert (s1.size == s2.size);
        assert (size == s1.size);

        boolean[][] r1 = s1.getRelations();
        boolean[][] r2 = s2.getRelations();

        int n = size;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (r1[i][j] || r2[i][j]) {
                    addRelation(i, j);
                }
            }
        }

    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public int getSize() {
        return size;
    }

    public boolean isRelation(int i, int j) {
        return relations[i][j];
    }

    @Override
    public void checkLoggerInfo(String message) {
        if (LOGGER) {
            logger.info(message);
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.out.println(df.format(new Date()) + " " + message);
        }
    }

    @Override
    public void checkLoggerWarning(String message) {
        if (LOGGER) {
            logger.warning(message);
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.out.println(df.format(new Date()) + " " + message);
        }
    }

}
