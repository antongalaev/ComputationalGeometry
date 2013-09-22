package mkm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Face {
    List<Vertex> points = new ArrayList<>();

    public Face(List<Vertex> pts) {
        points = pts;
    }

    public List<Vertex> getPoints() {
        return points;
    }
}
