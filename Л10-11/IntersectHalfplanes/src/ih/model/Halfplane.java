package ih.model;

import java.util.List;

/**
 * User: Администратор
 * Date: 21.04.13
 * Time: 20:07
 */
public class Halfplane extends ConvexRegion {
    private double k;
    private double b;
    private boolean below;

    public Halfplane (Point p1, Point p2, Point p3) {
        k = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
        b = (p1.getX() * p2.getY() - p1.getY() * p2.getX()) / (p1.getX() - p2.getX());
        if (p3.getY() > y(p3.getX())) {
            below = true;
        } else {
            below = false;
        }
        left.add(this);
    }

    public double y(double x) {
        return getK() * x + getB();
    }

    public boolean isBelow() {
        return below;
    }

    public double getK() {
        return k;
    }

    public double getB() {
        return b;
    }
}
