package CycleChecker;

import CycleChecker.Johnson.ElementaryCyclesSearch;
import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.*;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.List;

public class CycleChecker {

    public static boolean CyclicHBo(boolean[][] relations, int index) {
        return Cyclic(relations);
    }

    public static boolean Cyclic(boolean[][] relations) {
        for (int i = 0; i < relations.length; i++) {
            if (relations[i][i]) {
                return true;
            }
        }
        return false;

//        int n = relations.length;
//        String[] nodes = new String[n];
//        Graph<Integer, DefaultEdge> directedGraph = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
//        for (int i = 0; i < n; i++) {
//            directedGraph.addVertex(i);
//        }
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                if (relations[i][j]) {
//                    directedGraph.addEdge(i, j);
//
//                }
//            }
//        }

//        CycleDetector<Integer, DefaultEdge> detector = new CycleDetector<Integer, DefaultEdge>(directedGraph);
//        boolean cyclic = false;
//        HawickJamesSimpleCycles<Integer, DefaultEdge> finder = new HawickJamesSimpleCycles<Integer, DefaultEdge>(directedGraph);
//        List<List<Integer>> cycles = finder.findSimpleCycles();
//        for (List<Integer> list : cycles) {
//            if (list.size() > 1) {
//                for (Integer v : list) {
//                    System.out.print(v + " ");
//                }
//                System.out.println();
//                cyclic = true;
//            }
//        }
//        if (cyclic) {
//            System.out.println("==============================================");
//        }
//        return cyclic;
    }

    @Deprecated
    public static boolean CyclicOld(boolean[][] relations) {
        int n = relations.length;
        String[] nodes = new String[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = "Node " + i;
        }

        ElementaryCyclesSearch ecs = new ElementaryCyclesSearch(relations, nodes);
        List cycles = ecs.getElementaryCycles();
        boolean cyclicFlag = false;
        for (int i = 0; i < cycles.size(); i++) {
            List cycle = (List) cycles.get(i);
            for (int j = 0; j < cycle.size(); j++) {
                String node = (String) cycle.get(j);
                if (j < cycle.size() - 1) {
//                    System.out.print(node + " -> ");
                    return true;
                } else {
//                    System.out.print(node);
                    return true;
                }
            }
//            System.out.print("\n");
        }
        return cyclicFlag;
    }

    public static void main(String[] args) {

    }
}
