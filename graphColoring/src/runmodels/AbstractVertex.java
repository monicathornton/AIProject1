package runmodels;

import java.util.ArrayList;

/*
 * An abstract class for the functions implemented in this work.
 */
public abstract class AbstractVertex {
	
	public AbstractVertex(){
		
	}
	
	abstract double getXLoc();
	abstract double getYLoc();
	abstract int getId();
	abstract int getColor();
	abstract ArrayList<Vertex> getNeighbors();
	abstract ArrayList<Vertex> getConflicts();	
}
