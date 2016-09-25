package runmodels;

import java.util.Comparator;

/**
 * Created by lisapeters on 9/24/16.
 */
public class HIGH_DEGREE implements Comparator<Vertex>{

    @Override
    public int compare(Vertex v1, Vertex v2) {
        return v2.getNeighbors().size() - v1.getNeighbors().size() ;
    }
}
