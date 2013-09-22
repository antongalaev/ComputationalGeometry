package mi.model;

public class Segment {

    private SegPoint up;
    private SegPoint down;
    private double k, b; // line characteristics

    public Segment(SegPoint first, SegPoint second) {
        up = first;
        down = second;
        if (first.compareTo(second) > 0) {
            up = second;
            down = first;
        }
        k = (double)(up.getY() - down.getY()) / (double)(up.getX() - down.getX());
        b = (double)(up.getX() * down.getY() - up.getY() * down.getX()) / (double)(up.getX() - down.getX());
        up.appendU(this);
        down.appendL(this);
    }

    public double countX(double y) {
        if (Math.abs(k) < 0.001) return Integer.MAX_VALUE;
        return (y - b) / k;
    }

    public SegPoint getUp() {
        return up;
    }

    public SegPoint getDown() {
        return down;
    }

    public boolean intersects(Segment other) {
        SegPoint p1 = up,
                p2 = down,
                p3 = other.up,
                p4 = other.down;
        double d1 = SegPoint.direction(p3, p4, p1);
        double d2 = SegPoint.direction(p3, p4, p2);
        double d3 = SegPoint.direction(p1, p2, p3);
        double d4 = SegPoint.direction(p1, p2, p4);
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
                ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))){
            return true;
        }
        else if (d1 == 0 && SegPoint.onSegment(p3, p4, p1)) {
            return true;
        }
        else if (d2 == 0 && SegPoint.onSegment(p3, p4, p2)) {
            return true;
        }
        else if (d3 == 0 && SegPoint.onSegment(p1, p2, p3)){
            return true;
        }
        else if (d4 == 0 && SegPoint.onSegment(p1, p2, p4)){
            return true;
        }
        return false;
    }

    public SegPoint countLineCross(Segment other) {
        double x, y;
        if (Math.abs(other.k) == Double.POSITIVE_INFINITY) {
            x = other.up.getX();
            y = this.k * x + this.b;
        } else if (Math.abs(this.k) == Double.POSITIVE_INFINITY) {
            x = this.up.getX();
            y = other.k * x + other.b;
        } else {
            x = (this.b - other.b) / (other.k - this.k);
            y = this.k * x + this.b;
        }
        SegPoint cross = new SegPoint((int)(x + 0.5), (int)(y + 0.5));
        cross.appendC(this);
        cross.appendC(other);
        return cross;
    }

    public String toString() {
        return String.format("Segment: from " + up + " to " + down);
    }
}
