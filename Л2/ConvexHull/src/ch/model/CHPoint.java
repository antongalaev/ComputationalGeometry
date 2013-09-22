package ch.model;

public class CHPoint
        implements Comparable<CHPoint> {
    private final int x;
    private final int y;
    private static CHPoint min;

    public CHPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int compareTo(CHPoint other) {
        int product = direction(min, this, other);
        if (product > 0) {
            return -1;
        }
        if (product < 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CHPoint) {
            CHPoint op = (CHPoint) other;
            return x == op.x && y == op.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 13 * hash + x;
        hash = 13 * hash + y;
        return hash;
    }

    public static int direction(CHPoint prev, CHPoint curr, CHPoint next) {
        return (curr.x - prev.x)*(next.y - prev.y) - (next.x - prev.x)*(curr.y - prev.y);
    }

    public static void setMin(CHPoint min) {
        CHPoint.min = min;
    }
}
