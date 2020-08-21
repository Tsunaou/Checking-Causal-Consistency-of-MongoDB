import DifferentiatedHistory.History;
import DifferentiatedHistory.HistoryItem;
import DifferentiatedHistory.HistoryReader;
import Relation.ProgramOrder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class POTest {

    @Test
    public void main() {
        String url = "history.edn";
        int concurrency = 10;
        HistoryReader reader = new HistoryReader(url, concurrency);
        try {
            History history = reader.readHistory();
            int lastIndex = history.getLastIndex();

            // get program order
            ProgramOrder PO = new ProgramOrder(lastIndex);
            PO.calculateProgramOrder(history, concurrency);
            // TODO: test whether PO is in the same process
            boolean[][] relations = PO.getRelations();
            int n = PO.getSize();
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    if(relations[i][j]){
                        HistoryItem it1 = history.getOperations().get(i);
                        HistoryItem it2 = history.getOperations().get(i);
                        assertEquals(it1.getProcess(),it2.getProcess());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}