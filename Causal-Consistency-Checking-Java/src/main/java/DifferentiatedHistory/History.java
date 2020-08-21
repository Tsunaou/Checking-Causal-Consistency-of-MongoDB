package DifferentiatedHistory;

import Operation.OP_TYPE;

import java.awt.font.OpenType;
import java.util.*;

public class History {

    LinkedList<HistoryItem> histories;
    LinkedList<HistoryItem> writeHistories;
    LinkedList<HistoryItem> readHistories;
    ArrayList<HistoryItem> operations; // index -> operation(or say history)
    HashMap<String, HashSet<Integer>> writeGroupByKey;
    HashMap<String, HashSet<Integer>> readGroupByKey;
    HashMap<Integer, Integer> readFrom; // if rxv and wxv, than readFrom(rx) = wx(Index)
    HashSet<String> opKeySets;
    int lastIndex;

    public History(LinkedList<HistoryItem> histories) {
        this.histories = histories;
        this.initOperations();
        this.initWriteReadHistories();
        this.initOpGroupByKey();
        this.initReadFrom();
        this.lastIndex = histories.get(histories.size() - 1).getIndex(); // the max index in the  histories

    }

    private void initOperations() {
        operations = new ArrayList<HistoryItem>();
        operations.addAll(histories);
    }

    private void initWriteReadHistories() {
        writeHistories = new LinkedList<HistoryItem>();
        readHistories = new LinkedList<HistoryItem>();
        for (HistoryItem item : histories) {
            if (item.isWrite()) {
                writeHistories.add(item);
            }
            if (item.isRead()) {
                readHistories.add(item);
            }
        }
    }

    private void initOpGroupByKey() {
        this.readGroupByKey = new HashMap<>();
        this.writeGroupByKey = new HashMap<>();
        this.opKeySets = new HashSet<>();
        String curKey;
        HashMap<String, HashSet<Integer>> iter;
        for (HistoryItem item : histories) {
            curKey = item.getK();

            // Add curKey to keySet
            this.opKeySets.add(curKey);

            if (item.getOptype() == OP_TYPE.READ) {
                iter = readGroupByKey;
            } else if (item.getOptype() == OP_TYPE.WRITE) {
                iter = writeGroupByKey;
            } else {
                iter = writeGroupByKey;
                assert (false);
            }
            if (!iter.containsKey(curKey)) {
                iter.put(curKey, new HashSet<>());
            }
            iter.get(curKey).add(item.getIndex());
        }
    }

    private void initReadFrom() {
        this.readFrom = new HashMap<>();
        for (String curKey : this.opKeySets) {
            if (this.readGroupByKey.containsKey(curKey) && this.writeGroupByKey.containsKey(curKey)) {
                for (int i : this.readGroupByKey.get(curKey)) {
                    for (int j : this.writeGroupByKey.get(curKey)) {
                        HistoryItem read = operations.get(i);
                        HistoryItem write = operations.get(j);
                        if (read.getV() == write.getV()) {
                            this.readFrom.put(i, j);
                        }
                    }
                }
            }
        }

    }

    public LinkedList<HistoryItem> getHistories() {
        return histories;
    }

    public LinkedList<HistoryItem> getWriteHistories() {
        return writeHistories;
    }

    public LinkedList<HistoryItem> getReadHistories() {
        return readHistories;
    }

    public ArrayList<HistoryItem> getOperations() {
        return operations;
    }

    public HashMap<String, HashSet<Integer>> getWriteGroupByKey() {
        return writeGroupByKey;
    }

    public HashMap<String, HashSet<Integer>> getReadGroupByKey() {
        return readGroupByKey;
    }

    public HashMap<Integer, Integer> getReadFrom() {
        return readFrom;
    }

    public HashSet<String> getOpKeySets() {
        return opKeySets;
    }

    public int getLastIndex() {
        return lastIndex;
    }
}
