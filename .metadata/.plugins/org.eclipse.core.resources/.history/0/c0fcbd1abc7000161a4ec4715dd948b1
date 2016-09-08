package runmodels;
/*
 * The framework for calculating the linear activation function for the FFNN.
 */


public class Linear extends ActivationFunction {
	
	// slope
	private double mu = .002;

	@Override
	double calcfx(double x) {
		return x*mu;
	}

	@Override
	Double partialDeriv(Double output) {
		return 1.0;
	}

}
