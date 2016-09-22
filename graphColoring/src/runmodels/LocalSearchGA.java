package runmodels;

import java.util.ArrayList;

public class LocalSearchGA extends AbstractAlgorithm {
	protected double mutRate;
	protected double killRate;
	protected double popSize;
	//curGraph is current best graph
	protected ArrayList<Chromosome> population;
	protected int fitConstant = -1;
	public LocalSearchGA(ArrayList<Vertex> vertices) {
		curGraph = vertices;
		
		System.out.println("Running the Local Search using a genetic algorithm");	
	}
	@Override
	public ArrayList<Integer> runAlgo(){
		curIterations = 0;
		initialize();
		while(curIterations < maxIterations){
			Chromosome[] parents = selection();
			Chromosome child = recombination(parents);
			child = mutation(child);
			child.fitness = evalFit(child);
			replacement(child);
			Chromosome best = getBest();
			curGraph = best.genes;
			conflictHistory.add(best.fitness);
			curIterations++;
		}
		return conflictHistory;
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
	protected int evalFit(Chromosome c){
		ArrayList<Integer[]> conflicts = findConflicts(c.genes);
		return fitConstant*conflicts.size();
	}
	protected Chromosome getBest(){
		return population.get(0);
	}
	protected void replacement(Chromosome growinUp){
		
	}
	
	protected void initialize(){
		
	}
	protected Chromosome mutation(Chromosome frankie){
		return frankie;
	}
	protected Chromosome[] selection(){
		Chromosome[] parents = new Chromosome[2];
		return parents;
	}
	protected Chromosome recombination(Chromosome[] parents){
		return parents[0];
	}
	public double getMutRate() {
		return mutRate;
	}

	public void setMutRate(double mutRate) {
		this.mutRate = mutRate;
	}

	public double getKillRate() {
		return killRate;
	}

	public void setKillRate(double killRate) {
		this.killRate = killRate;
	}

	public double getPopSize() {
		return popSize;
	}

	public void setPopSize(double popSize) {
		this.popSize = popSize;
	}

	public ArrayList<Chromosome> getPopulation() {
		return population;
	}

	public void setPopulation(ArrayList<Chromosome> population) {
		this.population = population;
	}
	
	

	

}
