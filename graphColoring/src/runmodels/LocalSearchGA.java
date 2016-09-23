package runmodels;

import java.util.ArrayList;
import java.util.Collections;

public class LocalSearchGA extends AbstractAlgorithm {
	protected double mutRate = 0.2;
	protected double killRate = 0.6;
	protected int popSize = 100;
	protected int numVertices;
	//curGraph is current best graph
	protected ArrayList<Chromosome> population = new ArrayList<Chromosome>();
	protected int fitConstant = -1;
	protected int poolSize = 2;
	protected int parentSize = 2;
	protected int colorlim = 3;
	
	public LocalSearchGA(ArrayList<Vertex> vertices) {
		curGraph = vertices;
		numVertices = vertices.size();
		for(int i = 0; i < numVertices; i++){
			vertices.get(i).createUsableColors(colorlim);
		}
		maxIterations = 1000;
		mutRate = 0.2;
		killRate = 0.6;
		popSize = 100;
		fitConstant = -1;
		poolSize = 2;
		parentSize = 2;
		colorlim = 3;
		conflictHistory = new ArrayList<Integer>();
		System.out.println("Running the Local Search using a genetic algorithm");	
		runAlgo();
	}
	@Override
	public ArrayList<Integer> runAlgo(){
		curIterations = 0;
		initialize();
		int bestfitness = Integer.MIN_VALUE;
		Double d = killRate*popSize;
		int numOffspring = d.intValue();
		System.out.println((killRate*popSize) + " " + numOffspring);
		for(int i = 0; i < population.size(); i++){
			population.get(i).fitness = evalFit(population.get(i));
		}
		
		
		while(curIterations < maxIterations && bestfitness < 0){
			System.out.println(numOffspring);
			ArrayList<Chromosome> kiddos = new ArrayList<Chromosome>();
			for (int j = 0; j < numOffspring; j++){
				
				Chromosome[] parents = selection();
				Chromosome child = recombination(parents);
				child = mutation(child);
				child.fitness = evalFit(child);
				kiddos.add(child);
			}
			//Collections.sort(kiddos,(player1, player2) -> player1.getFitness()- player2.getFitness());
			Collections.sort(population, (chromo1, chromo2) -> chromo1.getFitness()- chromo2.getFitness());
			
			for(int k = 0; k < kiddos.size(); k++){
				population.set(k, kiddos.get(k));
			}
			Chromosome best = getBest();
			curGraph = best.genes;
			conflictHistory.add(best.fitness);
			bestfitness = best.fitness;
			System.out.println(curIterations + " " + best.fitness);
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
		ArrayList<Vertex> conflicts = findConflicts(c.genes);
		
		return fitConstant*conflicts.size()/2;
	}
	protected Chromosome getBest(){
		int bestindex = 0;
		int bestfitness = population.get(0).fitness;
		for(int i = 1; i < population.size(); i++){
			if(population.get(i).fitness > bestfitness){
				bestfitness = population.get(i).fitness;
				bestindex = i;
			}
		}
		return population.get(bestindex);
	}
	
	
	protected void initialize(){//TODO finish initialization
		ArrayList v = curGraph;
		Chromosome c = new Chromosome(v);
		for(int i = 0; i < popSize; i++){
			Chromosome c1 = new Chromosome(c.getGenes());
			for(int j = 0; j < curGraph.size(); j++){
				int r = (int) Math.random() * curGraph.get(0).usableColors.size();
				int newcolor = curGraph.get(0).usableColors.get(r);
				c1.setGene(j, newcolor);
				
			}
			c1.fitness = evalFit(c1);
			population.add(c1);
		}
	}
	protected Chromosome mutation(Chromosome frankie){//TODO finish mutation
		for (int i = 0; i < frankie.getNumVertices(); i++) {
			double p = Math.random();
			if (p < mutRate) {
				
					Vertex c = frankie.getGene(i);
					int q = (int) Math.random() * c.usableColors.size();
					int newcolor = c.usableColors.get(q);
					frankie.setGene(i, ((newcolor+c.getColor())%c.usableColors.size()));
				
			}
		}
		
		return frankie;
	}
	protected Chromosome[] selection(){ //TODO finish selection
		Chromosome[] parents = new Chromosome[parentSize];
		for(int i = 0; i < parentSize; i++){
			ArrayList<Chromosome> pool = new ArrayList<Chromosome>();
			for(int j = 0; j < poolSize; j++){
				int r1 = (int) Math.random()*popSize;
				pool.add(population.get(r1));
				
			}
			Chromosome best = pool.get(0);
			for(int k = 1; k < pool.size(); k++){
				if(best.fitness > pool.get(k).fitness){
					best = pool.get(k);
				}
			}
			parents[i] = best;
		}
		
		
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

		Chromosome offspring = new Chromosome(curGraph);
		// probabilistically selects each gene
		for (int i = 0; i < parents[0].getNumVertices(); i++) {
			double p = Math.random();

			for (int j = 0; j < parentProbs.length; j++) {
				if (p < parentProbs[j]) {
					offspring.setGene(i, parents[j].getGene(i).getColor());
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

	public void setPopSize(int popSize) {
		this.popSize = popSize;
	}

	public ArrayList<Chromosome> getPopulation() {
		return population;
	}

	public void setPopulation(ArrayList<Chromosome> population) {
		this.population = population;
	}
	
	

	

}
