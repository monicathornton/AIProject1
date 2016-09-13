package runmodels;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/*
 * A class to create planar graphs ranging in size from 10-100 vertices (in steps of 10).  
 * The graphs are written out as flat files, which will be read in by a variety of 
 * graph coloring strategies.
 */

public class GraphGenerator {
	// The ranges that determine possible x and y values for the vertex
	// locations
	double minRange = 0.0;
	double maxRange = 1.0;

	// A list containing all of the vertices for the given problem
	ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	
	/**
	 * Generates the graphs to be used by the various graph coloring approaches
	 * 
	 * @throws IOException
	 */

	public GraphGenerator() {
		System.out.println("TODO: THIS IS NOT YET FINISHED/TESTED");
		
		// The size of the graph (as measured by the number of vertices)
		int size = 10;

		// The x and y locations randomly assigned to each point
		ArrayList<Double> xVals = new ArrayList<Double>();
		ArrayList<Double> yVals = new ArrayList<Double>();
		
		// Keeps a list of the invalid vertices (vertices that are no longer in
		// play)
		ArrayList<Vertex> invalidVertices = new ArrayList<Vertex>();
		
		// A variable used to store the distance between two points
		double distance = Double.MIN_VALUE;

		// Variables used to store the x and y locations of each vertex
		double xLoc = Double.MIN_VALUE;
		double yLoc = Double.MAX_VALUE;
		
		// file writer that writes out all of the graphs in the specified sizes
		BufferedWriter graphWriter10 = null;

		// BufferedWriter graphWriter20 = null;
		// BufferedWriter graphWriter30 = null;
		// BufferedWriter graphWriter40 = null;
		// BufferedWriter graphWriter50 = null;
		// BufferedWriter graphWriter60 = null;
		// BufferedWriter graphWriter70 = null;
		// BufferedWriter graphWriter80 = null;
		// BufferedWriter graphWriter90 = null;
		// BufferedWriter graphWriter100 = null;

		// TODO: make a new file if file already exists
		try {
			FileWriter fileWriter10 = new FileWriter(
					"../graphColoring/graphsBySize/graphSize10/graph10.txt");
			graphWriter10 = new BufferedWriter(fileWriter10);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Scatter the n points on the unit square
		for (int i = 0; i < size; i++) {
			// Gets the random x and y location for each of the points in the
			// unit square
			double randomXVal = Math.random();
			double randomYVal = Math.random();

			// Check that no two points have exact same x and y locations, if so
			// - correct
			while (xVals.contains(randomXVal) && yVals.contains(randomYVal)) {
				randomXVal = Math.random();
				randomYVal = Math.random();
			}

			// In the (likely) case where there are no duplicate x and y
			// locations, add to array of xVals and yVals
			xVals.add(randomXVal);
			yVals.add(randomYVal);

			// Build the vertices, given the node's unique identifier, and x and
			// y locations
			Vertex x = new Vertex(i, xVals.get(i).doubleValue(), yVals.get(i)
					.doubleValue(), 0);

			// Add each vertex to the list of vertices
			vertices.add(x);
		} // end for

		System.out.println(size + " vertices constructed");
	
		// Select a point X at random from list of vertices
		Random rand = new Random();

		int min = 0;
		int max = size - 1;
				
		/*
		 * The following while loop connects the vertices as specified, that is: 
		 * Connect X to nearest point Y (s.t. X is not already connected to
		 * Y and line crosses no other line). Repeat until no more connections are possible.
		 * NOTE: a vertex is "done" when it is connected to all other vertices,
		 * or when any new line would cross an existing line
		 */
		
		// while there are still valid vertices remaining 
		while (invalidVertices.size() != size) {
			// keeps track of the distance at the time, the minimum distance and the closest vertex
			distance = Double.MIN_VALUE;
			double minDistance = Double.MAX_VALUE;
			Vertex closestVertex = null;

			// Keeps track of all distances from the selectedVertex
			Map<Integer,Double> distMap = new HashMap<Integer,Double>(); 
			
			// generates a random number corresponding to the vertex id
			int randomVertexId = rand.nextInt(size);
			
			// makes sure the randomVertexId corresponds to a valid vertex choice
			while (invalidVertices.contains(vertices.get(randomVertexId))) {
				randomVertexId = rand.nextInt(size);
			}
			
			// pick a vertex at random from the list of valid vertices
			Vertex selectedVertex = vertices.get(randomVertexId);
				
			// Moves through each of the not currently selected vertices, calculating distance
			for (Vertex v : vertices) {		
				
					// Prevents the selectedVertex from being picked from the list
					if (v.getId() != selectedVertex.getId()) {
						
						// Checks that the selected vertex is a valid choice
						if (!invalidVertices.contains(selectedVertex)) {
						
						// calculate distance from the selectedVertex to v
						distance = calcDistance(selectedVertex, v);
						
						//stores all distances from the selectedVertex to this 
						distMap.put(v.getId(), distance);
						
						// if this distance is less than minDistance, set new
						// minDistance
						if (distance < minDistance) {
							minDistance = distance;
							closestVertex = v;

						} // end if -- checks if minDistance should be updated
					} // end if -- checks that selected vertex is a valid choice
				} // end if -- makes sure selectedVertex is not the one picked 
			} // end for - have checked distance from each vertex to the selected vertex
						
			// check that the proposed edge meets the necessary constraints (cannot connect to a vertex that
			// is already a neighbor, and that the proposed edge does not cross an existing edge)
			if (!selectedVertex.neighbors.contains(closestVertex) && !linesCross(selectedVertex, closestVertex)) {
				// connections are bidirectional, so add each to neighbor list
				selectedVertex.addNeighbors(closestVertex);
				closestVertex.addNeighbors(selectedVertex);
			} else {
				// pick new vertex, until it is not a neighbor or lines do not cross
				// if that is not possible, then add to the list of invalid vertices
				boolean noConnectionsLeft = false;
				boolean connectionMade = false;
				
				// while a connection has not been made, or there are no connections left
				while ((!connectionMade) && (!noConnectionsLeft)) {
					connectionMade = false;
				
					//closestVertex is not a valid choice for an endpoint
					distMap.remove(closestVertex.getId());
					
					// while there are still entries in the distance map
					if (distMap.size() > 0) {				
						System.out.println("in if for vertex " + selectedVertex.getId());
						
						// gets the minimum distance value from the distance map
						double newMinDist = Collections.min(distMap.values());
						
						// gets the closest entry
				        for (Entry<Integer, Double> entry : distMap.entrySet()) {
				            if (entry.getValue().equals(newMinDist)) {
				                closestVertex = vertices.get((entry.getKey()));
				            }
				         
				        // is already a neighbor, and that the proposed edge does not cross an existing edge)
						if (!selectedVertex.neighbors.contains(closestVertex) && !linesCross(selectedVertex, closestVertex)) {
							System.out.println("");
							
							// connections are bidirectional, so add each to neighbor list
							selectedVertex.addNeighbors(closestVertex);
							closestVertex.addNeighbors(selectedVertex);
							connectionMade = true;
						} 

						System.out.println("at end of if for vertex " + selectedVertex.getId());
						System.out.println("connection made " + connectionMade);
				      } //end for, all connections have been made
					} else {
						System.out.println("in else for vertex " + selectedVertex.getId());
						// nothing left in the distance map, so the vertex can now be added to the list of invalid vertices
						noConnectionsLeft = true;
						invalidVertices.add(selectedVertex);
						System.out.println("at end of else for vertex " + selectedVertex.getId());
					} //end else which checks size of distMap (which stores all vertex distances)
				} //end while that checks that the vertex is still valid
			} //end if/else that edge placement meets constraints
		} // end while checking that vertices are remaining

		System.out.println("All edges added");

		// Write out to flat file
		

		// Close all the writers, clear the array lists, set distance back up

		// increase the size by 10

		// close the writers for the graphs of size 10
		try {
			graphWriter10.write("% list of all vertices in the graph (comma separated)");
			graphWriter10.newLine();
			
			String listOfVerts = "";
			
			for (int i = 0; i < vertices.size(); i++) {
				listOfVerts += vertices.get(i).getId() + ",";
			}
			
			//remove the trailing comma for printing
			listOfVerts = listOfVerts.substring(0, listOfVerts.length() - 1);
			
			// print the list of vertices
			graphWriter10.write(listOfVerts);
			graphWriter10.newLine();			
			
			String listOfNeighbors = "";
			String listOfLocs = "";
			
			for (Vertex v: vertices) {
				listOfLocs += v.getId() + ": " + v.getXLoc() + ", " + v.getYLoc() + "\n";
				
				for (int i = 0; i < v.neighbors.size(); i++) {
					listOfNeighbors += v.getId() + "," + v.neighbors.get(i).getId() + "; "; 					
				}
			}
			
			System.out.println(listOfLocs);
			System.out.println(listOfNeighbors);
			
			graphWriter10.close();
		} catch (IOException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	public double calcDistance(Vertex v1, Vertex v2) {
		double distance = Math.sqrt(Math.pow((v2.getXLoc() - v1.getXLoc()), 2) + Math.pow((v2.getYLoc() - v1.getYLoc()), 2));
		
		return distance;
	}
	
	
	 
	// This method checks if the proposed line segment intersects any existing line segments using the cross product, 
	// based on the method proposed in "Introduction to Algorithms" by Cormen, Leiserson, Rivest and Stein in 
	// the Computational Geometry section (chapter 33, pg. 1015 of the 3rd edition)
	 public boolean linesCross(Vertex selectedVertex, Vertex closestVertex) {
		 // get p3 and p4 from the current list of neighbors
		 for (Vertex v: vertices){
			 Vertex p3 = v;
			 for(int i = 0; i < v.getNeighbors().size(); i++) {
				 Vertex p4 = v.getNeighbors().get(i);
			 
				 // determine the orientation associated with each of the end points, because if the lines
				 // do intersect, the orientation of three of the points will be clockwise, and the intersection
				 // of the other three will be clockwise
				 double d1 = orientation(p3,p4,selectedVertex);
				 double d2 = orientation(p3,p4,closestVertex);
				 double d3 = orientation(selectedVertex,closestVertex,p3);
				 double d4 = orientation(selectedVertex,closestVertex,p4);
		 
				 if( ((d1 > 0) && (d2 < 0)) || ((d1 < 0) && (d2  > 0)) && ((d3 > 0) && (d4 < 0))){
					 return true;
				 } 
				 // check if points are co-linear
				 else if((d1 == 0) && (onSegment(p3,p4,selectedVertex))) {
					 return true;
				 } else if ((d2 == 0) && (onSegment(p3,p4,closestVertex))) {
					 return true;
				 } else if ((d3 == 0) && (onSegment(selectedVertex,closestVertex,p3))) {
					 return true;
				 } else if ((d4 == 0) && (onSegment(selectedVertex,closestVertex,p4))) {
					 return true;
				 }
				 
			 } //end for which checks all neighbors for that vertex
		} // end for which checks all vertices
		 
		 // no lines could be added
		 return false;
	 }
	 
	 // determines if 3 points are counterclockwise, clockwise or co-linear using the cross product
	 public double orientation(Vertex p0, Vertex p1, Vertex p2) {
		 double part1 = 0.0;
		 double part2 = 0.0;
		 
		 //compute first part of cross product
		 part1 = (p1.getXLoc() - p0.getXLoc()) * (p2.getYLoc() - p0.getYLoc());
		 
		 //compute second part of cross product
		 part2 = (p2.getXLoc() - p0.getXLoc()) * (p1.getYLoc() - p0.getYLoc());
		 
		 // return entire cross product to aid with determining orientation
		 return (part1 - part2);		 
	 }

	 
	 // determines if 3 points are counterclockwise, clockwise or co-linear using the cross product
	 public boolean onSegment(Vertex p0, Vertex p1, Vertex p2) {
		 double minX = Double.min(p0.getXLoc(), p1.getXLoc());
		 double maxX = Double.max(p0.getXLoc(), p1.getXLoc());
		 double minY = Double.min(p0.getYLoc(), p1.getYLoc());
		 double maxY = Double.max(p0.getYLoc(), p1.getYLoc());		 
		 
		 if ( ((minX <= p2.getXLoc()) && (p2.getXLoc() <= maxX)) && ((minY <= p2.getYLoc()) && (p2.getYLoc() <= maxY))) {
			 return true;
		 } else {
			 return false;
		 }	 
	 }
	 
	 
	 
	 
	 
	 
	 //	//
//	// // the derivative calculation is not necessary for this function
//	// public double calcderivfx(double x) {
//	// return 0.00;
//	// }
//
}
