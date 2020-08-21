package Relation;

import DifferentiatedHistory.History;
import DifferentiatedHistory.HistoryItem;
import Operation.OP_TYPE;

import java.text.SimpleDateFormat;
import java.util.*;


public class ReadFrom extends PoSetMatrix {
    public ReadFrom(int size) {
        super(size);
    }

    public void calculateReadFrom(History history, int concurrency) {
        checkLoggerInfo("Calculating RF");
        LinkedList<HistoryItem> histories = history.getHistories();
        HashMap<String, HashMap<OP_TYPE, ArrayList<HistoryItem>>> groups = new HashMap<>();// group by key and func
        // group histories by key and type
        for (HistoryItem item : histories) {
            String key = item.getK();
            // TODO: improve here
            String func = item.getF();
            OP_TYPE type;
            if(func.equals(":write")){
                type = OP_TYPE.WRITE;
            }else if(func.equals(":read")){
                type = OP_TYPE.READ;
            }else{
                // TODO: throw exception
                System.out.println("Invalid Type");
                return;
            }

            if (groups.containsKey(key)) {
                if (groups.get(key).containsKey(type)) {
                    // key: exist, type: exist
                    groups.get(key).get(type).add(item);
                } else {
                    // key: exist, type: null
                    ArrayList<HistoryItem> list = new ArrayList<HistoryItem>();
                    list.add(item);
                    groups.get(key).put(type, list);
                }
            } else {
                // key: null, type: null
                HashMap<OP_TYPE, ArrayList<HistoryItem>> funcMap = new HashMap<OP_TYPE, ArrayList<HistoryItem>>();
                ArrayList<HistoryItem> list = new ArrayList<HistoryItem>();
                list.add(item);
                funcMap.put(type, list);
                groups.put(key, funcMap);
            }
        }
        // add sample read-from order
        for (Map.Entry<String, HashMap<OP_TYPE, ArrayList<HistoryItem>>> entry : groups.entrySet()) {
            String key = entry.getKey();
            ArrayList<HistoryItem> wlist = entry.getValue().get(OP_TYPE.WRITE);
            ArrayList<HistoryItem> rlist = entry.getValue().get(OP_TYPE.READ);
            if (wlist == null || rlist == null){
                continue;
            }
            for(HistoryItem write: wlist){
                for(HistoryItem read: rlist){
                    if(write.getV() == read.getV()){
                        int op1 = write.getIndex();
                        int op2 = read.getIndex();
                        this.addRelation(op1, op2);
                    }
                }
            }
        }
        // calculate transitive closure
//        this.calculateTransitiveClosure();
        this.setClose(true);

    }


    public boolean isRF(int i, int j){
        return isRelation(i, j);
    }

    public boolean isRF(HistoryItem it1, HistoryItem it2){
        return isRF(it1.getIndex(),it2.getIndex());
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
