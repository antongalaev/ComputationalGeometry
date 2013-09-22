package ai.model;

public class AnyPoint implements Comparable<AnyPoint> {
    private final int x;
    private final int y;
    private Side side;
    private Segment segment;

    public AnyPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int compareTo(AnyPoint other) {
        int result = this.getX() - other.getX();
        if (result == 0) {
            result = this.side.compareTo(other.side);
            if (result == 0){
                result = this.getY() - other.getY();
            }
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o instanceof AnyPoint) {
            AnyPoint p = (AnyPoint) o;
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

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public static int direction(AnyPoint pi, AnyPoint pj, AnyPoint pk) {
        return (pj.x - pi.x)*(pk.y - pi.y) - (pk.x - pi.x)*(pj.y - pi.y);
    }

    public static boolean onSegment(AnyPoint pi, AnyPoint pj, AnyPoint pk){
        if (Math.min(pi.x, pj.x) <= pk.x &&
                pk.x <= Math.max(pi.x, pj.x) &&
                Math.min(pi.y, pj.y) <= pk.y &&
                pk.y <= Math.max(pi.y, pj.y)) {
            return true;
        }
        return false;
    }
}
