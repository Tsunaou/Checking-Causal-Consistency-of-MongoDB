import CausalChecker.CCvChecker;
import CausalChecker.CMChecker;
import CausalLogger.CheckerWithLogger;
import DifferentiatedHistory.History;
import DifferentiatedHistory.HistoryReader;
import Relation.CausalOrder;
import Relation.ProgramOrder;
import Relation.ReadFrom;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checker implements CheckerWithLogger {

    String url;
    int concurrency;
    HistoryReader reader;
    boolean file;
    int maxIndex;
    protected Logger logger;


    public Checker(String url, int concurrency) {
        this.url = url;
        this.concurrency = concurrency;
        this.reader = new HistoryReader(url, concurrency);
        this.maxIndex = Integer.MAX_VALUE;
        this.logger = Logger.getLogger(this.getClass().getName());
        this.logger.setLevel(Level.ALL);
    }

    public Checker(String url, int concurrency, boolean file) {
        this.url = url;
        this.concurrency = concurrency;
        this.reader = new HistoryReader(url, concurrency, file);
        this.maxIndex = Integer.MAX_VALUE;
        this.logger = Logger.getLogger(this.getClass().getName());
        this.logger.setLevel(Level.ALL);
    }

    public Checker(String url, int concurrency, boolean file, int maxIndex) {
        this.url = url;
        this.concurrency = concurrency;
        this.reader = new HistoryReader(url, concurrency, file);
        this.maxIndex = maxIndex;
        this.logger = Logger.getLogger(this.getClass().getName());
        this.logger.setLevel(Level.ALL);
    }

    public void checkCausal(boolean CCv, boolean CM) {
        try {
            History history = reader.readHistory(maxIndex);
            int lastIndex = history.getLastIndex();
            System.err.println("LastIndex is " + lastIndex);
            for(int i=0;i<=lastIndex;i++){
                System.out.println(history.getOperations().get(i));
            }
            // get program order
            ProgramOrder PO = new ProgramOrder(lastIndex);
            PO.calculateProgramOrder(history, concurrency);
            PO.printRelations();
            // get read-from
            ReadFrom RF = new ReadFrom(lastIndex);
            RF.calculateReadFrom(history, concurrency);
            RF.printRelations();
            // get causal order
            CausalOrder CO = new CausalOrder(lastIndex);
            CO.calculateCausalOrder(PO, RF);
            CO.printRelations();
            if (CCv) {
                // Causal Convergence checker
                CCvChecker ccvChecker = new CCvChecker(PO, RF, CO, history);
                ccvChecker.checkCausalConvergence();
            }
            if (CM) {
                // Causal Memory checker
                CMChecker cmChecker = new CMChecker(PO, RF, CO, history);
                cmChecker.checkCausalMemory();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkCausalConvergence() {
        checkCausal(true, false);
    }

    public void checkCausalMemory() {
        checkCausal(false, true);
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        int concurrency = 100;
//        String url = "/home/young/Desktop/NJU-Bachelor/Causal-Memory-Checking-Java/src/main/resources/adhoc/paper_history_c.edn";
//        String url = "/home/young/Desktop/NJU-Bachelor/Causal-Memory-Checking-Java/src/main/resources/history.edn";
//        String url = "/home/young/Desktop/NJU-Bachelor/Causal-Memory-Checking-Java/src/main/resources/latest/history.edn";
        String url = "E:\\Causal-Memory-Checking-Java\\src\\main\\resources\\adhoc\\paper_history_e.edn";
//        String url = "E:\\Causal-Memory-Checking-Java\\src\\main\\resources\\latest\\history.edn";

        boolean file = false;
        boolean typeCCv = true;
        int maxIndex = Integer.MAX_VALUE;
        maxIndex = 2000;
        if (args.length == 3 && args[0].matches("\\d+")) {
            concurrency = Integer.parseInt(args[0]);
            url = args[1];
            file = true;
            if (args[2].equals("CM")) {
                typeCCv = false;
            }
        }
        if (args.length == 4 && args[0].matches("\\d+") && args[3].matches("\\d+")) {
            concurrency = Integer.parseInt(args[0]);
            url = args[1];
            file = true;
            if (args[2].equals("CM")) {
                typeCCv = false;
            }
            maxIndex = Integer.parseInt(args[3]);
        }

        Checker cheker = new Checker(url, concurrency, file, maxIndex);
        if (typeCCv) {
            cheker.checkCausalConvergence();
        } else {
            cheker.checkCausalMemory();
        }

        long end = System.currentTimeMillis();

        cheker.checkLoggerInfo("Cost " + (end-start) + " ms");
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
}
