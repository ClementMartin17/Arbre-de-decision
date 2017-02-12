package ArbreDeDecision;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TreeNode extends Node {

    private List<Attribut> attributes;
    private int nextSplitAttrIdx;
    private double Seuil = 0.0;
    private HashMap<String, TreeNode> children;
    private HashSet<Integer> splittedAttrs;
    private int level;
    private String displayPrefix;

    public TreeNode() {
        super();
    }

    public TreeNode(List<Data> dataSet, int labelIndex,
            List<Attribut> attributes, int level, double Seuil) {
        super(dataSet, labelIndex);
        this.attributes = attributes;
        splittedAttrs = new HashSet<Integer>();
        this.level = level;
        this.displayPrefix = "";
        this.children = null;
        this.Seuil = Seuil;
    }

    private double Entropy(List<Integer> list) {
        double entropy = 0.0;
        HashMap<String, Integer> map = ClassCount(list);
        int totalCounts = list.size();

        for (Integer i : map.values()) {
            double proportion = i * 1.0 / totalCounts;
            entropy += -1.0 * proportion * Math.log(proportion);
        }
        return entropy;
    }
///fonction ultra moche!!!
    public void ConstrucTree() {
        this.updateEntropy();
        if (super.getEntropy() > this.Seuil && this.getEchantillon().size() > 1) {
            this.nextSplitAttrIdx = -1;
            double minEntropy = Double.MAX_VALUE;
            for (int attrIndex = 0; attrIndex < attributes.size(); attrIndex++) {
                if (splittedAttrs.contains(attrIndex) || attrIndex == super.getLabelIndex()) {
                    continue;
                }
                double[] derp = new double[1];
                HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();
                HashMap<String, Integer> distribution = new HashMap<String, Integer>();
                List<Data> dataSet = super.getDataSet();
                for (Integer dataIndex : super.getEchantillon()) {
                    String attribut = dataSet.get(dataIndex).getValue(attrIndex);
                    if (map.containsKey(attribut)) {
                        map.get(attribut).add(dataIndex);
                    } else {
                        List<Integer> list = new ArrayList<Integer>();
                        list.add(dataIndex);
                        map.put(attribut, list);
                    }
                    if (!distribution.containsKey(attribut)) {
                        distribution.put(attribut, 1);
                    }
                }
                double entropy = 0.0;
                for (List<Integer> list : map.values()) {
                    entropy += Entropy(list) * list.size()
                            / super.getEchantillon().size();
                }
                double gain = 0;
                for (String attr : distribution.keySet()) {
                    double curRatio = distribution.get(attr) * 1.0
                            / super.getEchantillon().size();
                    gain = -1.0 * curRatio * Math.log(curRatio);
                }
                if (!(gain > 0)) {
                    entropy = Double.MAX_VALUE;
                }
                derp[0] = entropy;

                if (derp[0] < minEntropy) {
                    minEntropy = derp[0];
                    this.nextSplitAttrIdx = attrIndex;
                }
            }
            children = new HashMap<String, TreeNode>();
            for (Integer i : this.getEchantillon()) {
                Data data = this.getDataSet().get(i);
                String curValue = data.getValue(this.nextSplitAttrIdx);
                if (children.containsKey(curValue)) {
                    children.get(curValue).addRecord(i);
                } else {
                    TreeNode insert = new TreeNode(super.getDataSet(),
                            super.getLabelIndex(), attributes, this.level + 1,
                            Seuil);
                    for (int j = 0; j < this.level; j++) {
                        insert.displayPrefix += "-";
                    }
                    insert.displayPrefix += attributes.get(this.nextSplitAttrIdx)
                            .getName();
                    insert.displayPrefix += " : ";
                    insert.displayPrefix += curValue;
                    children.put(curValue, insert);
                }
            }
            for (TreeNode child : this.children.values()) {
                child.ConstrucTree();
            }
        } else {
            displayPrefix += System.getProperty("line.separator");;
            for (int j = 0; j < level; j++) {
                displayPrefix += "=";
            }
            displayPrefix += ">";
            displayPrefix += super.getClassification();
        }
    }

    public String getDisplayPrefix() {
        return this.displayPrefix;
    }

    public HashMap<String, TreeNode> getChildren() {
        return this.children;
    }

    private HashMap<String, Integer> ClassCount(List<Integer> list) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (int i : list) {
            String curClass = super.getDataSet().get(i)
                    .getValue(super.getLabelIndex());
            if (!map.containsKey(curClass)) {
                map.put(curClass, 1);
            } else {
                map.put(curClass, map.get(curClass) + 1);
            }

        }
        return map;
    }

}
