package runmodels;

/*
 * A class to create the vertices for a graph coloring problem. 
 */

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Vertex extends AbstractVertex {
	// unique identifier associated with each node
	private int id;
	
	// x and y coordinates associated with each node
	private double xLoc;
	private double yLoc;
	
	// list of all neighbors for each node
	ArrayList<Integer> neighbors = new ArrayList<Integer>();

	/**
	 * 
	 * @param name
	 *            unique identifier associated with the node
	 * @param xLoc
	 *            x-coordinate of node
	 * @param yLoc
	 *            y-coordinate of node
	 */
	public Vertex(int id, double xLoc, double yLoc) {
		this.id = id;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		//TODO: Will need to give each node a color, and a list of neighbors (will save as unique id, so an array of ints, also something to indicate was visited?)
	}

	@Override
	double getXLoc() {
		return xLoc;
	}

	@Override
	double getYLoc() {
		return yLoc;
	}

	//TODO: handle the color
	@Override
	int getColor() {
		return Integer.MIN_VALUE;
	}

	@Override
	int getId() {
		return id;
	}

	 void addNeighbors(int id) {
		neighbors.add(id);
	}


	@Override
	ArrayList<Integer> getNeighbors() {
		return neighbors;
	}

}

