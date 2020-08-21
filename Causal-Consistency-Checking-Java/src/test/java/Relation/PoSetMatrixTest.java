package Relation;

import junit.framework.TestCase;

import java.util.Random;

import java.util.ArrayList;

public class PoSetMatrixTest extends TestCase {

    public void testCalculateTransitiveClosure() {
        int n = 1500;
        PoSetMatrix r1 = new PoSetMatrix(n);
        Random random = new Random();
        for (int i = 0; i < Math.sqrt(n*n); i++) {
            int a = random.nextInt(n);
            int b = random.nextInt(n);
            if (a != b) {
                r1.addRelation(a, b);
            }
        }
        r1.addRelation(1,3);
        r1.addRelation(3,2);
        r1.addRelation(2,1);


        long s1 = System.currentTimeMillis();
        r1.transitiveClosure();
        boolean[][] x = r1.getTc();
        long s2 = System.currentTimeMillis();
        r1.calculateTransitiveClosure();
        boolean[][] y = r1.getRelations(true);
        long s3 = System.currentTimeMillis();

        System.out.println("dfs costs " + (s2 - s1) + " ms");

//        r1.printRelationsMatrix();
//        g.printRelationsMatrix();

        // test
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                assertEquals(x[i][j], y[i][j]);
            }
        }
    }
}