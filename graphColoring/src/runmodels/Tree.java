package runmodels;

import java.lang.reflect.Array;
import java.util.ArrayList;
import runmodels.Vertex;

public class Tree {

    private Node root;
    private ArrayList<Node> prev;
    private Node last;

    public Tree() {
        prev = new ArrayList<Node>();
    }

    public Node getLast() {
        return last;
    }

    public ArrayList getPrev(){
        return prev;
    }

    public Node getRoot() {
        return this.root;
    }

    public void setRoot(Node root) {
        this.root = root;
        prev.add(root);
        last = root;
    }

    public void addChild(ArrayList<Vertex> vertices){
        Vertex v  = vertices.get(0); //fix this
        ArrayList<Vertex> clone = v.cloneList(vertices);
        if (root == null){
            setRoot(new Node(null, vertices));
        }
        else {
            Node newNode = new Node(last, clone);
            prev.add(newNode);
            last = newNode;
        }
    }

}