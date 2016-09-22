package runmodels;

import java.util.ArrayList;

public class Chromosome {
	protected int numVertices;
	public ArrayList<Vertex> genes;
	public int fitness;
	public Chromosome(){
		
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
	public void setGene(int i, Vertex v){
		genes.set(i, v);
	}
}
