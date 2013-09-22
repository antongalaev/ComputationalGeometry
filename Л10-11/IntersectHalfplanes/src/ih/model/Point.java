package ih.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Администратор
 * Date: 21.04.13
 * Time: 21:29
 */
public class Point {

    private double x;
    private double y;
    private List<Halfplane> halfplanes = new ArrayList<>();



    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point(java.awt.Point point) {
        x = point.getX();
        y = point.getY();
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public List<Halfplane> getHalfplanes() {
        return halfplanes;
    }

    public void addHalfplanes(Halfplane... h) {
        halfplanes.addAll(Arrays.asList(h));
    }
}
