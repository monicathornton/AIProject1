package runmodels;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
	// to help avoid plateaus
	int tabuListMaxSize = 2;
	int tabuStaleTime = 1000;
	
	
	// A random number generator to stochastically get colors for vertices of
	// graph
	Random rand = new Random();

	// File writer that writes out the sample run information (if needed)
	BufferedWriter sampleWriter = null;

	// allows user to indicate whether or not this is a sample run (for output
	// purposes)
	boolean sampleRun = false;

	public MinConflicts(ArrayList<Vertex> vertices) throws IOException {
		this.vertices = vertices;

		try {
			FileWriter fileWriter = new FileWriter(
					"../graphColoring/sampleRuns/SampleRuns_MinConflicts_"
							+ vertices.size() + ".txt");
			sampleWriter = new BufferedWriter(fileWriter);

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (sampleRun == true) {
			printSampleRun1();

		}

		// call the minConflicts algorithm
		algorithm(0);

		if (sampleRun == true) {
			printSampleRun4();
		} else {

			if (!unsolvable) {
				System.out.println("Did not find a " + kColors
						+ " coloring of graph in " + numIterations
						+ " iterations.");
			} else {
				System.out.println("Found a " + kColors
						+ " coloring of graph in " + numIterations
						+ " iterations.");
			}
		}

		System.out.println("MinConflicts has finished running");

		sampleWriter.close();

	}

	@Override
	protected int algorithm(int curV) {

		// generate a complete assignment for graph coloring problem (initially
		// random)
		for (int iterator = 0; iterator < vertices.size(); iterator++) {
			int randomVertexColor = rand.nextInt(kColors);
			vertices.get(iterator).setColor(randomVertexColor);
		}

		if (sampleRun == true) {
			try {
				printSampleRun2();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		// while we have not yet reached the maximum number of steps to solve
		// the problem, repeat
		while (numIterations < maxIterations) {

			// set this to 0, before reassigning based on changes to graph
			numConflictsInEntireGraph = 0;
			ArrayList<Vertex> conflictVerts = new ArrayList<Vertex>();

			Vertex randomVertWithConflict = null;

			// go through entire graph looking for conflicts
			for (Vertex v : vertices) {
					// System.out.println("Vertex v is " + v.getId());
					// check if current assignment results in any conflicts, if
					// no conflicts, current is a
					// solution to the k-coloring of graph G
					numConflictsInEntireGraph += v.getNumConflicts();

					// add every vertex with a conflict to the list of
					// conflictVerts
					if (v.getNumConflicts() > 0) {
						conflictVerts.add(v);
					} 

					// System.out.println("num conflicts for " + v.getId() +
					// " = " + v.getNumConflicts());


			} // end for -- have gone through each vertex checking for conflicts

			// for sample run
			if (sampleRun == true) {

				try {
					printSampleRun3();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

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

		// check if tabuList value is stale
		if (numIterations < selectedVertex.tabuClock + tabuStaleTime) {
			selectedVertex.tabuList.clear();
		}
		
		
		// if color is in tabu list for this vertex, reassign to another color
		if (selectedVertex.tabuList.contains(minConflictValue)) {
			assignColor(selectedVertex, minConflictValue);
		}
		
		// set value to the variable that minimizes the number of conflicts
		selectedVertex.setColor(minConflictValue);
		// adds this value to the tabu list, if not already there
		if (!selectedVertex.tabuList.contains(minConflictValue)) {
			// make sure tabu list is appropriate size
			if (selectedVertex.tabuList.size() < tabuListMaxSize) {
				selectedVertex.setTabu(minConflictValue);	
				selectedVertex.setTabuClock(numIterations);	
			}
		}

	}

	@Override
	protected void assignColorToVertex(int v, int c) {
	}

	@Override
	protected int selectVertex() {
		// TODO Auto-generated method stub
		return 0;
	}

	// for printing sample runs
	public void printSampleRun1() throws IOException {

		sampleWriter
				.write("*******************************************************************************************");
		sampleWriter.newLine();
		sampleWriter
				.write("*******************************************************************************************");
		sampleWriter.newLine();
		sampleWriter
				.write("Running the Min Conflicts algorithm (with Tabu Search on) to "
						+ kColors
						+ " color graph with "
						+ vertices.size()
						+ " vertices");
		sampleWriter.newLine();
		sampleWriter
				.write("*******************************************************************************************");
		sampleWriter.newLine();
		sampleWriter
				.write("*******************************************************************************************");
		sampleWriter.newLine();

	}

	// for printing initial graph
	public void printSampleRun2() throws IOException {

		// for sample run

		sampleWriter.newLine();
		sampleWriter.write("###############################");
		sampleWriter.newLine();
		sampleWriter.write("Randomly Colored Initial Graph");
		sampleWriter.newLine();
		sampleWriter.newLine();

		setCurGraph(vertices);
		printGandCOut(sampleWriter);

		sampleWriter.write("###############################");
		sampleWriter.newLine();
		sampleWriter.newLine();

		sampleWriter
				.write("----------------------------------------------------------------------------");
		sampleWriter.newLine();
		sampleWriter.write("Algorithm terminates once solution is found or "
				+ maxIterations + " iterations is reached");
		sampleWriter.newLine();

		sampleWriter
				.write("Conflicts written as A:Color --> B:Color, and A -> B is counted, as is B-> A ");
		sampleWriter.newLine();
	}

	// for printing iterations
	public void printSampleRun3() throws IOException {
		sampleWriter.newLine();
		sampleWriter.write("Iteration " + numIterations + ": "
				+ numConflictsInEntireGraph + " conflicts");
		sampleWriter.newLine();

		for (Vertex v : vertices) {

			int conflicts = v.getNumConflicts();

			if (conflicts > 0) {
				for (int i = 0; i < v.getAllConflicts().size(); i++) {
					sampleWriter.write(v.getId() + ":" + v.getColor() + "--> "
							+ v.getAllConflicts().get(i).getId() + ":"
							+ v.getAllConflicts().get(i).getColor());
					sampleWriter.newLine();
				}
			}

		} // end for

	} 
	// for printing solution
	public void printSampleRun4() throws IOException {
		sampleWriter
		.write("----------------------------------------------------------------------------");
		sampleWriter.newLine();
		sampleWriter.newLine();
		
		sampleWriter
		.write("*****************************************************************************");
		sampleWriter.newLine();		
		sampleWriter
		.write("*****************************************************************************");
		sampleWriter.newLine();
		
		if (!unsolvable) {
			sampleWriter.write("Did not find a " + kColors
							+ " coloring of graph in " + numIterations
							+ " iterations.");
			sampleWriter.newLine();
			
		} else {
			sampleWriter.write("Found a " + kColors + " coloring of graph in "
					+ numIterations + " iterations.");
			
			sampleWriter.newLine();
		}
		
		sampleWriter
		.write("*****************************************************************************");		
		sampleWriter.newLine();
		sampleWriter
		.write("*****************************************************************************");
		sampleWriter.newLine();
		
		
		sampleWriter.newLine();
		sampleWriter.write("###############################");
		sampleWriter.newLine();
		sampleWriter.write("Final Graph Coloring");
		sampleWriter.newLine();		
		sampleWriter.newLine();				 
		setCurGraph(vertices);
		printGandCOut(sampleWriter);
		sampleWriter.newLine();		
		sampleWriter.write("###############################");		

	} 

}
