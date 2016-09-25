package runmodels;

import java.util.Comparator;

/**
 * Created by lisapeters on 9/24/16.
 */
public class FEWEST_LEGAL implements Comparator<Vertex> {

    @Override
    public int compare(Vertex v1, Vertex v2) {
        return v1.usableColors.size() - v2.usableColors.size() ;
    }
}