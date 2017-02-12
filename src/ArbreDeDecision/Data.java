package ArbreDeDecision;

import java.util.*;

public class Data {

    private List<String> values;

    public Data() {
        this.values = new ArrayList<String>();
    }

    public void setAttribute(String[] values) {
        for (int i = 0; i < values.length; i++) {
            this.values.add(values[i]);
        }
    }

    public String getValue(int index) {
       return( index < 0 || index >= values.size())? "":  values.get(index);
    }

}
