package runmodels;

import java.util.ArrayList;

public class BacktrackConProp extends AbstractAlgorithm { //May want to make this inherit from BacktrackSimple? Can we do that?
	ArrayList<Vertex> current; //vertices as currently colored
	int numColors = 4;  //total number of colors allowed
	Vertex curVertex; //cur vertex to color
	int numNodes;  //total number of vertices to color
	boolean unsolvable = false;


	public BacktrackConProp(ArrayList<Vertex> vertices) {
        System.out.println("Running the backtracking algorithm with MAC to " + numColors + " color graph with " + vertices.size() + " vertices");
		this.current = vertices;
        setCurGraph(current);
        this.curVertex = current.get(0);
		this.numNodes = vertices.size();
        System.out.println("Initial Graph");
        printGandC();

        preProcess(); //create arcs and set number of usable colors

		algorithm(0);
		if (unsolvable){
            System.out
                    .println("\nDid not find a " + numColors
                            + " coloring of graph");
		}
		else {
            System.out.println("\nFound a " + numColors + " coloring of graph");
        }
        System.out.println("Backtracking with MAC has finished running");
        System.out.println("Final Graph");
        printGandC();
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

	@Override
	protected int algorithm(int curV) {
		// TODO Auto-generated method stub
		actualAlgorithm();

		return 0;
	}

	public void preProcess(){
        for (Vertex v : current){
            v.createUsableColors(numColors);  // set number of colors available
            for (Vertex vt : current){        // set arcs up
                if (vt != v) {
                    v.addArc(vt);
                }
            }
        }
    }

	public void actualAlgorithm(){
		while(true){
			chooseColor();
			if (unsolvable){
				break;
			}
			else if (curVertex.getId() != numNodes -1 ) {// check for all nodes colored
				curVertex = current.get(current.indexOf(curVertex) + 1); //next vertex
			}
			else{
				break;
			}
		}
	}

	public void chooseColor(){
	    if(curVertex.getAllDeleted()){
            unsolvable = true; //hack
            return;
        }
        curVertex.setColor(curVertex.usableColors.get(0)); // first available color
        applyArc();
    }

    public void applyArc(){
        System.out.println("Enforcing arc consistency");
        for (Arc arc : curVertex.outArcs){
            if (arc.getTail().getNeighbors().contains(curVertex)){
                arc.getTail().deleteColor(curVertex.getColor());
                if (arc.getTail().getAllDeleted()){
                    backtrackColor();
                }
                //since something was deleted from tail, all arcs pointing to tail need to be reconsidered
                checkConsistency(arc.getTail());
            }
        }
    }

    public void checkConsistency(Vertex tail){
        for (Arc arc : tail.inArcs ){
            if (arc.getTail().getNeighbors().contains(arc.getStart()) && arc.getTail().usableColors.size() == 1) { //neighbors
                if (arc.getStart().getColor() == -1) {  //only prune unassigned colors
                    arc.getStart().deleteColor(arc.getTail().usableColors.get(0));
                    if (arc.getStart().getAllDeleted() && arc.getStart().getId() == 0){
                        unsolvable = true;
                        return;
                    }
                    else if (arc.getStart().getAllDeleted()){
                        backtrackColor();
                    }
                }
            }

        }
    }

    public void backtrackColor(){
        System.out.println("Backtracking a color");
        resetConsistency();
        curVertex.deleteColor(curVertex.getColor());
        if (curVertex.getAllDeleted() && curVertex.getId() == 0){
            unsolvable = true;
            return;
        }
        else if (curVertex.getAllDeleted()){
            backtrackLevel();
        }
        else{
            curVertex.setColor(curVertex.usableColors.get(0));
            applyArc();
        }
    }

    public void resetConsistency(){
        for (Arc arc : curVertex.outArcs){
            if (arc.getTail().getNeighbors().contains(curVertex)) {
                arc.getTail().addColor(curVertex.getColor());
            }
        }
    }

    public void backtrackLevel(){
        System.out.println("Backtracking a level");
        curVertex.setColor(-1);
        curVertex = current.get(current.indexOf(curVertex) -1);
        resetConsistency();
        curVertex.deleteColor(curVertex.getColor());
        if (curVertex.getAllDeleted() && curVertex.getId() == 0){
            unsolvable = true;
            return;
        }
        else if(curVertex.getAllDeleted()){
            unsolvable = true; //hack
            return;
        }
        curVertex.setColor(curVertex.usableColors.get(0));
        applyArc();

    }
}
