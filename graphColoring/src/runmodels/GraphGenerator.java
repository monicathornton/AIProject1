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
 * The graphs are written out as flat files, which will be read in by a parser and sent to 
 * the various  graph coloring strategies.
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
		System.out.println("Generating Graphs");   
		
		// The size of the graph (as measured by the number of vertices)
		int size = 10;

		// The x and y locations randomly assigned to each point
		ArrayList<Double> xVals = new ArrayList<Double>();
		ArrayList<Double> yVals = new ArrayList<Double>();

		// Keeps a list of the invalid vertices (vertices that are no longer in
		// play)
		ArrayList<Vertex> invalidVertices = new ArrayList<Vertex>();

		// File writer that writes out all of the graphs in the specified sizes
		BufferedWriter graphWriter = null;

		// Change these as needed for full graph generation
		String folderName = "graphSize10";
		String fileName = "graph10";
		String versionName = "_v1.txt";

		try {
			FileWriter fileWriter = new FileWriter(
					"../graphColoring/graphsBySize/" + folderName + "/"
							+ fileName + versionName);
			graphWriter = new BufferedWriter(fileWriter);

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
			// - corrects that (unlikely,  but worth a check)
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

		/*
		 * The following while loop connects the vertices as specified, that is:
		 * Connect X to nearest point Y (s.t. X is not already connected to Y
		 * and line crosses no other line). Repeat until no more connections are
		 * possible. NOTE: a vertex is "done" when it is connected to all other
		 * vertices, or when any new line would cross an existing line
		 */

		// while there are still valid vertices remaining
		while (invalidVertices.size() < size) {
			// pick a vertex at random
			int randomVertexId = rand.nextInt(size);
			
			// check that the candidate vertex is not in the list of invalid vertices
			while (invalidVertices.contains(vertices.get(randomVertexId))) {
				randomVertexId = rand.nextInt(size);
			} // end while -- have selected a vertex

			
			// pick a vertex at random from the list of valid vertices
			Vertex selectedVertex = vertices.get(randomVertexId);
			Vertex closestVertex = null;
					
			// calculate distance from selectedVertex to all remaining (valid) vertices
			double distance = Double.MIN_VALUE;

			// Keeps track of all distances from the selectedVertex
			Map<Vertex, Double> distMap = new HashMap<Vertex, Double>();
						
			// Moves through each of the valid vertices, checking the distance between it and selectedVertex
			for (Vertex v: vertices) {
				
				// check that v is not the selected vertex, bc comparing v to itself would not be helpful
				if (v != selectedVertex) {
					
					// calculate the distance between selectedVertex and each v					
					distance = calcDistance(selectedVertex, v);
					distMap.put(v, distance);
				
				} // end if -- have checked that v != selectedVertex
				
			} // end for -- checking for each vertex to get distance
			
			// pick the closest vertex 		
			closestVertex = getClosestVertex(selectedVertex, distMap);        
			
			// check that the closestVertex does not violate any constraint	
			// first, check that closest is not invalid
			// second, check that selectedVertex and closestVertex are not already neighbors
			// third, check that an edge between selected and closest would not cross an existing line
			while( (invalidVertices.contains(closestVertex)) || (selectedVertex.neighbors.contains(closestVertex)) || (linesCross(selectedVertex,closestVertex,invalidVertices)) ) {
				
				// if closest does violate the constraint, pick a new closest until distMap is empty
				if (distMap.size() != 0) {
					distMap.remove(closestVertex);
					
					// checks again if distMap is empty after removal
					if (distMap.size() != 0) {
						// if it is not empty, get the closest vertex
						closestVertex = getClosestVertex(selectedVertex, distMap);							
					} else {
						// if it is empty, we know selectedVertex is invalid
						if (!invalidVertices.contains(selectedVertex)) {
							invalidVertices.add(selectedVertex);						
						}						
					} // end else checking distMap size
				} else {
					//distMap = 0
					break;
				}
	
			} // end while -- closestVertex is not marked invalid
			
						
			// have checked constraints, and none are violated
			// edges are bidirectional, so add both
			if (!selectedVertex.neighbors.contains(closestVertex)) {
				selectedVertex.addNeighbors(closestVertex);
				closestVertex.addNeighbors(selectedVertex);				
			}

						
		} // end of while, no valid vertices remaining
		
		System.out.println("All edges added");
		
		// Write graph out to flat file

		try {
			// for testing, remove when finished
			validEdgeCount();
			
			graphWriter
					.write("% list of all vertices in the graph (comma separated)");
			graphWriter.newLine();
			
			String listOfVerts = "";

			for (int i = 0; i < vertices.size(); i++) {
				listOfVerts += vertices.get(i).getId() + ",";
			}

			// remove the trailing comma for printing
			listOfVerts = listOfVerts.substring(0, listOfVerts.length() - 1);

			// print the list of vertices
			graphWriter.write(listOfVerts);
			graphWriter.newLine();

			String listOfNeighbors = "";
			String listOfLocs = "";

			for (Vertex v : vertices) {
				listOfLocs += v.getId() + ": " + v.getXLoc() + ", "
						+ v.getYLoc() + "\n";

				for (int i = 0; i < v.neighbors.size(); i++) {
					listOfNeighbors += v.getId() + ","
							+ v.neighbors.get(i).getId() + "; ";
				}
			}

// 			for testing graphs in visualizer						
//			graphWriter.newLine();
//			graphWriter.write(listOfLocs);
			
			graphWriter.newLine();
			graphWriter
			.write("% list of all edges in the graph (semicolon separated)");
			graphWriter.newLine();
			graphWriter.write(listOfNeighbors);
//
			graphWriter.close();
		} catch (IOException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// Method for testing that the number of edges in the generated graph to not exceed the maximum amount 
	// of edges in a planar graph based on Euler's identity (for v>=3, e <= 3n-6).
	void validEdgeCount() {
		int edgeCount = 0;
		
		for (Vertex v : vertices) {
			edgeCount += v.neighbors.size();
		}
		
		// account for bi-directional edges
		edgeCount = edgeCount/2; 
		
		int maxEdges = 3 * (vertices.size() - 2);
				
		if (edgeCount <= maxEdges) {
			System.out.println("true");
			System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::Edge Count " + edgeCount);
			System.out.println(":::::::::::::::::::::::::::::::::Max Edges " + maxEdges);
		} else {
			System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::Edge Count " + edgeCount);
			System.out.println(":::::::::::::::::::::::::::::::::Max Edges " + maxEdges);
			System.out.println("false");
		}	
	}
	
	
	// Method to calculate the Euclidean distance between two points
	public double calcDistance(Vertex v1, Vertex v2) {
		double distance = Math.sqrt(Math.pow((v2.getXLoc() - v1.getXLoc()), 2)
				+ Math.pow((v2.getYLoc() - v1.getYLoc()), 2));

		return distance;
	}

	/*
	 * This method checks if the proposed line segment intersects any existing  
	 * line segments using the cross product, based on the method proposed in 
	 * "Introduction to Algorithms" by Cormen,Leiserson, Rivest and Stein in 
	 * the Computational Geometry section (chapter 33, pg. 1015 of the 3rd edition)
	 */
	public boolean linesCross(Vertex selectedVertex, Vertex closestVertex, ArrayList<Vertex> invalidVertices) {
		// get p3 and p4 from the current list of neighbors
		for (Vertex v : vertices) {
			Vertex p3 = v;
			for (int i = 0; i < v.getNeighbors().size(); i++) {
				Vertex p4 = v.getNeighbors().get(i);

				if (p3 != p4) {
					if ((p3 != selectedVertex) && (p4 != closestVertex)) {
						if ((p3 != closestVertex) && (p4 != selectedVertex)) {

							// determine the orientation associated with each of
							// the end
							// points, because if the lines
							// do intersect, the orientation of three of the
							// points will
							// be clockwise, and the intersection
							// of the other three will be clockwise
							double d1 = orientation(p3, p4, selectedVertex);
							double d2 = orientation(p3, p4, closestVertex);
							double d3 = orientation(selectedVertex,
									closestVertex, p3);
							double d4 = orientation(selectedVertex,
									closestVertex, p4);

							if (((d1 > 0) && (d2 < 0))
									|| ((d1 < 0) && (d2 > 0))
									&& ((d3 > 0) && (d4 < 0))) {
								return true;
							}
							// check if points are co-linear
							else if ((d1 == 0)
									&& (onSegment(p3, p4, selectedVertex))) {
								return true;
							} else if ((d2 == 0)
									&& (onSegment(p3, p4, closestVertex))) {
								return true;
							} else if ((d3 == 0)
									&& (onSegment(selectedVertex,
											closestVertex, p3))) {
								return true;
							} else if ((d4 == 0)
									&& (onSegment(selectedVertex,
											closestVertex, p4))) {
								return true;
							} 
						}
					} // end if -- checking that endpoints are not the same as
						// points we are checking
				}// end if -- p3 != p4
			} // end for which checks all neighbors for that vertex
		} // end for which checks all vertices
		
		// no lines could be added
		return false;		
	}

	// determines if 3 points are counterclockwise, clockwise or co-linear using
	// the cross product
	public double orientation(Vertex p0, Vertex p1, Vertex p2) {
		double part1 = 0.0;
		double part2 = 0.0;

		// compute first part of cross product
		part1 = (p1.getXLoc() - p0.getXLoc()) * (p2.getYLoc() - p0.getYLoc());

		// compute second part of cross product
		part2 = (p2.getXLoc() - p0.getXLoc()) * (p1.getYLoc() - p0.getYLoc());

		// return entire cross product to aid with determining orientation
		return (part1 - part2);
	}

	// determines if 3 points are counterclockwise, clockwise or co-linear using
	// the cross product
	public boolean onSegment(Vertex p0, Vertex p1, Vertex p2) {
		double minX = Double.min(p0.getXLoc(), p1.getXLoc());
		double maxX = Double.max(p0.getXLoc(), p1.getXLoc());
		double minY = Double.min(p0.getYLoc(), p1.getYLoc());
		double maxY = Double.max(p0.getYLoc(), p1.getYLoc());

		if (((minX <= p2.getXLoc()) && (p2.getXLoc() <= maxX))
				&& ((minY <= p2.getYLoc()) && (p2.getYLoc() <= maxY))) {
			return true;
		} else {
			return false;
		}
	}
	
	
	// determines if 3 points are counterclockwise, clockwise or co-linear using
	// the cross product
	public Vertex getClosestVertex(Vertex selectedVertex, Map<Vertex, Double> distMap) {
		double minDistance = Double.MAX_VALUE;
				
		minDistance = Collections.min(distMap.values());
		Vertex closestVertex = null;
		
        for (Entry<Vertex, Double> entry : distMap.entrySet()) {
            if (entry.getValue().equals(minDistance)) {
            	closestVertex = vertices.get((entry.getKey().getId()));
            }
        } // end for -- finds the closest vertex
    	
        // return the closestVertex
        return closestVertex;	
	}
	

}
