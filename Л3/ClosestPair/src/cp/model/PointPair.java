package cp.model;

public class PointPair {

    private CpPoint[] pair = new CpPoint[2];
    private double distance;

    public PointPair(CpPoint first, CpPoint second) {
        getPair()[0] = first;
        getPair()[1] = second;
        distance = Math.sqrt((first.getX()-second.getX())*(first.getX()-second.getX()) +
                             (first.getY()-second.getY())*(first.getY()-second.getY()));
    }

    public double getDistance() {
        return distance;
    }

    public static double countDistance(CpPoint first, CpPoint second) {
        return Math.sqrt((first.getX()-second.getX())*(first.getX()-second.getX()) +
                        (first.getY()-second.getY())*(first.getY()-second.getY()));
    }

    public CpPoint[] getPair() {
        return pair;
    }
}
