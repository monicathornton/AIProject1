/*
 * The class that contains the main method that runs either the graph
 * generation process, or one of the graph coloring variations (min conflicts, 
 * simple backtracking, backtracking with forward checking, backtracking with
 * constraint propagation and local search using a genetic algorithm).  
 */

package runmodels;

import java.io.*;
import java.util.*;


public class RunModels {

	static BufferedReader br = null;
	static FileReader fileIn = null;
	
	// Array List to keep track of vertices read in from file
	static ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	
	// default values for all vertices (x and y loc don't matter, and
	// vertices are all uncolored)
	static double xLoc = 0.0;
	static double yLoc = 0.0;
	static int color = -1;
	
	/**
	 * @param args
	 *            the command line arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// keeps track of user choice (from the menu)
		Scanner in = new Scanner(System.in);
		String choice = "";
		
		// gives the user a series of choices
		System.out.println("Please pick from one of the following options");
		System.out.println("To run the graph generator, type 'gc'");
		System.out.println("To run the Min Conflicts algorithm type '1'");
		System.out.println("To run the Simple Backtracking algorithm type '2'");
		System.out.println("To run the Backtracking algorithm with forward checking type '3'");
		System.out.println("To run the Backgracking algorithm with constraint propagation type '4'");
		System.out.println("To run Local search using a genetic algorithm type '5'");
		System.out.println("Type 'x' to exit");

		// gets the user choice, to make a decision which algorithm to run
		choice = in.nextLine();

		// change these to get the required graph from the input file
		String folderName = "graphSize40";
		String fileName = "graph40";
		String versionName = "_v1.txt";
//		String versionName = "_v2.txt";
//		String versionName = "_v3.txt";
//		String versionName = "_v4.txt";
//		String versionName = "_v5.txt";
//		
		
		if (!choice.equals("gc")) {
			// reads the input file to populate the ArrayList of vertices, for passing to the algorithm
			getGraphFromFile(folderName,fileName,versionName);
		}
		
		
		if (choice.equals("gc")) {
			// calls function to run the graph generator
			// commented out for now, bc don't want to change a graph accidentally while we are running experiments
			//GraphGenerator gcGraphs = new GraphGenerator();
			System.out.println("If you need to run the graph generator,please uncomment it.");
		} else if (choice.equals("1")) {

			for (int i = 0; i < 5; i++) {
				MinConflicts mc = new MinConflicts(vertices);				
			}

		} else if (choice.equals("2")) {

			
			BacktrackSimple bs = new BacktrackSimple(vertices);
		} else if (choice.equals("3")) {
			
			BacktrackForCheck fc = new BacktrackForCheck(vertices);
		} else if (choice.equals("4")) {

			
			BacktrackConProp mac = new BacktrackConProp(vertices);
		} else if (choice.equals("5")) {

			
			LocalSearchGA ls = new LocalSearchGA(vertices);
		} else {
			System.exit(0);
		}
		
		in.close();
	}
	
	
	 static void getGraphFromFile(String folderName, String fileName, String versionName) throws IOException {

		try {
			fileIn = new FileReader("graphColoring/graphsBySize/"
					+ folderName + "/" + fileName + versionName);
			br = new BufferedReader(fileIn);

			// reads the first line in the file
			String currentLine = br.readLine();
			// all files that start with % are a comment
			while (currentLine.startsWith("%")) {
				// read in the first line, which gives the list of vertices
				// (comma separated)
				currentLine = br.readLine();
			}

			// turns the comma separated string into an array corresponding to
			// the vertices
			String[] vertList = currentLine.split(",");

			Vertex vertexToAdd = null;

			for (int i = 0; i < vertList.length; i++) {
				vertexToAdd = new Vertex(i, xLoc, yLoc, color);
				vertices.add(vertexToAdd);
			}

			// reads in the next line
			currentLine = br.readLine();

			// checks if line begins with % (denotes comment) or if it is a
			// blank line
			while ((currentLine.isEmpty()) || currentLine.startsWith("%")) {
				// read in the first line, which gives the list of edges
				// (semicolon separated)
				currentLine = br.readLine();
			}

			// turns the comma separated string into an array corresponding to
			// the edges
			String[] edgesList = currentLine.split(";");

			for (int i = 0; i < edgesList.length - 1; i++) {
				edgesList[i] = edgesList[i].trim();

				int startVertexId = Integer.parseInt(edgesList[i].substring(0,
						edgesList[i].indexOf(",")));
				int endVertexId = Integer.parseInt(edgesList[i].substring(
						edgesList[i].indexOf(",") + 1, edgesList[i].length()));

				for (Vertex v : vertices) {
					if (v.getId() == startVertexId) {
						if (!v.neighbors.contains(endVertexId)) {
							v.addNeighbors(vertices.get(endVertexId));
						}
					}
				}

			} // end for

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
