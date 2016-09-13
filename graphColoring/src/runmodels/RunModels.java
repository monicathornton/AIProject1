/*
 * The class that contains the main method that runs either the graph
 * generation process, or one of the graph coloring variations (min conflicts, 
 * simple backtracking, backtracking with forward checking, backtracking with
 * constraint propagation and local search using a genetic algorithm).  
 */

package runmodels;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.util.*;


public class RunModels {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {    	
    	Scanner in = new Scanner(System.in);
    	String choice = "";
    	
    	//gives the user a series of choices
    	System.out.println("Please pick from one of the following options");
    	System.out.println("To run the graph generator, type 'gc'");
    	System.out.println("To run the Min Conflicts algorithm type '1'");
    	System.out.println("To run the Simple Backtracking algorithm type '2'");
    	System.out.println("To run the Backtracking algorithm with forward checking type '3'");
    	System.out.println("To run the Backgracking algorithm with constraint propagation type '4'");
    	System.out.println("To run Local search using a genetic algorithm type '5'");    	
    	System.out.println("Type 'x' to exit");

    	choice = in.nextLine();
    	
   		// the below is for testing, I will clean this up and make it prettier soon!
		
		// A list containing all of the vertices for the given problem
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
		double xLoc = 0.0;
		double yLoc = 0.0;
		int color = 0;
		
		Vertex p1 = new Vertex(1, xLoc, yLoc, color);
		Vertex p2 = new Vertex(2, xLoc, yLoc, color);	 
		Vertex p3 = new Vertex(3, xLoc, yLoc, color);
		Vertex p4 = new Vertex(4, xLoc, yLoc, color);
		Vertex p5 = new Vertex(5, xLoc, yLoc, color);
		
		vertices.add(p1);
		vertices.add(p2);
		vertices.add(p3);
		vertices.add(p4);
		vertices.add(p5);
		   		
		// Neighbors of 1: 5 and 2
		p1.addNeighbors(p5);
		p1.addNeighbors(p2);
		
		//Neighbors of 2: 1, 3, 5
		p2.addNeighbors(p1);
		p2.addNeighbors(p3);
		p2.addNeighbors(p5);		
		
		//Neighbors of 3: 2, 4		
		p3.addNeighbors(p2);
		p3.addNeighbors(p4);		
		
		//Neighbors of 4: 3, 5		
		p4.addNeighbors(p3);
		p4.addNeighbors(p5);		
		
		//Neighbors of 5: 1, 2, 4		
		p5.addNeighbors(p1);
		p5.addNeighbors(p2);		
		p5.addNeighbors(p4);		

    	// end testing -- will clean this up to make it more general
    	
    	if (choice.equals("gc")) {
    		//calls function to run the graph generator
    		System.out.println("Generating Graphs");    		
            GraphGenerator gcGraphs = new GraphGenerator();
    	} else if (choice.equals("1")) {	    		
    		MinConflicts mc = new MinConflicts(vertices);
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
    	
    	// Monica leaving this, will need again in future
//		//gets necessary info for performing Feed Forward Experiments	
//		System.out.println("Performing Feed Forward Experiments");
//        // gets the os for the computer this program is run on
//        String os = System.getProperty("os.name").toLowerCase();
//        // gets the home location
//        String home = System.getProperty("user.home");
//        // starts building the file path
//        String filePathTrain = home;
//        String filePathTest = home;
//        
//        // uses file separator so is operating system agnostic
//        if (os.startsWith("windows")) { // Windows
//            filePathTrain += File.separator;
//            filePathTest += File.separator;
//        } else if (os.startsWith("mac")) { // Mac
//            filePathTrain += File.separator;
//            filePathTest += File.separator;
//        } else {
//            // everything else
//            filePathTrain += File.separator;
//            filePathTest += File.separator;
//        }

//        // calls the file chooser, returns the updated file path
//        System.out.println("Select Training Data Location");
//        filePathTrain = callFileChooser(filePathTrain);
//        System.out.println("Training Data: " + filePathTrain);
//        System.out.println("Select Test Data Location");
//        filePathTest = callFileChooser(filePathTest);
//        System.out.println("Test Data: " + filePathTest);
//        
//        FeedForwardExperiment test1 = new FeedForwardExperiment(filePathTrain, filePathTest);
    	
    	
    }

    //calls a window with a pop up box that lets the user choose their exact
    //file location (with input from file string that gives user's home directory.
    public static String callFileChooser(String filePath) {
        // builds a JFrame
        JFrame frame = new JFrame("Folder Selection Pane");
        // string to score the path
        String thisPath = "";

        // JFrame look and feel
        frame.setPreferredSize(new Dimension(400, 200));
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button = new JButton("Select Folder");

        // sets up the file chooser
        JFileChooser fileChooser = new JFileChooser();
        // uses file path as a starting point for file browsing
        fileChooser.setCurrentDirectory(new File(filePath));
        // choose only from directories
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int fileChosen = fileChooser.showOpenDialog(null);

        // returns either the file path, or nothing (based on user choice)
        if (fileChosen == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            thisPath = selectedFile.getAbsolutePath();
            return thisPath;
        } else {
            return null;
        }
    }
}
