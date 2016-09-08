package runmodels;

/*
 * A class for calculating the sigmoidal activation function for the FFNN 
 * (which, in this case is the logistic function). 
 */

public class LogisticFunction extends AbstractFunction {
	
	private double xo;
	private double L;
	private double k;
	
	/** 
	 * Generates a logistic function with user-specified parameters (we probably won't need this)
	 */
	public LogisticFunction(double xo, double L, double k){
		this.xo = xo;
		this.L = L;
		this.k = k;
	}
	
	/** 
	 * Generates a standard logistic function (k=1, xo=0, L=1)
	 */
	public LogisticFunction(){
		// standard is k=1, xo=0, L=1
		xo = 0;
		L = 1;
		k = 1;
	}
	
	public double calcfx(double x){
		return ((L)/(1 + Math.exp(-1*k*(x-xo))));
	}
	
	public double calcderivfx(double x){
		return calcfx(x)*(1-calcfx(x));
	}

}
