import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Graph {
    ArrayList<Node> nodes;
    Node start;
    Node end;
    HashMap<String, Node> graph;
    HashSet<Node> actorSet;
    public Graph() {
        this.graph = new HashMap<>();
        this.nodes = new ArrayList<>();
        this.start = null;
        this.end = null;
        this.actorSet = new HashSet<>();
    }

    public Node setStart(String title) {
        this.start = this.graph.get(title);
        return this.start;
    }

    public Node setEnd(String title) {
        this.end = this.graph.get(title);
        return this.end;
    }

    public void addNode(String title, Node n, boolean actor) {
        this.nodes.add(n);
        this.graph.put(title, n);
    }

    public Node getNode(String title) {
        return graph.get(title);
    }

    //Reset To Be Run Before Each Search
    public void softReset() {
        for (Node i : this.nodes) {
            i.searched = false;
            i.parent = null;
        }
    }

    //Reset Fpr Every New Download
    public void wipeGraph() {
        this.graph = new HashMap<>();
        this.nodes = new ArrayList<>();
    }
}
