package runmodels;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LocalSearchGA extends AbstractAlgorithm {
	protected double mutRate = 0.2; //mutation rate
	protected double killRate = 0.6; //recombination/replacement rate
	protected int popSize = 100; //population size
	protected int numVertices; //number of vertices in the graph
	//curGraph is current best graph
	protected ArrayList<Chromosome> population = new ArrayList<Chromosome>(); //Each chromosome holds a fully colored graph. 
	//All that changes at each iteration is the colors in the graph
	protected int fitConstant = -1; //A constant for how quickly the search is directed away from solutions with conflicts
	protected int poolSize = 2;//parameter for tournament selection
	protected int parentSize = 2;//parameter for recombination
	protected int colorlim = 3;//target coloring
	
	// File writer that writes out the sample run information (if needed)
	BufferedWriter sampleWriter = null;

	// allows user to indicate whether or not this is a sample run (for output
	// purposes)
	boolean sampleRun = false;
	
	
	public LocalSearchGA(ArrayList<Vertex> vertices) throws IOException {//initialize ALL the parameters and call runAlgo
		algo = "GA";
		curGraph = vertices;
		numVertices = vertices.size();
		for(int i = 0; i < numVertices; i++){
			vertices.get(i).createUsableColors(colorlim);
		}
		
		maxIterations = 10000;
		mutRate = 0.1;
		killRate = 0.5;
		popSize = 150;
		fitConstant = -1;
		poolSize = 2;
		parentSize = 2;
		colorlim = 3;
		conflictHistory = new ArrayList<Integer>();
		
		ArrayList<Integer> iter = runAlgo();
		printGandCResult(iter);
	}
	@Override
	public ArrayList<Integer> runAlgo(){
		curIterations = 0;
		initialize();
		int bestfitness = Integer.MIN_VALUE;
		Chromosome best = getBest();
		curGraph = best.genes;
		Double d = killRate*popSize;

		if (sampleRun == true) {
			try {
				printSampleRun1();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int numOffspring = d.intValue();
		//System.out.println((killRate*popSize) + " " + numOffspring);
		
		for(int i = 0; i < population.size(); i++){
			population.get(i).fitness = evalFit(population.get(i));
		}
		
		//Stuff above:all initialization stuff
		//Stuff below: actual algortihm
		while(curIterations < maxIterations && bestfitness < 0){//while end conditions not reached
			//System.out.println(numOffspring);
			
			ArrayList<Chromosome> kiddos = new ArrayList<Chromosome>();//create list of offspring
			for (int j = 0; j < numOffspring; j++){
				
				Chromosome[] parents = selection();//tournament selection			
				Chromosome child = recombination(parents);//uniform recombination
				child = mutation(child);//mutation
				child.fitness = evalFit(child);//evaluate fitness
				kiddos.add(child);
			}
			//Collections.sort(kiddos,(player1, player2) -> player1.getFitness()- player2.getFitness());
			Collections.sort(population, (chromo1, chromo2) -> chromo1.getFitness()- chromo2.getFitness());
			//sort the population in ascending order of fitness
			for(int k = 0; k < kiddos.size(); k++){//replace the lowest fitness members of the population with the children
				population.set(k, kiddos.get(k).clone());
			}
			best = getBest();
			curGraph = best.genes;
			
			conflictHistory.add(best.fitness);//add the best fitness to the history
			bestfitness = best.fitness;
			
			if (sampleRun == true) {
				try {
					printSampleRun2(best.fitness);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}

			curIterations++;
		}
		//print the best graph after algorithm finishes
		if (sampleRun==true) {
			try {
				printSampleRun3();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conflictHistory;
	}
	@Override
	protected int algorithm(int curV) {//not used
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int selectVertex() {//not used
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void assignColorToVertex(int v, int c) {//not used
		// TODO Auto-generated method stub
		
	}
	protected int evalFit(Chromosome c){//Evaluates the fitness of the Chromosome by calling findConflicts in Abstract Algorithm
		ArrayList<Vertex> conflicts = findConflicts(c.genes);
		
		return fitConstant*conflicts.size()/2;
	}
	protected Chromosome getBest(){//get the best member of the population
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
	
	protected void initialize(){//copies the graph popSize times and color each vertex randomly
		ArrayList<Vertex> v = curGraph;
		
		for(int i = 0; i < numVertices; i++){
			v.get(i).createUsableColors(colorlim);
		}//create a list of possible colors in the graph
		Chromosome c = new Chromosome(v);
		for(int i = 0; i < popSize; i++){
			
			
			Chromosome c1 = copyGraph(c);//copy the Chromosome 
			for(int j = 0; j < curGraph.size(); j++){
				
				
				Double r1 = Math.random() * curGraph.get(0).usableColors.size();
				int r = r1.intValue();
				//System.out.println(r);
				int newcolor = curGraph.get(0).usableColors.get(r);//randomly select a color
				//System.out.println(newcolor);
				c1.setGene(j, newcolor);//set the color
				
			}
			c1.fitness = evalFit(c1);//evaluate fitness and add to the population
			population.add(c1);
		}
	}
	protected Chromosome mutation(Chromosome tomut){//mutates the color of vertexes
		Chromosome frankie = tomut.clone();
		for (int i = 0; i < frankie.getNumVertices(); i++) {
			double p = Math.random();
			if (p < mutRate) {
				
					Vertex c = frankie.getGene(i);
					Double q1 =  Math.random() * colorlim;
					int q = q1.intValue();
					int newcolor = c.usableColors.get(q);
					frankie.setGene(i, ((newcolor+c.getColor())%colorlim));
				
			}
		}
		
		return frankie;
	}
	protected Chromosome[] selection(){ //tournament selection
		Chromosome[] parents = new Chromosome[parentSize];
		
	
		for(int i = 0; i < parentSize; i++){//for each set of parents
			ArrayList<Chromosome> pool = new ArrayList<Chromosome>();//create a pool
			for(int j = 0; j < poolSize; j++){
				Double r = Math.random()*popSize;//randomly select a set of individuals from the population for the pool
				int r1 = r.intValue();
				pool.add(population.get(r1));
				
			}
			Chromosome best = pool.get(0);//get the best individual from the pool
			for(int k = 1; k < pool.size(); k++){
				if(best.fitness > pool.get(k).fitness){
					best = pool.get(k);
				}
			}
			parents[i] = best;
		}
				
		return parents;
	}
	protected Chromosome recombination(Chromosome[] parents){//uniform recombination
		int parentSize = parents.length;
		double selectProb = (1 / parentSize);

		double[] parentProbs = new double[parentSize];

		// vector of upper bounds on parent probabilities (uniform)
		parentProbs[0] = selectProb;
		for (int i = 1; i < parentSize; i++) {
			parentProbs[i] = parentProbs[i - 1] + selectProb;
		}

		Chromosome offspring = parents[0].clone();
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
	public Chromosome copyGraph(Chromosome graph){
		//System.out.println("clonging chromosome");
		return graph.clone();
	}
	
	// print intro and initial graph
	public void printSampleRun1() throws IOException {

		sampleWriter
				.write("*******************************************************************************************");
		sampleWriter.newLine();
		sampleWriter
				.write("*******************************************************************************************");
		sampleWriter.newLine();
		sampleWriter
				.write("Running the Local Search using a genetic algorithm to " + colorlim + " color graph with " + curGraph.size() + " vertices");	
		
		sampleWriter.newLine();
		sampleWriter
				.write("*******************************************************************************************");
		sampleWriter.newLine();
		sampleWriter
				.write("*******************************************************************************************");
		sampleWriter.newLine();

		
		sampleWriter.newLine();
		sampleWriter.write("###############################");
		sampleWriter.newLine();
		sampleWriter.write("Initial Graph");
		sampleWriter.newLine();
		sampleWriter.newLine();
		
		printGandCOut(sampleWriter);
		
		sampleWriter.newLine();
	
		sampleWriter.write("###############################");	
		
		sampleWriter.newLine();
		sampleWriter.newLine();		
		sampleWriter.write("Genes in inital population");
		sampleWriter.newLine();		
		
		int count = 0; 
		
		for (int i = 0; i < population.size(); i++) {
			for (int j = 0; j < population.get(i).getGenes().size(); j++) {
				count++;
				sampleWriter.write(population.get(i).getGene(j).getId() + ":" + population.get(i).getGene(j).getColor() + ", ");
			
				if (count == 10) {
					sampleWriter.newLine();
					count = 0; 
				}
			}
		}
		
		sampleWriter.newLine();
		sampleWriter
		.write("----------------------------------------------------------------------------");
		sampleWriter.newLine();
		sampleWriter.write("Algorithm terminates once solution is found or " + maxIterations + " iterations is reached and bestFitness = 0");
		sampleWriter.newLine();
	}
	
	
	// print iteration info
	public void printSampleRun2(int bestFitness) throws IOException {
		sampleWriter.write("Iteration " + curIterations + ": " + bestFitness + " best fitness");
		sampleWriter.newLine();		
	}
	
	
	// print finalGraph
	public void printSampleRun3() throws IOException {
		sampleWriter.newLine();
		sampleWriter
		.write("----------------------------------------------------------------------------");
		
		sampleWriter.newLine();		
		sampleWriter.write("Genes in final population");
		sampleWriter.newLine();		
		
		int count = 0; 
		
		for (int i = 0; i < population.size(); i++) {
			for (int j = 0; j < population.get(i).getGenes().size(); j++) {
				count++;
				sampleWriter.write(population.get(i).getGene(j).getId() + ":" + population.get(i).getGene(j).getColor() + ", ");
			
				if (count == 10) {
					sampleWriter.newLine();
					count = 0; 
				}
			}
		}
		
		
		sampleWriter.newLine();
		sampleWriter.write("###############################");
		sampleWriter.newLine();
		sampleWriter.write("Final Graph");
		sampleWriter.newLine();
		sampleWriter.newLine();
		
		printGandCOut(sampleWriter);
		
		sampleWriter.newLine();
	
		sampleWriter.write("###############################");	

	}
	
	
	// print finalGraph
	public void printSampleRun4(ArrayList<Integer> conflictHistory) throws IOException  {
		sampleWriter.newLine();
		int numConflictsInEntireGraph = 0; 
		
		for (Vertex v: curGraph) {
			numConflictsInEntireGraph += v.getNumConflicts();
		}

		sampleWriter
		.write("*****************************************************************************");
		sampleWriter.newLine();	
		
		if (numConflictsInEntireGraph > 0) {
			sampleWriter.write("Did not find a " + colorlim
					+ " coloring of graph in " + maxIterations
					+ " iterations.");
				sampleWriter.newLine();
		} 	else {
			sampleWriter.write("Found a " + colorlim + " coloring of graph in "
					+ curIterations + " iterations.");
			
			sampleWriter.newLine();
		}
		
		sampleWriter.newLine();			
		sampleWriter
		.write("*****************************************************************************");
		for(int i = 0; i < conflictHistory.size(); i++){
			sampleWriter.write(i + "," + conflictHistory.get(i));
		}
		
	}
	
}