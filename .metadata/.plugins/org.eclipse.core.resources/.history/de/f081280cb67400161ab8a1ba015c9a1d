/*
 * A class to calculate the distance between the target and observed outputs in RBFNN.
 */
package runmodels;

import java.util.ArrayList;

public class Distance {
    public Distance(){
        
    }

	public double calculateDistance(ArrayList<Double>x, ArrayList<Double> means, int dim){
        //System.out.println("distance");
        double d = 0;
        //calculates the distance for each dimension per method described in Haykin's
        //2009 book, Neural networks and learning machines. 
        for(int i = 0; i < dim;i++){
            //System.out.println(means.get(i) + " : " + x.get(i));
            double g = Double.parseDouble(""+means.get(i));
            double h = Double.parseDouble(""+x.get(i));
            d += Math.pow(g-h, 2);
        }
        d = Math.sqrt(d);
        return d;
    }
    
    public double calculateDistance(double x, double means){
        double d = Math.pow(x-means, 2);
        d = Math.sqrt(d);
        return d;
    }
//    public double calculateDifference(ArrayList<Double>expected, ArrayList<Double> observed){
//        double d = 0;
//        for(int i = 0; i < expected.size();i++){
//            d += Math.abs(expected.get(i)-observed.get(i));
//        }
//            return d;
//    }
}
