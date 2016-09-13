package runmodels;

/*
 * A class to create the vertices for a graph coloring problem. 
 */

import java.util.ArrayList;

public class Vertex extends AbstractVertex {
	// unique identifier associated with each node
	private int id;
	
	// x and y coordinates associated with each node
	private double xLoc;
	private double yLoc;
	
	// the color associated with each node, a value of -1 means that the node has no color
	private int color;
	
	// list of all neighbors for each node
	ArrayList<Vertex> neighbors = new ArrayList<Vertex>();

	/**
	 * 
	 * @param name
	 *            unique identifier associated with the node
	 * @param xLoc
	 *            x-coordinate of node
	 * @param yLoc
	 *            y-coordinate of node
	 */
	public Vertex(int id, double xLoc, double yLoc, int color) {
		this.id = id;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.color = color;
	}

	@Override
	double getXLoc() {
		return xLoc;
	}

	@Override
	double getYLoc() {
		return yLoc;
	}

	int setColor(int newColor) {
		return color = newColor;
	}
	
	@Override
	int getColor() {
		return color;
	}

	@Override
	int getId() {
		return id;
	}

	 void addNeighbors(Vertex v) {
		neighbors.add(v);
	}

	@Override
	ArrayList<Vertex> getNeighbors() {
		return neighbors;
	}

}

