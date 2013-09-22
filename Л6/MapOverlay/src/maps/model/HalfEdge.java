package maps.model;

public class HalfEdge {

    private Vertex origin;
    private HalfEdge twin;
    private Face incidentFace;
    private HalfEdge prev;
    private HalfEdge next;

    public Vertex getOrigin() {
        return origin;
    }

    public void setOrigin(Vertex origin) {
        this.origin = origin;
    }

    public HalfEdge getTwin() {
        return twin;
    }

    public void setTwin(HalfEdge twin) {
        this.twin = twin;
    }

    public Face getIncidentFace() {
        return incidentFace;
    }

    public void setIncidentFace(Face incidentFace) {
        this.incidentFace = incidentFace;
    }

    public HalfEdge getPrev() {
        return prev;
    }

    public void setPrev(HalfEdge prev) {
        this.prev = prev;
    }

    public HalfEdge getNext() {
        return next;
    }

    public void setNext(HalfEdge next) {
        this.next = next;
    }

    private double k, b; // line characteristics

    public HalfEdge() {

    }

    public HalfEdge(Vertex first, Vertex second) {
        origin = first;
        twin = new HalfEdge();
        twin.origin = second;
        k = twin.k = (double)(origin.getY() - twin.origin.getY()) / (double)(origin.getX() - twin.origin.getX());
        b = twin.b = (double)(origin.getX() * twin.origin.getY() - origin.getY() * twin.origin.getX()) /
                (double)(origin.getX() - twin.origin.getX());
        origin.appendU(this);
        twin.origin.appendL(this);
    }

    public double countX(double y) {
        if (Math.abs(k) < 0.001) return Integer.MAX_VALUE;
        return (y - b) / k;
    }

}
