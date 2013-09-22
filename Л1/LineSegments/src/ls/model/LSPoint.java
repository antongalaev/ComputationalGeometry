package ls.model;

/** @author Anton Galaev */

public class LSPoint {
    private final int x;
    private final int y;

    public LSPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static int direction(LSPoint pi, LSPoint pj, LSPoint pk) {
        return (pj.x - pi.x)*(pk.y - pi.y) - (pk.x - pi.x)*(pj.y - pi.y);
    }

    public static boolean onSegment(LSPoint pi, LSPoint pj, LSPoint pk){
        if (Math.min(pi.x, pj.x) <= pk.x &&
                pk.x <= Math.max(pi.x, pj.x) &&
                Math.min(pi.y, pj.y) <= pk.y &&
                pk.y <= Math.max(pi.y, pj.y)) {
            return true;
        }
        return false;
    }
}
