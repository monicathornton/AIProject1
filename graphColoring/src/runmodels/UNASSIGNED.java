package runmodels;

import java.util.Comparator;

/**
 * Created by lisapeters on 9/24/16.
 */
public class UNASSIGNED implements Comparator<Vertex> {

    @Override
    public int compare(Vertex v1, Vertex v2) {
        return v1.getColor() - v2.getColor();
    }
}