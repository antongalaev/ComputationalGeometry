package maps.model;

import java.util.Comparator;
import java.util.TreeSet;

public class SegPoint implements Comparable<SegPoint> {

    public static Comparator<Segment> comp = new Comparator<Segment>() {
        @Override
        public int compare(Segment s1, Segment s2) {
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
    private TreeSet<Segment> U;
    private TreeSet<Segment> C;
    private TreeSet<Segment> L;

    public SegPoint(int x, int y) {
        this.x = x;
        this.y = y;
        U = new TreeSet<>(comp);
        C = new TreeSet<>(comp);
        L = new TreeSet<>(comp);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TreeSet<Segment> getU() {
        return U;
    }

    public TreeSet<Segment> getC() {
        return C;
    }

    public TreeSet<Segment> getL() {
        return L;
    }

    public void appendU(Segment s) {
        U.add(s);
    }

    public void appendC(Segment s) {
        C.add(s);
    }

    public void appendL(Segment s) {
        L.add(s);
    }

    public int compareTo(SegPoint other) {
        if (this == other) return 0;
        int result = this.getY() - other.getY();
        if (Math.abs(result) < 2) {
            result = this.getX() - other.getX();
        }
        return Math.abs(result) < 2 ? 0 : result ;
    }

    public boolean equals(Object o) {
        if (o instanceof SegPoint) {
            SegPoint p = (SegPoint) o;
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

    public static double direction(SegPoint pi, SegPoint pj, SegPoint pk) {
        return (pj.x - pi.x)*(pk.y - pi.y) - (pk.x - pi.x)*(pj.y - pi.y);
    }

    public static boolean onSegment(SegPoint pi, SegPoint pj, SegPoint pk){
        if (Math.min(pi.x, pj.x) <= pk.x &&
                pk.x <= Math.max(pi.x, pj.x) &&
                Math.min(pi.y, pj.y) <= pk.y &&
                pk.y <= Math.max(pi.y, pj.y)) {
            return true;
        }
        return false;
    }
}
