package runmodels;


public class Arc {
    private Vertex start;
    private Vertex tail;

    public Arc(Vertex start, Vertex tail){
        this.start = start;
        this.tail = tail;
    }

    public Vertex getTail(){return tail;}

    public Vertex getStart(){return start;}

    public void applyConsistency(int startColor){
        if(tail.usableColors.contains(startColor));
        {
            tail.usableColors.remove(startColor);
        }
    }
}
