package ArbreDeDecision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Node {

    private List<Data> dataSet;
    private List<Integer> allEchantillon;
    private int classificationLabelIndex;
    private double entropy = -1;
    private HashMap<String, Integer> classCounts = null;
    private String classification;

    public Node() {
    }

    public Node(List<Data> dataSet, int labelIndex) {
        this.allEchantillon = new ArrayList<Integer>();
        this.dataSet = dataSet;
        this.classificationLabelIndex = labelIndex;
        this.entropy = 0;
    }

    public List<Integer> getEchantillon() {
        return this.allEchantillon;
    }

    public List<Data> getDataSet() {
        return this.dataSet;
    }

    public void addRecord(int index) {
        allEchantillon.add(index);
    }

    public int getLabelIndex() {
        return this.classificationLabelIndex;
    }

    public HashMap<String, Integer> getClassCounts() {
        if (this.classCounts == null || this.classCounts.size() == 0) {
            this.updateClassCounts();
        }
        return classCounts;
    }

    public void updateClassCounts() {
        if (this.classCounts == null || this.classCounts.size() == 0) {
            classCounts = new HashMap<String, Integer>();
            for (Integer i : this.allEchantillon) {
                String curClass = this.dataSet.get(i).getValue(classificationLabelIndex);
                if (!classCounts.containsKey(curClass) ) {
                    classCounts.put(curClass, 1);
                } else {
                    classCounts.put(curClass, classCounts.get(curClass) + 1);
                }
            }
        }
    }

    public void updateEntropy() {
        updateClassCounts();
        double entropy = 0.0;
        int totalCounts = allEchantillon.size();
        for (Integer i : this.classCounts.values()) {
            double proportion = i * 1.0 / totalCounts;
            entropy += -1.0 * proportion * Math.log(proportion);
        }
        this.entropy = entropy;
    }

    public double getEntropy() {
        if (this.entropy == -1) {
            this.updateEntropy();
        }
        return this.entropy;
    }

    public String getClassification() {
        if (this.classification == null) {
            this.updateClassification();
        }
        return classification;
    }

    public void updateClassification() {
        int maxCount = 0;
        updateClassCounts();
        for (String name : this.classCounts.keySet()) {
            if (this.classCounts.get(name) > maxCount) {
                maxCount = this.classCounts.get(name);
                this.classification = name;
            }
        }
    }
}
