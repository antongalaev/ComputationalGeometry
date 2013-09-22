package ai.model;

import java.util.TreeSet;

public class SweepStatus {

    private TreeSet<Segment> tree = new TreeSet<Segment>();

    public void insert(Segment s) {
        tree.add(s);
    }

    public void delete(Segment s) {
        tree.remove(s);
    }

    public Segment above(Segment s) {
        return tree.higher(s);
    }

    public Segment below(Segment s) {
        return tree.lower(s);
    }

    public void clear() {
        tree.clear();
    }
}
