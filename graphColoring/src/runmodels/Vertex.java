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

	// flag to determine that all usable colors have been deleted
	private boolean allDeleted = false;
	
	// the color associated with each node, a value of -1 means that the node has no color
	private int color;
	
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

	void addArc(Vertex v){
        Arc a = new Arc(this, v);
        if(v.getId() > this.getId()) {
            outArcs.add(a);
            v.inArcs.add(a);
        }
    }

	void addColor(int color){
		usableColors.add(color);
		setAllDeleted();

	}

	public void createUsableColors(int upper){
		for (int i = 0; i < upper; i++){
			usableColors.add(i);
		}
	}

	public void deleteColor(int toDelete){
		if (usableColors.contains(toDelete)) {
			usableColors.remove(usableColors.indexOf(toDelete));
		}
        if (usableColors.size() == 0) {
            allDeleted = true;
        }
	}

	public boolean getAllDeleted(){
		return allDeleted;
	}

    public void setAllDeleted(){
         allDeleted = false;
    }

	@Override
	ArrayList<Vertex> getNeighbors() {
		return neighbors;
	}

	public Vertex checkConflicts(){
		Vertex conflicting = null;
		for (Vertex nei : neighbors) {
			if (this.color == nei.color){
				conflicting = nei;
			}
		}
		return conflicting;
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

