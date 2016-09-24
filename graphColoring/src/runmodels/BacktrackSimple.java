package runmodels;

//import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import runmodels.Node;
import runmodels.Tree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class BacktrackSimple extends AbstractAlgorithm {
    ArrayList<Vertex> current; //vertices as currently colored
    Tree states; //tree of states, where each state depends on color of vertices
    int numColors = 4;  //total number of colors allowed
    Vertex curVertex; //cur vertex to color
    int numNodes;  //total number of vertices to color
    boolean unsolvable = false;
	String heurType = "HighDegree"; //choices include None, HighDegree, LowColor, and LeastConflict

	// File writer that writes out the sample run information (if needed)
	BufferedWriter sampleWriter = null;

	// allows user to indicate whether or not this is a sample run (for output
	// purposes)
	boolean sampleRun = true;
    
	public BacktrackSimple(ArrayList<Vertex> vertices) throws IOException {

        this.current = vertices;

		if (heurType.equals("HighDegree")){
			sortByDegree();
		}
        this.curVertex = current.get(0);
        this.numNodes = vertices.size();

        
		try {
			FileWriter fileWriter = new FileWriter(
					"../graphColoring/sampleRuns/SampleRuns_Backtracking_"
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
        else if (curVertex.usableColors.size() == 0) {
            curVertex.createUsableColors(numColors);  // set number of colors available
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
        Vertex curNextI = current.get(curVertex.getId() -1);  //taking into account 0-based index. indexOf(curVertext may work now)
        curNextI.deleteColor(curNextI.getColor()); // color won't work

        //reset curVertex
        curVertex.setColor(-1);
        curVertex.createUsableColors(numColors);
        curVertex.setAllDeleted();

        curVertex = curNextI; //set curVertex back one

        actualAlgorithm(); //start over from here
    }


    // sort vertices by number of neighbors, so colors are given to nodes with higher degree first
    public void sortByDegree(){
		Collections.sort(current);
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
		
		setCurGraph(current);
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
		
		setCurGraph(current);
		printGandCOut(sampleWriter);

		sampleWriter.write("###############################");
		sampleWriter.newLine();
		sampleWriter.newLine();

	}

}
