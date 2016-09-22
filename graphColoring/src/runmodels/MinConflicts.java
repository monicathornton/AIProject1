package runmodels;

import java.util.ArrayList;

public class MinConflicts extends AbstractAlgorithm {
	
	public MinConflicts(ArrayList<Vertex> vertices) {
		System.out.println("Running the Min Conflicts algorithm");
//		
		// makes sure the vertices were brought in correctly
		System.out.println("Size: " + vertices.size());
		
		System.out.println("Vertices :");
		for (int i = 0; i < vertices.size(); i++) {
			//System.out.println(vertices.get(i).getId());
			System.out.print(vertices.get(i).getId() + ", ");
		}
		
		System.out.println();
		
		// and the edges
		System.out.println("Edges: ");
		for (Vertex v: vertices) {
			for (int i = 0; i < v.neighbors.size(); i++) {
				//System.out.println(v.getId() + "," + v.getNeighbors().get(i).getId());
				System.out.print(v.getId() + "," + v.getNeighbors().get(i).getId() + "; ");
			}
		}
//		
//		//set colors
//		vertices.get(0).setColor(1);
//		vertices.get(1).setColor(2);
//		vertices.get(2).setColor(3);
//		vertices.get(3).setColor(4);
//		vertices.get(4).setColor(5);
//		
//		System.out.println("Color after setting");
//		for (Vertex v: vertices) {
//			System.out.println(v.getId() + "'s color: " + v.getColor());
//		}		
		
		
		
	}

	@Override
	protected int algorithm(int curV) {
		// TODO Auto-generated method stub
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

	
}


