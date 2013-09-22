package maps.model;

import java.util.Comparator;
import java.util.TreeSet;

public class Vertex
        implements Comparable<Vertex> {

    private int x;
    private int y;
    private HalfEdge incidentEdge;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public HalfEdge getIncidentEdge() {
        return incidentEdge;
    }

    public void setIncidentEdge(HalfEdge incidentEdge) {
        this.incidentEdge = incidentEdge;
    }

    public static Comparator<HalfEdge> comp = new Comparator<HalfEdge>() {
        @Override
        public int compare(HalfEdge s1, HalfEdge s2) {
            if (s1 == s2) return 0;
            double result = s1.countX(currY) - s2.countX(currY);
            if (result > 0) return 1;
            if (result < 0) return -1;
            return 0;
        }
    };
    public static int currY;
    private TreeSet<HalfEdge> U;
    private TreeSet<HalfEdge> C;
    private TreeSet<HalfEdge> L;

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
        U = new TreeSet<>(comp);
        C = new TreeSet<>(comp);
        L = new TreeSet<>(comp);
    }

    public TreeSet<HalfEdge> getU() {
        return U;
    }

    public TreeSet<HalfEdge> getC() {
        return C;
    }

    public TreeSet<HalfEdge> getL() {
        return L;
    }

    public void appendU(HalfEdge s) {
        U.add(s);
    }

    public void appendC(HalfEdge s) {
        C.add(s);
    }

    public void appendL(HalfEdge s) {
        L.add(s);
    }

    public int compareTo(Vertex other) {
        if (this == other) return 0;
        int result = this.getY() - other.getY();
        if (Math.abs(result) < 2) {
            result = this.getX() - other.getX();
        }
        return Math.abs(result) < 2 ? 0 : result ;
    }

    public boolean equals(Object o) {
        if (o instanceof Vertex) {
            Vertex p = (Vertex) o;
            return x == p.x && y == p.y;
        }
        return false;
    }

    public int hashCode() {
        int hash = 1;
        hash = 13 * hash + x;
        hash = 13 * hash + y;
        return hash;
    }

    public String toString() {
        return String.format("Point: (%d, %d)", x, y);
    }

    public static double direction(Vertex pi, Vertex pj, Vertex pk) {
        return (pj.x - pi.x)*(pk.y - pi.y) - (pk.x - pi.x)*(pj.y - pi.y);
    }

    public static boolean onSegment(Vertex pi, Vertex pj, Vertex pk){
        if (Math.min(pi.x, pj.x) <= pk.x &&
                pk.x <= Math.max(pi.x, pj.x) &&
                Math.min(pi.y, pj.y) <= pk.y &&
                pk.y <= Math.max(pi.y, pj.y)) {
            return true;
        }
        return false;
    }

}
