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
		int bestindex = 0;
		int bestfitness = population.get(0).fitness;
		for(int i = 1; i < population.size(); i++){
			if(population.get(i).fitness < bestfitness){
				bestfitness = population.get(i).fitness;
				bestindex = i;
			}
		}
		return population.get(bestindex);
	}
	protected void replacement(Chromosome growinUp){//TODO finish replacement
		
	}
	
	protected void initialize(){//TODO finish initialization
		
	}
	protected Chromosome mutation(Chromosome frankie){//TODO finish mutation
		return frankie;
	}
	protected Chromosome[] selection(){ //TODO finish selection
		Chromosome[] parents = new Chromosome[2];
		return parents;
	}
	protected Chromosome recombination(Chromosome[] parents){
		int parentSize = parents.length;
		double selectProb = (1 / parentSize);

		double[] parentProbs = new double[parentSize];

		// vector of upper bounds on parent probabilities (uniform)
		parentProbs[0] = selectProb;
		for (int i = 1; i < parentSize; i++) {
			parentProbs[i] = parentProbs[i - 1] + selectProb;
		}

		Chromosome offspring = new Chromosome();
		// probabilistically selects each gene
		for (int i = 0; i < parents[0].getNumVertices(); i++) {
			double p = Math.random();

			for (int j = 0; j < parentProbs.length; j++) {
				if (p < parentProbs[j]) {
					offspring.setGene(i, parents[j].getGene(i));
					break;
				}
			}

		}

		return offspring;
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
