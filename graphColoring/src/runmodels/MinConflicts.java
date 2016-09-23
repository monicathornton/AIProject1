package runmodels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MinConflicts extends AbstractAlgorithm {

	// a list containing all of the vertices for the given problem
	ArrayList<Vertex> vertices = new ArrayList<Vertex>();

	// tunable parameter, corresponds to the maximum number of steps before
	// giving up
	int maxIterations = 10000;

	// keeps track of the number of iterations, for comparing performance and
	// monitoring steps in relation to maxIterations
	int numIterations = 0;

	// desired number of colors in the final solution (valid choices k = 3, k =
	// 4)
	int kColors = 4;

	// note: because conflicts is counted once for each vertex with a conflict
	// (and edges are
	// not directed), the true number of conflicts in the graph is this number
	// halved
	int numConflictsInEntireGraph;

	// variable used to indicate if the graph was solved with the specified
	// coloring in <= # max iterations
	boolean unsolvable = false;

	// variables necessary for the tabu list, which helps to direct the search
	int tabuListMaxSize = 0;
	ArrayList<Vertex> tabuList = new ArrayList<Vertex>();

	// A random number generator to stochastically get colors for vertices of
	// graph
	Random rand = new Random();

	public MinConflicts(ArrayList<Vertex> vertices) {
		this.vertices = vertices;

		System.out.println();
		System.out.println("Running the Min Conflicts algorithm (with Tabu Search on) to " + kColors + " color graph with " + vertices.size() + " vertices");
		System.out.println();

		// call the minConflicts algorithm
		algorithm(0);

		if (!unsolvable) {
			System.out
					.println("Did not find a " + kColors
							+ " coloring of graph in " + numIterations
							+ " iterations.");
		} else {
			System.out.println("Found a " + kColors + " coloring of graph in "
					+ numIterations + " iterations.");
		}

		System.out.println("MinConflicts has finished running");
		
		// for Sample Run
		System.out.println("Final Graph");
		printGandC();
		
		
		// print colors, for checking
//		 System.out.println("Colors :");
//		 for (int i = 0; i < vertices.size(); i++) {
//		 System.out.println("Vertex " + vertices.get(i).getId() + " : " +
//		 vertices.get(i).getColor());
//		 }
	}

	@Override
	protected int algorithm(int curV) {

		// generate a complete assignment for graph coloring problem (initially
		// random)
		for (int iterator = 0; iterator < vertices.size(); iterator++) {
			int randomVertexColor = rand.nextInt(kColors);
			vertices.get(iterator).setColor(randomVertexColor);
		}

		// for sample run
		System.out.println("Initial Graph");
		printGandC();
		
		
		// direct algorithm with tabu search to help avoid plateaus
		tabuListMaxSize = vertices.size() / 10;

		// while we have not yet reached the maximum number of steps to solve
		// the problem, repeat
		while (numIterations < maxIterations) {

			// set this to 0, before reassigning based on changes to graph
			numConflictsInEntireGraph = 0;

			ArrayList<Vertex> conflictVerts = new ArrayList<Vertex>();

			Vertex randomVertWithConflict = null;

			// go through entire graph looking for conflicts
			for (Vertex v : vertices) {
				if (!tabuList.contains(v)) {
					// System.out.println("Vertex v is " + v.getId());
					// check if current assignment results in any conflicts, if
					// no conflicts, current is a
					// solution to the k-coloring of graph G
					numConflictsInEntireGraph += v.getNumConflicts();

					// add every vertex with a conflict to the list of
					// conflictVerts
					if (v.getNumConflicts() > 0) {
						conflictVerts.add(v);
					} else {
						// there were no conflicts with this vertex, so candidate for tabu list
						if (!tabuList.contains(v)) {
							tabuList.add(v);
						} 
					} // end else -- tabu list additions

					// System.out.println("num conflicts for " + v.getId() +
					// " = " + v.getNumConflicts());
				} // end if -- have picked a vertex not in the tabuList
				
				if (tabuList.size() >= tabuListMaxSize) {
					tabuList.remove(0);
				}				
				
			} // end for -- have gone through each vertex checking for conflicts

			// test the current color assignment to see if a solution has been
			// found
			if (numConflictsInEntireGraph == 0) {
				// a solution has been found
				unsolvable = true;
				return (1);
			}

			// chose a randomly chosen variable from the set of conflicted
			// variables
			Collections.shuffle(conflictVerts);
			randomVertWithConflict = conflictVerts.get(0);

			// select the color for randomVert that minimizes conflicts - break
			// ties randomly
			assignColor(randomVertWithConflict,
					randomVertWithConflict.getColor());

			// for sample run
			System.out.println("Iteration " + numIterations + ": " + numConflictsInEntireGraph/2 + " conflicts");
			
			numIterations++;
		} // end while -- we have either returned a solution or reached
			// maxIterations

		return 0;
	}

	protected void assignColor(Vertex selectedVertex, int currentColor) {
		// for all the possible colors, evaluate the number of conflicts in
		// graph with each choice
		int minNumConflicts = Integer.MAX_VALUE;
		int minConflictValue = -1;

		for (int colorIterator = 0; colorIterator < kColors; colorIterator++) {

			// check each color to determine which results in the minimum number
			// of conflicts
			if (colorIterator != currentColor) {

				selectedVertex.setColor(colorIterator);
				int numConflictsInEntireGraph = 0;

				for (Vertex v : vertices) {
					// System.out.println("Vertex v is " + v.getId());
					// check if current color choice results in any conflicts
					numConflictsInEntireGraph += v.getNumConflicts();

					if (numConflictsInEntireGraph < minNumConflicts) {
						// we have a new value for minConflicts
						minNumConflicts = numConflictsInEntireGraph;
						minConflictValue = colorIterator;
					} else if (numConflictsInEntireGraph == minNumConflicts) {
						// if equal, break ties randomly
						Random rand = new Random();
						int tieBreak = rand.nextInt(2);

						if (tieBreak == 0) {
							// change color if tiebreak = 0, else leave as is
							minConflictValue = colorIterator;
						} // end if -- have broken tie

					} // end if -- have checked tie breaking condition

				} // end for -- have checked all vertices for the colorIterator
					// color
			} // end if -- have checked that the colorIterator color is not the
				// same as the vertex's current color
		} // end for -- check all of the possible colors to get the min conflict
			// value

		// set value to the variable that minimizes the number of conflicts
		selectedVertex.setColor(minConflictValue);

	}

	@Override
	protected void assignColorToVertex(int v, int c) {
	}

	@Override
	protected int selectVertex() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	// for sample runs
	public void printGandC(){
		//prints graph and coloring
		//print coloring first
		System.out.println("Printing coloring");
		
		for(Vertex v : vertices){		
			System.out.println("Vertex: " + v.getId() + ", Color:" + v.getColor());
		}
		
		int count = 0;
		System.out.println("Printing graph");
		for(int i = 0; i < vertices.size(); i++){
			for(int j = 0; j < vertices.get(i).neighbors.size(); j++){
				
				System.out.print(vertices.get(i).getId() + " -> " + vertices.get(i).neighbors.get(j).getId() + ", ");
				count++;
				if(count == 15){
					System.out.println();
					count = 0;
				}
			}
			
		}
		System.out.println();
	} // end, can comment out after sample runs are ran
	
}
