package runmodels;

/*
 * Class to construct a radial basis function neural network (RBFNN, hereafter) with an arbitrary
 * number of inputs, Gaussian basis functions, and an arbitrary number of outputs.
 * Program also accepts the number of inputs, Gaussians, and outputs. Activation function
 * used is a Gaussian activation function.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class KernelANN {

    //private ArrayList<Double> inputs;
    private ArrayList<RBFNode> functions = new ArrayList();//the list of RBF functions
    private ArrayList<Double> outputs = new ArrayList();//ordinarily a single double but need an arbitrary number of outputs
    private ArrayList<Double> targets = new ArrayList();//list of outputs of Rosenbrock function, our ideal numbers
    private double eta = 0.05;//learning rate
    private Distance dist = new Distance();
    private double variance; //a constant representation of error in the dataset
    public double oldError;//Error with each training example
    public double newError; //Error after update
    //public double totalError;
    public int dim2 = 0;//number of dimensions in the data set, set at read data
    public int datasize = 0;//number of items in the data set
    //a series of terms used to determine if the program is returning the same output unnecessarily
    private double isStuck; 
    private double isStuck2;
    private double isStuck3;
    private boolean stuck;
    public double meanError; 
    public KernelANN() {
    }

    //builds the RBF network
    public void buildNetwork(int numInputs, int numOutputs, int numFunctions) {
        Random rand = new Random();
        double r = 0.0;
        for (int j = 0; j < numOutputs; j++) {//construct output nodes
            outputs.add(1.0);
        }
        for (int m = 0; m < numFunctions; m++) {//construct basis function nodes
            RBFNode node = new RBFNode(numOutputs);
            functions.add(node);
        }
    }

    //
    public ArrayList<ArrayList> readData(String fname) {
        BufferedReader br = null;//read from data
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<ArrayList> input = new ArrayList();//list of examples in data set
        int count = 0;
        try {
            br = new BufferedReader(new FileReader(fname));
            while ((line = br.readLine()) != null) {
                String[] example = line.split(cvsSplitBy);
                dim2 = example.length;//set number of dimensions in dataset
                input.add(new ArrayList());
                for (int i = 0; i < dim2 - 1; i++) {
                    int x = Integer.parseInt(example[i]);//change input to integers
                    input.get(count).add(x);//add each dimension to list
                }
                dim2 = dim2 - 1;
                double x = Double.parseDouble(example[dim2]);//add each output to the target
                targets.add(x);
                count++;
            }
            datasize = count;//set the datasize so we know where to count to
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
    
    //reads in input/output data from file (as .csv), trains network
    public int trainNetwork(String fname, int k, double threshold, String outputFile) {
        BufferedWriter bw = null;
        //String outputFile = "";
        int count2 = 0;
        try {
            isStuck = -1;
            isStuck = -2;
            stuck = false;
            bw = new BufferedWriter(new FileWriter(outputFile));
            ArrayList<ArrayList> input = readData(fname);//initialize data
            kMeansClustering(k, input, datasize);//use input to cluster data
            meanError = oldError = (double) (Double.MAX_VALUE);//set initial error to maximum value

            while (meanError > threshold && !stuck) {//until either convergence, or a certain number of iterations
                for (int i = 0; i < datasize; i++) {//for each training example
                    for (int j = 0; j < k; j++) {//for each RBF function
                        functions.get(j).activationOut
                                = functions.get(j).calculateActivation(input.get(i), variance, dim2);//input each dimension into each RBF and calculate activation
                    }
                    
                    double oldOut = outputs.get(i);
                    double newOut = generateOutputs();//generate the output for the training example
                    if(i == datasize-1){
                        
                        isStuck = isStuck2;
                        isStuck2 = isStuck3;
                        isStuck3 = newOut;
                        if(isStuck == isStuck2 && isStuck2 == isStuck3){
                            stuck = true;
                        }
                    }
                    updateAllWeights(targets.get(i), newOut);//update all the weights using the new output
                    outputs.set(i, newOut);
                    newError = calculateTotalError(targets, outputs);
                    if (newError < oldError || count2==0) {
                        //System.out.println("out: " + outputs + ", targets: " + targets + ", Error: " + newError);
                        oldError = newError;
                    } else {
                        //System.out.println("out: " + outputs + ", targets: " + targets + ", weights reverted: " + newError);
                        outputs.set(i, oldOut);
                        oldError = calculateTotalError(targets, outputs);
                        revertWeights();
                    }
                    meanError = oldError/datasize;
                }
                count2++;
                bw.newLine();
                bw.write("Iteration: " + count2 + ", Total Error: " + oldError + ", Variance: " + variance);

                System.out.println("Iteration: " + count2 + ", Total Error: " + oldError + ", Variance: " + variance);
            }
            bw.close();
            return count2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count2;
    }

    //calculates the total error (that is, the distance between ALL target and observed outputs)
    private double calculateTotalError(ArrayList<Double> target, ArrayList<Double> out) {
        double sum = 0;
        for(int i = 0; i < target.size(); i++){
            sum += Math.abs(target.get(i) - out.get(i));
        }
        return sum;
    }

    //calculates the distance between target and observed output
    private double calcError(ArrayList target, ArrayList out) {
        return dist.calculateDistance(target, out, 1);
    }

    //calculates the distance between target and observed output (as a double)    
    private double calcError(double target, double out) {
        return dist.calculateDistance(target, out);
    }

    //calculates the variance for the kMeans clustering
    public void kMeansClustering(int k, ArrayList<ArrayList> in, int outDim) {//input all examples and k
        KMeans kmeans = new KMeans();
        ArrayList<ArrayList> meanslist = kmeans.createClusters(k, in, dim2);
        buildNetwork(dim2, outDim, k);
        for (int i = 0; i < functions.size(); i++) {
            //System.out.println("means: " + meanslist.get(i));
            functions.get(i).means.addAll(meanslist.get(i));
        }
        variance = kmeans.maxD / 2 * Math.sqrt(k);
    }

    //performs the weight update for each of the individual edges
    private double updateIndWeight(double inweight, double input, double target, double observed) {
        double outweight = inweight;
        outweight += (target - observed) * eta * input;//multiply this by input
        return outweight;
    }

    //updates weights for the entire network
    private void updateAllWeights(double target, double output) {
        double x = 0;
        for (int i = 0; i < functions.size(); i++) {//for each hidden node
            //for (int j = 0; j < 1; j++) {

            functions.get(i).oldweight = functions.get(i).outweight;
            x = updateIndWeight(functions.get(i).outweight,
                    functions.get(i).activationOut, target, output);
            functions.get(i).outweight = x;
            //System.out.println("new weight: " + functions.get(i).outweight + ", old weight: " + functions.get(i).oldweight);
            
            //}
        }
    }

    //generates the outputs for each node(s) in the output layer
    private double generateOutputs() {
        double sum = 0;
        for (int i = 0; i < 1; i++) {
            //for each output node calculate weighted sum of hidden layers
            for (int j = 0; j < functions.size(); j++) {//each hidden node has output
                sum += functions.get(j).activationOut * functions.get(j).outweight;
            }
        }
        return sum;
    }

    //reverts the weights, when a worse guess is made
    private void revertWeights() {
        //System.out.println("Weights reverted");
        for (int i = 0; i < functions.size(); i++) {
            for (int j = 0; j < 1; j++) {
                functions.get(i).outweight = functions.get(i).oldweight;
            }
        }
    }

    //tests a single input node
    public double testSingleInput(ArrayList input) {
        double stuff = 0;
        double otherStuff = 0;
        //multiplies the activation function by the out weight for that node
        for (int i = 0; i < functions.size(); i++) {
            stuff = functions.get(i).calculateActivation(input, variance, dim2);
            otherStuff += stuff * functions.get(i).outweight;
        }
        return otherStuff;
    }

    //tests the observed outputs against the target outputs
    public void testNetwork(String testData, String outputFile) {
        BufferedReader br = null;//read from data
        BufferedWriter bw = null;
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<ArrayList> input = new ArrayList();//list of examples in data set
        ArrayList<Double> targetOut = new ArrayList<Double>();
        int testLen = 0;
        int count = 0;
        //reads in target output (for testing)
        try {
            br = new BufferedReader(new FileReader(testData));
            while ((line = br.readLine()) != null) {
                String[] example = line.split(cvsSplitBy);
                input.add(new ArrayList());
                for (int i = 0; i < dim2; i++) {
                    int x = Integer.parseInt(example[i]);//change input to integers
                    input.get(count).add(x);//add each dimension to list
                }
                double x = Double.parseDouble(example[dim2]);//add each output to the target
                targetOut.add(x);
                count++;
            }
            testLen = count;
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //writes out observed output
        try {
            //String outputFile = "";
            bw = new BufferedWriter(new FileWriter(outputFile));
            bw.newLine();
            bw.write("input array, output, target, error");
            for (int i = 0; i < testLen; i++) {
                bw.newLine();
                double out = testSingleInput(input.get(i));
                double error = calcError(targetOut.get(i), out);
                bw.write(input.get(i) + ", " + out + ", " + targetOut.get(i) + ", " + error);

            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
