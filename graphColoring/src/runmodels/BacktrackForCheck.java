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
		this.curVertex = current.get(0);
		this.numNodes = vertices.size();

        for (Vertex v : vertices){
            v.createUsableColors(numColors);  // set number of colors available
        }
		algorithm(0);
		if (unsolvable){
			System.out.println("This graph is unsolvable!");
		}
		System.out.println("Done!");
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
			else if (curVertex.getId() != numNodes) {// check for all nodes colored
				curVertex = current.get(current.indexOf(curVertex) + 1); //next vertex
			}
			else{
				System.out.println("Breaking out of while loop.");
				break;
			}
		}
	}

	public void chooseColor(){
	    if (unsolvable){return;}
        System.out.println("Forward checking...");
        curVertex.setColor(curVertex.usableColors.get(0));  //first available color
        for (Vertex nei : curVertex.neighbors){         //delete all conflicting colors in neighbors
            if (nei.getId() > curVertex.getId()) {      //only look forward!
                nei.deleteColor(curVertex.getColor());
                if (nei.getAllDeleted()){
                    backtrackColor();


                }
            }
        }
    }

    public void backtrackColor(){
        curVertex.deleteColor(curVertex.getColor());  // delete unusable color
        if (curVertex.getId() == 1 && curVertex.getAllDeleted()){
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
            curVertex = current.get(current.indexOf(curVertex) - 1); //backtrack one vertex
            backtrackColor();
            chooseColor();
        }
    }
}
