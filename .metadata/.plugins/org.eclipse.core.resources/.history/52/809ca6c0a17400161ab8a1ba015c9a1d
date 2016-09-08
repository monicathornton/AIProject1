package runmodels;
/*
 * The framework for calculating the sigmoidal activation function 
 * (which in this case is logistic).
 */

public class Logistic extends ActivationFunction{

	private double xo;
	private double L;
	private double k;
	
	
	/** 
	 * Generates a standard logistic function (k=1, xo=0, L=1)
	 */
	public Logistic(){
		// standard is k=1, xo=0, L=1
		xo = 0;
		L = 1;
		k = 1;
	}
	
	//calculates the function
	public double calcfx(double x){
		return ((L)/(1 + Math.exp(-1*k*(x-xo))));
	}
	
	//calculates the partial derivative
	public Double partialDeriv(Double neuronOutput){
		return (neuronOutput * (1-neuronOutput));
	}
	
}
