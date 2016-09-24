package runmodels;



/*
 * A class to create the vertices for a graph coloring problem. 
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Vertex extends AbstractVertex {
	// unique identifier associated with each node
	private int id;
	
	// x and y coordinates associated with each node
	private double xLoc;
	private double yLoc;

	// flag to determine that all usable colors have been deleted
	private boolean allDeleted = false;
	
	// the color associated with each node, a value of -1 means that the node has no color
	private int color;

	private HashMap<Integer, String> colorMap = new HashMap<>();
	// list of all neighbors for each node
	ArrayList<Vertex> neighbors = new ArrayList<Vertex>();
	ArrayList<Arc> inArcs = new ArrayList<>();
    ArrayList<Arc> outArcs = new ArrayList<>();
	ArrayList<Integer> usableColors = new ArrayList<Integer>();

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
		colorMap.put(0, "RED");
		colorMap.put(1, "GREEN");
		colorMap.put(2, "BLUE");
		colorMap.put(3, "YELLOW");
	}


	@Override
	// get node's x Location
	double getXLoc() {
		return xLoc;
	}

	@Override
	// get node's y location
	double getYLoc() {
		return yLoc;
	}
	
	// set the color of the node
	int setColor(int newColor) {
		return color = newColor;
	}
	
	@Override
	// gets the color of the node
	int getColor() {
		return color;
	}

	@Override
	// gets the node's unique id
	int getId() {
		return id;
	}


	//add arc to list of arcs
	void addArc(Vertex v){
        Arc a = new Arc(this, v);
        if(v.getId() > this.getId()) {
            outArcs.add(a);
            v.inArcs.add(a);
        }
    }


	// add the color to the list of usable colors
	void addColor(int color){
		usableColors.add(color);
		setAllDeleted();

	}

	// create the usable colors for this vertex
	public void createUsableColors(int upper){
		for (int i = 0; i < upper; i++){
			usableColors.add(i);
		}
	}

	// remove a color as usable
	public void deleteColor(int toDelete){
		if (usableColors.contains(toDelete)) {
			usableColors.remove(usableColors.indexOf(toDelete));
		}
        if (usableColors.size() == 0) {
            allDeleted = true;
        }
	}

	// get all deleted colors
	public boolean getAllDeleted(){
		return allDeleted;
	}

	// set all deleted colors
    public void setAllDeleted(){
         allDeleted = false;
    }

	@Override
	// get all neighbors for the specified node
	ArrayList<Vertex> getNeighbors() {
		return neighbors;
	}

	// given a vertex V, add it to the list of this node's neighbors
	void addNeighbors(Vertex v) {
		neighbors.add(v);
	}
	
	// check for a vertex that conflicts with this vertex
	public Vertex checkConflicts(){
		Vertex conflicting = null;
		for (Vertex nei : neighbors) {
			if (this.color == nei.color){
				conflicting = nei;
			}
		}
		return conflicting;
	}

	// get the number of conflicts for this node in the current graph
	public int getNumConflicts(){
		int numConflicts = 0;
		
		for (Vertex nei : neighbors) {
			if (this.color == nei.color){
				numConflicts++;
			}
		}
		return numConflicts;
	}

	public String printColors(){
		String result = "";
		result += "{";
		for (Integer color : usableColors){
			if (null != colorMap.get(color)){
				result += colorMap.get(color);
				if(color != colorMap.size() -1 ){
					result += ",";
				}
			}
		}
		result += "}";
		return result;
	}
	

    public ArrayList<Vertex> cloneList(ArrayList<Vertex> vList) {
        ArrayList<Vertex> clonedList = new ArrayList<Vertex>(vList.size());
        for (Vertex v : vList) {
            Vertex newV = new Vertex(v.getId(), v.getXLoc(), v.getYLoc(), v.getColor());
            for (Vertex nei: v.getNeighbors()){
                newV.addNeighbors(nei);
            }
            newV.usableColors = v.usableColors;  //may need to be changed...
            clonedList.add(newV);
        }
        return clonedList;
    }
}