package runmodels;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    public void addChild(ArrayList vertices){
        Node newNode = new Node(last, vertices);
        prev.add(newNode);
        last = newNode;
    }

}