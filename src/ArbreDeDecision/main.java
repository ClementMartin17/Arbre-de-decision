package ArbreDeDecision;

import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {
        Tree tree = new Tree();
        String dataFile = "src\\ArbreDeDecision\\donnees.txt";
        tree.Tree(dataFile);
    }
}
