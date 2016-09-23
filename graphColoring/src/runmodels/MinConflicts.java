package runmodels;

import java.util.ArrayList;
import java.util.Random;

public class MinConflicts extends AbstractAlgorithm {

	public MinConflicts(ArrayList<Vertex> vertices) {
		System.out.println("Running the Min Conflicts algorithm");

		// tunable parameter, corresponds to the maximum number of steps before giving up
		int maxIterations = 50;
		
		// desired number of colors in the final solution (valid choices k = 3, k = 4)
		int kColors = 3;
		
		// A random number generator to stochastically get colors for vertices of graph
		Random rand = new Random();
		
		int numConflictsInEntireGraph = Integer.MIN_VALUE;
		
		// generate a complete assignment for graph coloring problem (initially random)
		for (int i = 0; i < vertices.size(); i++) {
			int randomVertexColor = rand.nextInt(kColors);	
			vertices.get(i).setColor(randomVertexColor);
		}
		
		
		for (int iterations = 0; iterations < maxIterations; iterations++) {
			// if current is a solution for csp, return current
			// we know current is a solution if there are no conflicts in the graph
			numConflictsInEntireGraph = getCurConflicts(vertices);
			
			System.out.println(numConflictsInEntireGraph);
			
			for (Vertex v: vertices) {
				
				
			} // end 
	
			// chose a randomly chosen conflicted variable from csp.variables
			
			
			
			// value the value v for far that minimizes conflicts (var, v, current, csp) - break ties randomly
			
			
			// set var = value in current
			
			
			//return failure
			
			
			// tabu search to help avoid plateaus 
			
		}
		
		
				
		// print colors, for checking
		System.out.println("Colors :");
		for (int i = 0; i < vertices.size(); i++) {
			System.out.println("Vertex " + vertices.get(i).getId() + " : " + vertices.get(i).getColor());
		}
		
		
		System.out.println("Vertices :");
		for (int i = 0; i < vertices.size(); i++) {
			// System.out.println(vertices.get(i).getId());
			System.out.print(vertices.get(i).getId() + ", ");
		}

		System.out.println();

		// and the edges
		System.out.println("Edges: ");
		for (Vertex v : vertices) {
			for (int i = 0; i < v.neighbors.size(); i++) {
				// System.out.println(v.getId() + "," +
				// v.getNeighbors().get(i).getId());
				System.out.print(v.getId() + ","
						+ v.getNeighbors().get(i).getId() + "; ");
			}
		}

	}

	@Override
	protected int algorithm(int curV) {
		// TODO Auto-generated method stub
		
		return 0;
	}

	
	public ArrayList<Integer> runAlgo(){
		return conflictHistory;
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
	
	public int getCurConflicts(ArrayList<Vertex> vertices) {
		int numConflicts = 0; 
		
		for (Vertex v: vertices) {
			numConflicts += v.checkConflicts().getId();

		}
		
		return curConflicts;
	}
	

}
