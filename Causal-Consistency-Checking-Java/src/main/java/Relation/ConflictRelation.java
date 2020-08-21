package Relation;

import DifferentiatedHistory.History;
import DifferentiatedHistory.HistoryItem;

import java.util.HashMap;
import java.util.HashSet;

public class ConflictRelation extends PoSetMatrix{

    public ConflictRelation(int size) {
        super(size);
    }

    public void calculateConflictRelation( CausalOrder CO, History history){
        System.out.println("Calculating CF");
        HashSet<Integer> rList = null;
        HashSet<Integer> wList = null;
        for(String x: history.getOpKeySets()){
            rList = history.getReadGroupByKey().get(x);
            wList = history.getWriteGroupByKey().get(x);
            if(rList!=null && wList!=null && wList.size()>=2){
                for(int r2: rList){
                    if(history.getReadFrom().containsKey(r2)){
                        int w2 = history.getReadFrom().get(r2); // index of w2
                        for(int w1: wList){
                            if(w1!=w2 && CO.isCO(w1, r2)){
                                this.addRelation(w1, w2);
                            }
                        }
                    }
                }
            }
        }
        this.printRelations();
    }

    public static void main(String[] args) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int j = map.get(1);
        System.out.println(j);
    }

}
