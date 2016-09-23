package runmodels;

import java.util.ArrayList;

public class Chromosome {
	protected int numVertices;//number of vertices
	public ArrayList<Vertex> genes = new ArrayList<Vertex>();//the colored graph for this individual
	public int fitness;//the number of conflicts in the colored graph
	public Chromosome(ArrayList<Vertex> genes){
		this.genes = genes;
		numVertices = genes.size();
	}
	public int getNumVertices() {
		return numVertices;
	}
	public void setNumVertices(int numVertices) {
		this.numVertices = numVertices;
	}
	public ArrayList<Vertex> getGenes() {
		return genes;
	}
	public void setGenes(ArrayList<Vertex> genes) {
		this.genes = genes;
	}
	public int getFitness() {
		return fitness;
	}
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	public Vertex getGene(int i){
		return genes.get(i);
	}
	public void setGene(int i, int color){
		//System.out.println(i + " " + color + " " +  genes.size());
		genes.get(i).setColor(color);
	}
	public Chromosome clone(){//copies the Chromosome
		ArrayList<Vertex> geneCopy = new ArrayList<Vertex>();//Copy the graph
		for(int i = 0; i < numVertices; i++){//add all vertexes to the graph
			Vertex v = new Vertex(genes.get(i).getId(), genes.get(i).getXLoc(), genes.get(i).getYLoc(), genes.get(i).getColor());
			v.createUsableColors(genes.get(i).usableColors.size());
			geneCopy.add(v);
		}
		//System.out.println(geneCopy.size() + " " + numVertices);
		for(int i = 0; i < numVertices;i++){//add the neighbors to each vertex
			ArrayList<Vertex> nei = genes.get(i).getNeighbors();
			for(int j = 0; j < genes.get(i).neighbors.size(); j++){
				
				geneCopy.get(i).addNeighbors(geneCopy.get(nei.get(j).getId()));
			}
		}
		//System.out.println(geneCopy.size() + " " + genes.size() + " " + numVertices);
		Chromosome c = new Chromosome(geneCopy);//copy the remaining parameters
		c.fitness = this.fitness;
		c.numVertices = this.numVertices;
		return c;
	}
}
