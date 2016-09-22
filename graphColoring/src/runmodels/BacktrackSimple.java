package runmodels;

import runmodels.Node;
import runmodels.Tree;
import java.util.ArrayList;
import java.util.List;

public class BacktrackSimple extends AbstractAlgorithm {
    ArrayList<Vertex> current; //vertices as currently colored
    Tree states; //tree of states, where each state depends on color of vertices
    int numColors = 3;  //total number of colors allowed
    Vertex curVertex; //cur vertex to color
    int numNodes;  //total number of vertices to color
    boolean unsolvable = false;
	
	public BacktrackSimple(ArrayList<Vertex> vertices) {
		System.out.println("Running the Simple Backtracking algorithm");
        this.current = vertices;
//        this.states = new Tree();
        this.curVertex = current.get(0);
        this.numNodes = vertices.size();

        actualAlgorithm();
        if (unsolvable){
            System.out.println("This graph is unsolvable!");
        }
        System.out.println("Done!");
    }

	@Override
	protected int algorithm(int curV) {
		actualAlgorithm();
		return 0;
	}

	@Override
	protected int selectVertex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void assignColorToVertex(int v, int c) {
		// TODO Auto-generated method stub
		
	}

	private void actualAlgorithm() {

        while(true) {
            chooseColorStupid();  //choose first available color
//            states.add(current); //add state to tree
            if (unsolvable){
                break;
            }
            else if (curVertex.getId() != numNodes) {// check for all nodes colored
                curVertex = current.get(current.indexOf(curVertex) + 1); //next vertex
            }
            else{ //TODO: return list of states that got us here.
                break;
            }
            actualAlgorithm(); //recursive call TODO: erasable...?
        }

    }

    private void chooseColorStupid(){
        if (curVertex.getAllDeleted()) {
            backtrackLevel();
        }
        else if (curVertex.usableColors.size() == 0) {
            curVertex.createUsableColors(numColors);  // set number of colors available
        }
        if (unsolvable){
            return;
        }
        curVertex.setColor(curVertex.usableColors.get(0)); //set first color in list
        Vertex conflict = curVertex.checkConflicts();  //check for a single conflicting neighboring color
        if (conflict != null){
            backtrackColor(conflict);
        }
    }

    private void backtrackColor(Vertex conflict){
        curVertex.deleteColor(conflict.getColor());  //delete conflicting color from possible choices
        chooseColorStupid();   //call again
    }

    private void backtrackLevel(){
        System.out.println("Backtracking one level.");
        if (curVertex.getId() == 1){
            unsolvable = true;
            return;
        }
        Vertex curNextI = current.get(curVertex.getId() -2);  //taking into account 0-based index. indexOf(curVertext may work now)
        curNextI.deleteColor(curNextI.getColor()); // color won't work

        //reset curVertex
        curVertex.setColor(-1);
        curVertex.createUsableColors(numColors);
        curVertex.setAllDeleted();

        curVertex = curNextI; //set curVertex back one
//        current = states.backtrack();

        actualAlgorithm(); //start over from here
    }
}
