package ih.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Администратор
 * Date: 21.04.13
 * Time: 21:07
 */
public class ConvexRegion {

    List<Halfplane> left = new ArrayList<>();
    List<Halfplane> right = new ArrayList<>();

    public ConvexRegion() {
    }

    public boolean containsPoint(Point p) {
        List<Halfplane> allPlanes = new ArrayList<>();
        allPlanes.addAll(left);
        allPlanes.addAll(right);
        for (Halfplane h: allPlanes) {
            if (h.isBelow() && p.getY() <= h.y(p.getX())) {
                return false;
            }
            if (! h.isBelow() && p.getY() >= h.y(p.getX())) {
                return false;
            }
        }
        return true;
    }

    public List<Point> getPoints(ConvexRegion other) {
        List<Point> result = new LinkedList<>();
        List<Halfplane> allPlanes = new ArrayList<>();
        allPlanes.addAll(left);
        allPlanes.addAll(right);
        List<Halfplane> otherPlanes = new ArrayList<>();
        allPlanes.addAll(other.left);
        allPlanes.addAll(other.right);
        for (int i = 0; i < allPlanes.size(); ++ i ) {
            for (int j = 0; j < allPlanes.size(); ++ j) {
                if (i != j) {
                    double x = (allPlanes.get(j).getB() - allPlanes.get(i).getB()) /
                            (allPlanes.get(i).getK() - allPlanes.get(j).getK());
                    double y = allPlanes.get(i).y(x);
                    Point p = new Point(x, y);
                    if (containsPoint(p)) {
                        p.addHalfplanes(allPlanes.get(i), allPlanes.get(j));
                        result.add(p);
                    }
                }
            }
            for (int j = 0; j < otherPlanes.size(); ++ j) {
                double x = (otherPlanes.get(j).getB() - allPlanes.get(i).getB()) /
                        (allPlanes.get(i).getK() - otherPlanes.get(j).getK());
                double y = allPlanes.get(i).y(x);
                Point p = new Point(x, y);
                if (containsPoint(p)) {
                    p.addHalfplanes(allPlanes.get(i), otherPlanes.get(j));
                    result.add(p);
                }
            }
        }
        return result;
    }

    public ConvexRegion(List<Point> points) {
        for (Point p : points) {
            left.addAll(p.getHalfplanes());
        }
    }
}
