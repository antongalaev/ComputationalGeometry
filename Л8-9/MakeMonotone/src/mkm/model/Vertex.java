package mkm.model;

import java.util.Comparator;
import java.util.TreeSet;

public class Vertex implements Comparable<Vertex> {

    public static Comparator<Edge> comp = new Comparator<Edge>() {
        @Override
        public int compare(Edge s1, Edge s2) {
            if (s1 == s2) return 0;
            double result = s1.countX(currY) - s2.countX(currY);
            if (result > 0) return 1;
            if (result < 0) return -1;
            return 0;
        }
    };
    public static int currY;

    private final int x;
    private final int y;
    private Edge edge;
    private Vertex prev;
    private Vertex next;
    private VertexType type;

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int compareTo(Vertex other) {
        if (this == other) return 0;
        int result = this.getY() - other.getY();
        if (result == 0) {
            result = this.getX() - other.getX();
        }
        return result ;
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

    public VertexType getType() {
        return type;
    }

    public void setType(VertexType type) {
        this.type = type;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public Vertex getPrev() {
        return prev;
    }

    public void setPrev(Vertex prev) {
        this.prev = prev;
    }

    public Vertex getNext() {
        return next;
    }

    public void setNext(Vertex next) {
        this.next = next;
    }
}
