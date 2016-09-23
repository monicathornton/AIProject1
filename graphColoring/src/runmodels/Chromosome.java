package runmodels;

import java.util.ArrayList;

public class Chromosome {
	protected int numVertices;
	public ArrayList<Vertex> genes;
	public int fitness;
	public Chromosome(ArrayList<Vertex> genes){
		this.genes = genes;
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
		System.out.println(i + " " + color);
		genes.get(i).setColor(color);
	}
	public Chromosome clone(){
		ArrayList<Vertex> geneCopy = new ArrayList<Vertex>();
		for(int i = 0; i < numVertices; i++){
			Vertex v = new Vertex(genes.get(i).getId(), genes.get(i).getXLoc(), genes.get(i).getYLoc(), genes.get(i).getColor());
			v.createUsableColors(genes.get(i).usableColors.size());
			geneCopy.add(v);
		}
		for(int i = 0; i < numVertices;i++){
			ArrayList<Vertex> nei = genes.get(i).getNeighbors();
			for(int j = 0; j < genes.get(i).neighbors.size(); j++){
				
				geneCopy.get(i).addNeighbors(geneCopy.get(nei.get(j).getId()));
			}
		}
		Chromosome c = new Chromosome(geneCopy);
		c.fitness = this.fitness;
		c.numVertices = this.numVertices;
		return c;
	}
}
