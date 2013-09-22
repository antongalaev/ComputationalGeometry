package cp.model;

import java.util.Comparator;

public class CpPoint {
    private final int x;
    private final int y;

    public CpPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Comparator<CpPoint> comparatorX = new Comparator<CpPoint>() {
        @Override
        public int compare(CpPoint o1, CpPoint o2) {
            return o1.x - o2.x;
        }
    };

    public static Comparator<CpPoint> comparatorY = new Comparator<CpPoint>() {
        @Override
        public int compare(CpPoint o1, CpPoint o2) {
            return o1.y - o2.y;
        }
    };

}
