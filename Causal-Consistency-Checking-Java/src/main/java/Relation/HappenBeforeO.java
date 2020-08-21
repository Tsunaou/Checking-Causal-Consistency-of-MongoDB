package Relation;

import DifferentiatedHistory.History;
import DifferentiatedHistory.HistoryItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class HappenBeforeO extends PoSetMatrix {

    public int oIndex;

    public boolean[][] links;

    public HappenBeforeO(int size, int oIndex) {
        super(size);
        this.oIndex = oIndex;
        this.links = new boolean[size + 1][size + 1];
    }

    void update_HBo(int w1, int w2) {
        int n = this.getSize();
        addRelation(w1, w2);
        List<Integer> toW1 = new LinkedList<>();
        List<Integer> fromW2 = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (isHBo(i, w1)) {
                toW1.add(i);
            }
            if (isHBo(w2, i)) {
                fromW2.add(i);
            }
        }
        for (int x : toW1) {
            for (int y : fromW2) {
                if (!isHBo(x, y)) {
                    addRelation(x, y);
                }
            }
        }
    }


    void update_link(int w1, int w2) {
//        System.out.println("update link " + w1 + "," + w2);
        int n = this.getSize();
//        System.out.println("n is " + n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.links[i][j] = isHBo(i, j);
            }
        }
        this.links[w1][w2] = true;
        assert (!isHBo(w1, w2));
        List<Integer> toW1 = new LinkedList<>();
        List<Integer> fromW2 = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (isHBo(i, w1)) {
                toW1.add(i);
            }
            if (isHBo(w2, i)) {
                fromW2.add(i);
            }
        }
        for (int x : toW1) {
            for (int y : fromW2) {
                if (!isHBo(x, y)) {
                    this.links[x][y] = true;
                }
            }
        }

    }

    public void calculateHappenBefore(ProgramOrder PO, CausalOrder CO, History history) {
        checkLoggerInfo("Calculating HBo " + oIndex);
        // calculate CasualPast(o)
        LinkedList<Integer> causalPast = CO.CausalPast(oIndex);
        // CO|CausalPast(o) \subset HB_o
//        System.out.println("Causal Past");
//        for (int i : causalPast) {
//            System.out.print(i + ",");
//        }
//        System.out.println();
        for (int i : causalPast) {
            for (int j : causalPast) {
                if (CO.isCO(i, j)) {
                    addRelation(i, j);
                }
            }
        }

        // Definition
        HistoryItem o = history.getOperations().get(oIndex);
        LinkedList<HistoryItem> writeList = history.getWriteHistories();
        LinkedList<HistoryItem> readList = history.getReadHistories();
        boolean flag = true;
        int count_continue = 0;
        out:
        while (flag) {
            // First Closure
//            System.out.println("count_continue is " + count_continue);
            count_continue = count_continue + 1;
            flag = false;
//            calculateTransitiveClosure();
            HashSet<Integer> rList = null;
            HashSet<Integer> wList = null;
            for (String x : history.getOpKeySets()) {
                rList = history.getReadGroupByKey().get(x);
                if (rList != null) {
                    for (int i : rList) {
                        HistoryItem r2 = history.getOperations().get(i);
                        if (PO.isPOEQ(r2, o)) {
                            if (history.getReadFrom().containsKey(i)) {
                                HistoryItem w2 = history.getOperations().get(history.getReadFrom().get(i));
                                wList = history.getWriteGroupByKey().get(x);
                                if (wList != null) {
                                    for (int j : wList) {
                                        HistoryItem w1 = history.getOperations().get(j);
                                        int d1 = w1.getV();
                                        int d2 = w2.getV();
                                        if (d1 != d2 && !isHBo(w1, w2) && isHBo(w1, r2)) {
                                            update_HBo(w1.getIndex(), w2.getIndex());
//                                    System.out.printf("w1 is %d, w2 is %d, r2 is %d  ADD HBO (%d %d)\n",
//                                            w1.getIndex(), w2.getIndex(), r2.getIndex(), w1.getIndex(), w2.getIndex());
//                                    System.out.println("Find new HB " + w1.getIndex() + ", " + w2.getIndex());
                                            flag = true;
                                            continue out;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

//            for (HistoryItem w1 : writeList) {
//                for (HistoryItem w2 : writeList) {
//                    if ((w1.getK().equals(w2.getK())) && (w1.getV() != w2.getV())) {
//                        for (HistoryItem r2 : readList) {
//                            if (r2.getK().equals(w1.getK())) {
//                                if (isHBo(w1, r2) && PO.isPOEQ(r2, o) && (r2.getV() == w2.getV()) && !isHBo(w1, w2)) {
////                                    System.out.println("------- isHBo(" + w1.getIndex() + "," + w2.getIndex() + ") is " + isHBo(w1, w2));
////                                    update_link(w1.getIndex(), w2.getIndex());
////                                    addRelation(w1.getIndex(), w2.getIndex());
//                                    update_HBo(w1.getIndex(), w2.getIndex());
////                                    System.out.printf("w1 is %d, w2 is %d, r2 is %d  ADD HBO (%d %d)\n",
////                                            w1.getIndex(), w2.getIndex(), r2.getIndex(), w1.getIndex(), w2.getIndex());
////                                    System.out.println("Find new HB " + w1.getIndex() + ", " + w2.getIndex());
//                                    flag = true;
//                                    continue out;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
        // Second Closure
        calculateTransitiveClosure();
    }

    @Override
    public void calculateTransitiveClosure() {
        super.calculateTransitiveClosure();
        int n = this.getSize();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                assert (isHBo(i, j) == links[i][j]);
            }
        }
    }

    public boolean isHBo(int i, int j) {
        return isRelation(i, j);
    }

    public boolean isHBo(HistoryItem it1, HistoryItem it2) {
        return isHBo(it1.getIndex(), it2.getIndex());
    }

    @Override
    public void checkLoggerInfo(String message) {
        if (LOGGER) {
            logger.info(message);
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.out.println(df.format(new Date())+" " + message);
        }
    }

    @Override
    public void checkLoggerWarning(String message) {
        if (LOGGER) {
            logger.warning(message);
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.out.println(df.format(new Date())+" " + message);
        }
    }

    public static void main(String[] args) {

    }
}
