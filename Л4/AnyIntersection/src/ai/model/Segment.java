package ai.model;

public class Segment implements Comparable<Segment> {

    private AnyPoint left;
    private AnyPoint right;
    private double k, b; // line characteristics

    public Segment(AnyPoint first, AnyPoint second) {
        if (first.getX() < second.getX()) {
            left = first;
            right = second;
        } else {
            left = second;
            right = first;
        }
        left.setSide(Side.LEFT);
        right.setSide(Side.RIGHT);
        left.setSegment(this);
        right.setSegment(this);
        k = (double)(left.getY() - right.getY()) / (double)(left.getX() - right.getX());
        b = (double)(left.getX() * right.getY() - left.getY() * right.getX()) / (double)(left.getX() - right.getX());
    }

    public double countY(int x) {
        return k*x + b;
    }

    public AnyPoint getLeft() {
        return left;
    }

    public AnyPoint getRight() {
        return right;
    }

    public boolean intersects(Segment other) {
        AnyPoint p1 = left,
                 p2 = right,
                 p3 = other.left,
                 p4 = other.right;
        int d1 = AnyPoint.direction(p3, p4, p1);
        int d2 = AnyPoint.direction(p3, p4, p2);
        int d3 = AnyPoint.direction(p1, p2, p3);
        int d4 = AnyPoint.direction(p1, p2, p4);
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
                ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))){
            return true;
        }
        else if (d1 == 0 && AnyPoint.onSegment(p3, p4, p1)) {
            return true;
        }
        else if (d2 == 0 && AnyPoint.onSegment(p3, p4, p2)) {
            return true;
        }
        else if (d3 == 0 && AnyPoint.onSegment(p1, p2, p3)){
            return true;
        }
        else if (d4 == 0 && AnyPoint.onSegment(p1, p2, p4)){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Segment o) {
        if (this == o) return 0;
        double result = this.getLeft().getY() - o.countY(this.getLeft().getX());
        if (result > 0) return 1;
        if (result < 0) return -1;
        return 0;
    }
}
