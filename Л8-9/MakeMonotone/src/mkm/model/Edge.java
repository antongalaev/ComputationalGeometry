package mkm.model;

public class Edge {

    private Vertex up;
    private Vertex down;
    private Vertex helper;
    private double k, b; // line characteristics

    public Edge(Vertex first, Vertex second) {
        up = first;
        down = second;
        if (first.compareTo(second) > 0) {
            up = second;
            down = first;
        }
        k = (double)(up.getY() - down.getY()) / (double)(up.getX() - down.getX());
        b = (double)(up.getX() * down.getY() - up.getY() * down.getX()) / (double)(up.getX() - down.getX());
    }

    public double countX(double y) {
        if (Math.abs(k) < 0.001) return Integer.MAX_VALUE;
        return (y - b) / k;
    }

    public Vertex getUp() {
        return up;
    }

    public Vertex getDown() {
        return down;
    }

    public boolean intersects(Edge other) {
        Vertex p1 = up,
                p2 = down,
                p3 = other.up,
                p4 = other.down;
        double d1 = Vertex.direction(p3, p4, p1);
        double d2 = Vertex.direction(p3, p4, p2);
        double d3 = Vertex.direction(p1, p2, p3);
        double d4 = Vertex.direction(p1, p2, p4);
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
                ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))){
            return true;
        }
        else if (d1 == 0 && Vertex.onSegment(p3, p4, p1)) {
            return true;
        }
        else if (d2 == 0 && Vertex.onSegment(p3, p4, p2)) {
            return true;
        }
        else if (d3 == 0 && Vertex.onSegment(p1, p2, p3)){
            return true;
        }
        else if (d4 == 0 && Vertex.onSegment(p1, p2, p4)){
            return true;
        }
        return false;
    }

    public Vertex countLineCross(Edge other) {
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
        Vertex cross = new Vertex((int)(x + 0.5), (int)(y + 0.5));
        return cross;
    }

    public String toString() {
        return String.format("Edge: from " + up + " to " + down);
    }

    public Vertex getHelper() {
        return helper;
    }

    public void setHelper(Vertex helper) {
        this.helper = helper;
    }
}
