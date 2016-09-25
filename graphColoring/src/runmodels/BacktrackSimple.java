package runmodels;

//import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import runmodels.Node;
import runmodels.Tree;

import org.apache.commons.collections.comparators.ComparatorChain;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class BacktrackSimple extends AbstractAlgorithm {
    List<Vertex> current; //vertices as currently colored
    Tree states; //tree of states, where each state depends on color of vertices
    int numColors = 3;  //total number of colors allowed
    Vertex curVertex; //cur vertex to color
    int numNodes;  //total number of vertices to color
    boolean unsolvable = false;
	String heurType = "FEWEST_LEGAL"; //choices include None, HIGH_DEGREE, FEWEST_LEGAL, and LeastConflict

	// File writer that writes out the sample run information (if needed)
	BufferedWriter sampleWriter = null;

	// allows user to indicate whether or not this is a sample run (for output
	// purposes)
	boolean sampleRun = false;
    
	public BacktrackSimple(ArrayList<Vertex> vertices) throws IOException {

        this.current = vertices;

        for (Vertex v : vertices){
            v.createUsableColors(numColors);  // set number of colors available
        }

		if (heurType.equals("HIGH_DEGREE")){
			Collections.sort(current, new HIGH_DEGREE());
		}
        this.curVertex = current.get(0);
        this.numNodes = vertices.size();

        
		try {
			FileWriter fileWriter = new FileWriter(
					"graphColoring/sampleRuns/SampleRuns_Backtracking_"
							+ vertices.size() + ".txt");
			sampleWriter = new BufferedWriter(fileWriter);

		} catch (IOException e) {
			e.printStackTrace();
		}
        
		if (sampleRun == true) {
			printSampleRun1();
		}

       
        try {
            actualAlgorithm();
        }
        catch (StackOverflowError e){
            System.out.println("StackOverflow, abandoning attempt!");
        }
        if (unsolvable){
            System.out.println("This graph is unsolvable!");
        }
        System.out.println("Done!");
        
        sampleWriter.close();
    }

	@Override
	protected int algorithm(int curV) {
		try {
			actualAlgorithm();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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

	private void actualAlgorithm() throws IOException {

		if (sampleRun == true) {
			try {
				printSampleRun2();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
            while (true) {
            	if (heurType.equals("FEWEST_LEGAL")){
                    ComparatorChain chain = new ComparatorChain();
                    chain.addComparator(new UNASSIGNED());
                    chain.addComparator(new FEWEST_LEGAL());
                    Collections.sort(current, chain);

                    curVertex = current.get(0);
                    if (curVertex.getColor() != -1){  //all nodes colored
                        break;
                    }
			    }
                chooseColorStupid();  //choose first available color
                if (unsolvable) {
                    break;
                } else if (curVertex.getId() != numNodes -1) {// check for all nodes colored
                    curVertex = current.get(current.indexOf(curVertex) + 1); //next vertex
                } else {
                    break;
                }
            }
            
            printSampleRun5();
    }

    private void chooseColorStupid() throws IOException{
        if (curVertex.getAllDeleted()) {
        	if (sampleRun == true) {
        		printSampleRun4();
        	}
            backtrackLevel();
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

    private void backtrackColor(Vertex conflict) throws IOException{
        curVertex.deleteColor(conflict.getColor());  //delete conflicting color from possible choices
    	
        if (sampleRun == true) {
            printSampleRun3();    		
    	}
        
        chooseColorStupid();   //call again
    }

    private void backtrackLevel() throws IOException{
    	
        if (curVertex.getId() == 1){
            unsolvable = true;
            return;
        }
        Vertex curNextI;
        if (heurType.equals("FEWEST_LEGAL")){
            if (curVertex.getId() == 0){
                unsolvable = true;
                return;
            }
            curNextI = curVertex.getLastNeighbor();
        }
        else {
            curNextI = current.get(curVertex.getId() -1);  //taking into account 0-based index. indexOf(curVertext may work now)
        }
        curNextI.deleteColor(curNextI.getColor()); // color won't work
        curNextI.setColor(-1);

        //reset curVertex
        curVertex.setColor(-1);
        curVertex.createUsableColors(numColors);
        curVertex.setAllDeleted();

        curVertex = curNextI; //set curVertex back one

        actualAlgorithm(); //start over from here
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
				.write("Simple Backtracking algorithm to "
						+ numColors
						+ " color graph with "
						+ numNodes
						+ " vertices");
		
		sampleWriter.newLine();
		sampleWriter
				.write("*******************************************************************************************");
		sampleWriter.newLine();
		sampleWriter
				.write("*******************************************************************************************");
		sampleWriter.newLine();

	}
    
	// for printing color backtracking info
	public void printSampleRun2() throws IOException {

		// for sample run
		sampleWriter.newLine();
		sampleWriter.write("###############################");
		sampleWriter.newLine();
		sampleWriter.write("Initial Graph");
		sampleWriter.newLine();
		sampleWriter.newLine();
		
		setCurGraph((ArrayList) current);
		printGandCOut(sampleWriter);

		sampleWriter.write("###############################");
		sampleWriter.newLine();
		sampleWriter.newLine();
		
		sampleWriter
		.write("----------------------------------------------------------------------------");
		sampleWriter.newLine();
	}
	
	// for printing level backtracking info
	public void printSampleRun3() throws IOException {

		// for sample run
		sampleWriter.write("Backtracking a color on Vertex " + curVertex.getId() + " after conflict with Vertex " + curVertex.checkConflicts().getId());
		sampleWriter.newLine();
		sampleWriter.write("-------Remaining colors for Vertex " + curVertex.getId() + ": " + curVertex.usableColors);
		sampleWriter.newLine();
	}
	

	// for printing initial graph
	public void printSampleRun4() throws IOException {

		// for sample run
		sampleWriter.write("Backtracking a level on Vertex " + curVertex.getId() + " after conflict with Vertex " + curVertex.checkConflicts().getId());
		sampleWriter.newLine();
		
	}
	
	// for printing color backtracking info
	public void printSampleRun5() throws IOException {
		
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
		
		if (unsolvable) {
			sampleWriter.write("Did not find a " + numColors
							+ " coloring of graph with backtracking");
			sampleWriter.newLine();
			
		} else {
			sampleWriter.write("Found a " + numColors + " coloring of graph with backtracking.");
			sampleWriter.newLine();
		}
		
		sampleWriter
		.write("*****************************************************************************");
		sampleWriter.newLine();		
		sampleWriter
		.write("*****************************************************************************");
		sampleWriter.newLine();
		
		
		// for sample run
		sampleWriter.newLine();
		sampleWriter.write("###############################");
		sampleWriter.newLine();
		sampleWriter.write("Final Graph");
		sampleWriter.newLine();
		sampleWriter.newLine();
		
		setCurGraph((ArrayList) current);
		printGandCOut(sampleWriter);

		sampleWriter.write("###############################");
		sampleWriter.newLine();
		sampleWriter.newLine();

	}

}
