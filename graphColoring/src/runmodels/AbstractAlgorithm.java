package runmodels;

import java.util.ArrayList;

public abstract class AbstractAlgorithm {
	/*BackTracking and MinConflicts share the following functions (though they may do different things):
	 * Check solution for validity
	 * Select Variable (Unassigned for backtracking, conflicting for minConflicts)
	 * Assignment of color to variable
	 * Find conflicting values (failure for backtracking, all current conflicts for minconflicts
	 * 
	 *Also need to track:
	 * iterations (and compare to maxIterations) and 
	 * conflicts at each iteration
	 */
	protected int curIterations = 0; 
	protected int maxIterations = 0;
	protected int curConflicts;
	protected ArrayList<Integer> conflictHistory;//Tracks conflicts/blank values at each iteration
	protected ArrayList<Vertex> curGraph;
	
	public ArrayList<Integer> runAlgo(){ //Template Method demands I write an algorithm skellington: Maybe override for GA?
		ArrayList<Vertex> conflicts = findConflicts(curGraph);
		while(curIterations < maxIterations){
			conflicts = findConflicts(curGraph);
			curConflicts = conflicts.size();
			conflictHistory.add(curConflicts);
			if(curConflicts == 0){
				return conflictHistory; //Algorithm done
			}
			int curV= selectVertex();
			int color = algorithm(curV);
			assignColorToVertex(curV, color);
			
			curIterations++;
		}
		return conflictHistory;//Algorithm fails
	}
	protected ArrayList<Vertex> findConflicts(ArrayList<Vertex> graph){//will implement later! 
		//Returns a list of pairs of indices of vertices that conflict
		ArrayList<Vertex> con = new ArrayList<Vertex>();
		for(Vertex v : graph){
			ArrayList<Vertex> neighbors = v.getNeighbors();
			for(Vertex nei :  neighbors){
				if(nei.getColor() == v.getColor()){
					con.add(nei);
				}
			}
		}
		return con;
	}
	protected abstract int algorithm(int curV);//detects conflicts, finds required values, etc. returns color
	protected abstract int selectVertex(); //Selects unassigned/conflicting/random variable(vertex) and returns the index in the current graph
	protected abstract void assignColorToVertex(int v, int c); //assigns a color c to a vertex at index v in curGraph
	//TODO What other functions to add, you guys?
	
	public void printGandC(){
		//prints graph and coloring
		//print coloring first
		System.out.println("Printing coloring");
		for(Vertex v : curGraph){
		
			System.out.println("Vertex: " + v.getId() + ", Color:" + v.getColor());
		}
		int count = 0;
		System.out.println("Printing graph");
		for(int i = 0; i < curGraph.size(); i++){
			for(int j = 0; j < curGraph.get(i).neighbors.size(); j++){
				
				System.out.print(curGraph.get(i).getId() + " -> " + curGraph.get(i).neighbors.get(j).getId() + ", ");
				count++;
				if(count == 15){
					System.out.println();
					count = 0;
				}
			}
			
		}
		
	}
	public int getCurIterations() {
		return curIterations;
	}
	public void setCurIterations(int curIterations) {
		this.curIterations = curIterations;
	}
	public int getMaxIterations() {
		return maxIterations;
	}
	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}
	public int getCurConflicts() {
		return curConflicts;
	}
	public void setCurConflicts(int curConflicts) {
		this.curConflicts = curConflicts;
	}
	public ArrayList<Integer> getConflictHistory() {
		return conflictHistory;
	}
	public void setConflictHistory(ArrayList<Integer> conflictHistory) {
		this.conflictHistory = conflictHistory;
	}
	public ArrayList<Vertex> getCurGraph() {
		return curGraph;
	}
	public void setCurGraph(ArrayList<Vertex> curGraph) {
		this.curGraph = curGraph;
	}
	
	
	
}
