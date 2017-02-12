package ArbreDeDecision;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tree {

    private List<Attribut> attributs;
    private List<Integer> trainingData = new ArrayList<Integer>();
    private List<Data> Data;
    private TreeNode root;

    public void Tree(String filename) throws IOException {

        this.loadData(filename);
        for (double Seuil = 0.0; Seuil <= Entropy(); Seuil += (Entropy() - 0.0)) {
            this.echantillonnage(10, Seuil);
        }
        this.display(root);
    }

    private double Entropy() {
        HashMap<String, Integer> classCount = new HashMap<String, Integer>();
        for (int i = 0; i < this.Data.size(); i++) {
            String count = Data.get(i).getValue(attributs.size() - 1);
            classCount.put(count, 1);
        }
        double entropy = 0;
        for (Integer i : classCount.values()) {
            entropy -= (i * 1.0 / this.Data.size()) * Math.log(i * 1.0 / this.Data.size());
        }
        return entropy;
    }

    private void display(TreeNode root) {
        System.out.println(root.getDisplayPrefix());
        if (root.getChildren() != null) {
            for (TreeNode child : root.getChildren().values()) {
                display(child);
            }
        }
    }

    public void loadData(String filename) throws FileNotFoundException, IOException {
        
            this.attributs = new ArrayList<Attribut>();
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line;
            boolean pouet = false;
            while ((line = in.readLine()) != null) {
                line.trim();
                String[] res = line.split(" ");
                if (res[0].equals("data")) {
                    pouet = true;
                    Data = new ArrayList<Data>();
                    continue;
                } else if (res[0].equals("attribut")) {
                    if (res[2].charAt(0) == '{' && res[2].charAt(res[2].length() - 1) == '}') {
                        Attribut attribute = new Attribut(res[1]);
                        attributs.add(attribute);
                    }
                } else {
                    if (pouet) {
                        String[] values = line.split(",");
                        Data data = new Data();
                        data.setAttribute(values);
                        Data.add(data);
                    }
                }
            }
            in.close();      
      
    }

    public void echantillonnage(int y, double Seuil) {
        HashMap<Integer, List<Integer>> groupe = new HashMap<Integer, List<Integer>>();
        for (int i = 0; i < Data.size(); i++) {
            groupe.put(i % y, new ArrayList<Integer>());
            groupe.get((i % y)).add(i);
        }
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < y; j++) {
                trainingData.addAll(groupe.get(j));
            }
            TreeNode root = new TreeNode(Data, attributs.size() - 1, attributs, 0, Seuil);
            this.root = root;
            for (Integer x : trainingData) {
                root.addRecord(x);
            }
            root.ConstrucTree();

        }
    }
}
