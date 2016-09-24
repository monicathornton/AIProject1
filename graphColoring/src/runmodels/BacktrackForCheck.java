package runmodels;

import java.util.ArrayList;

public class BacktrackForCheck extends AbstractAlgorithm {
	ArrayList<Vertex> current; //vertices as currently colored
	int numColors = 3;  //total number of colors allowed
	Vertex curVertex; //cur vertex to color
	int numNodes;  //total number of vertices to color
	boolean unsolvable = false;

	public BacktrackForCheck(ArrayList<Vertex> vertices) {
		System.out.println("Running the Backtracking algorithm with forward checking");
		this.current = vertices;
        setCurGraph(current);
        this.curVertex = current.get(0);
		this.numNodes = vertices.size();

        System.out.println("Initial Graph");
        printGandC();

        for (Vertex v : vertices){
            v.createUsableColors(numColors);  // set number of colors available
        }
		algorithm(0);
        if (unsolvable){
            System.out
                    .println("\nDid not find a " + numColors
                            + " coloring of graph");
        }
        else {
            System.out.println("Found a " + numColors + " coloring of graph");
        }
        System.out.println("Backtracking with Forward Checking has finished running");
        System.out.println("Final Graph");
        printGandC();
	}

	@Override
	protected int algorithm(int curV) {
		// TODO Auto-generated method stub
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

	public void actualAlgorithm(){
		while(true){
			chooseColor();
			if (unsolvable){
				break;
			}
			else if (curVertex.getId() != numNodes -1) {// check for all nodes colored
				curVertex = current.get(current.indexOf(curVertex) + 1); //next vertex
			}
			else{
				break;
			}
		}
	}

	public void chooseColor(){
	    if (unsolvable){return;}
        if (curVertex.getAllDeleted()){unsolvable = true; return;}  //Hack
        curVertex.setColor(curVertex.usableColors.get(0));  //first available color
        System.out.println("Forward Checking on Vertex " + curVertex.getId() + ":" + curVertex.getCurrentColor());
        for (Vertex nei : curVertex.neighbors){         //delete all conflicting colors in neighbors
            if (nei.getId() > curVertex.getId()) {      //only look forward!
                nei.deleteColor(curVertex.getColor());
                System.out.println("After forward check on neighbor " + nei.getId() + ":" + nei.printColors());
                if (nei.getAllDeleted()){
                    backtrackColor();
                }
            }
        }
    }

    public void backtrackColor(){
        System.out.println("Backtracking a color on Vertex " + curVertex.getId());

        curVertex.deleteColor(curVertex.getColor());  // delete unusable color
        if (curVertex.getId() == 0 && curVertex.getAllDeleted()){
            unsolvable = true;
            return;
        }
        for (Vertex nei : curVertex.getNeighbors() ){       // reset all neighbors
            if (nei.getId() > curVertex.getId()){
                nei.addColor(curVertex.getColor());
            }
        }
        curVertex.setColor(-1);                        //reset color
        if (curVertex.getAllDeleted()){
            System.out.println("Backtracking a level on Vertex " + curVertex.getId());
            curVertex = current.get(current.indexOf(curVertex) - 1); //backtrack one vertex
            backtrackColor();
            chooseColor();
        }
    }
}
