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
//    	System.out.println("To run the feed-forward neural net (with backprop) type 'ffnn'");
//    	System.out.println("To run the RBF network type 'rbf'");
    	System.out.println("Type 'x' to exit");

    	choice = in.nextLine();
    	
    	if (choice.equals("gc")) {
    		//calls function to run the graph generator
    		System.out.println("Generating Graphs");    		
            GraphGenerator gcGraphs = new GraphGenerator();
    	} else if (choice.equals("ffnn")) {
//			WILL MODIFY THE BELOW TO WORK FOR different GC algos    		
//    		//gets necessary info for performing Feed Forward Experiments	
//    		System.out.println("Performing Feed Forward Experiments");
//            // gets the os for the computer this program is run on
//            String os = System.getProperty("os.name").toLowerCase();
//            // gets the home location
//            String home = System.getProperty("user.home");
//            // starts building the file path
//            String filePathTrain = home;
//            String filePathTest = home;
//            
//            // uses file separator so is operating system agnostic
//            if (os.startsWith("windows")) { // Windows
//                filePathTrain += File.separator;
//                filePathTest += File.separator;
//            } else if (os.startsWith("mac")) { // Mac
//                filePathTrain += File.separator;
//                filePathTest += File.separator;
//            } else {
//                // everything else
//                filePathTrain += File.separator;
//                filePathTest += File.separator;
//            }

//            // calls the file chooser, returns the updated file path
//            System.out.println("Select Training Data Location");
//            filePathTrain = callFileChooser(filePathTrain);
//            System.out.println("Training Data: " + filePathTrain);
//            System.out.println("Select Test Data Location");
//            filePathTest = callFileChooser(filePathTest);
//            System.out.println("Test Data: " + filePathTest);
//            
//            FeedForwardExperiment test1 = new FeedForwardExperiment(filePathTrain, filePathTest);
    	} else {
    		System.exit(0);
    	}
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
