import java.util.ArrayList;

public class Node {
    String name;
    ArrayList<Node> edges;
    boolean searched;
    Node parent;

    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.searched = false;
        this.parent = null;
    }

    public void addEdge(Node neighbor) {
        this.edges.add(neighbor);
        neighbor.edges.add(this);
    }
}
