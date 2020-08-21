package Relation;

import DifferentiatedHistory.History;

import java.util.HashMap;

public class HappenBefore {
    HashMap<Integer, HappenBeforeO> HB;

    public HappenBefore(int size, ProgramOrder PO, CausalOrder CO, History history) {
        this.HB = new HashMap<Integer, HappenBeforeO>();
        for (int o = 0; o < history.getOperations().size(); o++) {
            HappenBeforeO HBo = new HappenBeforeO(size, o);
            HBo.calculateHappenBefore(PO, CO, history);
//            System.out.println("-----------------------------------------------------------------------");
//            System.out.println("for operation" + history.getOperations().get(o));
//            HBo.printRelations();
            HB.put(o, HBo);
        }
    }

    public HappenBeforeO getHBo(int oIndex) {
        return HB.get(oIndex);
    }
}
