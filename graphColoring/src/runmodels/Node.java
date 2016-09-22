package runmodels;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

public class Node {
    private String id;
    private ArrayList state; //vertices as currently colored

    private final List<Node> children = new ArrayList<Node>();

    private final Node parent;

    public Node(Node parent, ArrayList state) {
        this.parent = parent;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public ArrayList getState(){return  state;}

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public void addChild(Node child){
        children.add(child);
    }

}