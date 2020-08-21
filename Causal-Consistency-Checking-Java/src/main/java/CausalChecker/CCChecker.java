package CausalChecker;

import BadPattern.BAD_PATTERN;
import CycleChecker.CycleChecker;
import DifferentiatedHistory.History;
import DifferentiatedHistory.HistoryItem;
import Relation.CausalOrder;
import Relation.ProgramOrder;
import Relation.ReadFrom;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CCChecker extends CausalChecker {

    public CCChecker(ProgramOrder PO, ReadFrom RF, CausalOrder CO, History history) {
        super(PO, RF, CO, history);
        this.initCCMap();
    }

    void initCCMap() {
        badMap.put(BAD_PATTERN.CyclicCO, false);
        badMap.put(BAD_PATTERN.ThinAirRead, false);
        badMap.put(BAD_PATTERN.WriteCOInitRead, false);
        badMap.put(BAD_PATTERN.WriteCORead, false);
    }

    protected void checkCC() {
        checkCyclicCO();
        checkThinAirRead();
        checkWriteCOInitRead();
        checkWriteCORead();
    }


    public void checkCausalConsistency() {
        checkLoggerInfo("Starting Check Causal Consistency");
        checkCC();
        printCheckStatus();
    }

    void checkCyclicCO() {
        checkLoggerInfo("Checking CyclicCO");
        boolean cyclic = CycleChecker.Cyclic(CO.getRelations());
        if (cyclic) {
            badMap.put(BAD_PATTERN.CyclicCO, true);
        }
    }

    void checkWriteCOInitRead() {
        checkLoggerInfo("Checking WriteCOInitRead");
        for (HistoryItem write : writeHistories) {
            for (HistoryItem read : readHistories) {
                if (!write.getK().equals(read.getK())) {
                    continue;
                }
                if (CO.isCO(write, read)) {
                    if (read.readInit()) {
                        // TODO: record more information
                        System.out.println("WriteCOInitRead in " + write + read);
                        badMap.put(BAD_PATTERN.WriteCOInitRead, true);
//                        return;
                    }
                }
            }
        }
    }

    void checkThinAirRead() {
        checkLoggerInfo("Checking ThinAirRead");
        boolean exists = false;
        for (HistoryItem read : readHistories) {
            int value = read.getV();
            if (value != 0 && value != -1) {
                for (HistoryItem write : writeHistories) {
                    if (RF.isRF(write, read)) {
                        exists = true;
                    }
                }
            }
        }
        if (!exists) {
            badMap.put(BAD_PATTERN.ThinAirRead, true);
        }
    }

    void checkWriteCORead() {
        checkLoggerInfo("Checking WriteCORead");
//        System.out.println("PO"+PO.isPO(434, 544));
//        System.out.println("RF"+RF.isRF(434, 544));

        for (HistoryItem w1 : writeHistories) {
            for (HistoryItem w2 : writeHistories) {
                if (w1.getK().equals(w2.getK())) {
                    for (HistoryItem r1 : readHistories) {
                        if (CO.isCO(w1, w2) && CO.isCO(w2, r1) && RF.isRF(w1, r1)) {
                            System.out.println("bad pattern find:");
                            System.out.println("w1 is " + w1);
                            System.out.println("w2 is " + w2);
                            System.out.println("r1 is " + r1);
                            badMap.put(BAD_PATTERN.WriteCORead, true);
                            return;
                        }
                    }
                }
            }
        }

    }

    @Override
    public void checkLoggerInfo(String message) {
        if(LOGGER){
            logger.info(message);
        }else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.out.println(df.format(new Date())+" " + message);
        }
    }

    @Override
    public void checkLoggerWarning(String message) {
        if(LOGGER){
            logger.warning(message);
        }else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.err.println(df.format(new Date())+" " + message);
        }
    }
}
