package runmodels;

import runmodels.Node;
import runmodels.Tree;
import java.util.ArrayList;
import java.util.List;

public class BacktrackSimple extends AbstractAlgorithm {
    ArrayList<Vertex> current;
    Tree states;
    int numColors = 3;
    Vertex curVertex;
    int numNodes;
	
	public BacktrackSimple(ArrayList<Vertex> vertices) {
		System.out.println("Running the Simple Backtracking algorithm");
        this.current = vertices;
        this.states = new Tree();
        this.curVertex = current.get(0);
        this.numNodes = vertices.size();
		states.setRoot(new Node(null, vertices));
        actualAlgorithm();
        System.out.println("Totally done!");
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

	private ArrayList<Vertex> cloneList(List<Vertex> vList) {
		ArrayList<Vertex> clonedList = new ArrayList<Vertex>(vList.size());
		for (Vertex v : vList) {
			clonedList.add(new Vertex(v.getId(), v.getXLoc(), v.getYLoc(), v.getColor()));
		}
		return clonedList;
	}

	private ArrayList actualAlgorithm() {

        while(true) {  // all nodes colored return tree of states that got us here. TODO: make sure this colors last node.
            ArrayList<Vertex> newList = cloneList(current);
            chooseColorStupid(curVertex);
            states.addChild(newList);
            if (curVertex.getId() != numNodes) {
                curVertex = current.get(current.indexOf(curVertex) + 1); //next vertex
            }
            else{
                break;
            }
            actualAlgorithm(); //recursive call
        }
        return states.getPrev();
    }

    private void chooseColorStupid(Vertex v){ //simply choose first available color
        if (v.usableColors.size() == 0 && !v.getAllDeleted()) {
            v.createUsableColors(numColors);
        }
        v.setColor(v.usableColors.get(0));

    }
	
}
