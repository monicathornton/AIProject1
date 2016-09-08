/*
 * Constructs a node in an RBFNN, determines in weights, activation function. 
 */
package runmodels;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Janette
 */
public class RBFNode {
    double outweight; //weights to each output node from this RBF
    double oldweight;//array to keep track of old weights so can revert if error is bigger
    ArrayList<Double> means = new ArrayList<Double>(); //center of the cluster as vector
    double activationOut;//output calculate Activation function
    double input;
    Distance dist = new Distance();
    public RBFNode(int numOutputs){
        Random rand = new Random();
        for(int i = 0; i < numOutputs; i++){
            double r = rand.nextDouble();
            outweight = r;
            oldweight = r;
        }
    }
    public double calculateActivation(ArrayList<Double> x, double var, int dim){
        double variance = 1/(2*Math.pow(var, 2));//calculates 1/2*sigma^2
        double error = Math.pow(dist.calculateDistance(x, means, dim),2);//Calculates the squared distance between the input and the mean
        //System.out.println("means: " + means);
        return Math.exp(variance*error);//outputs e^variance*error
    }
    
}
