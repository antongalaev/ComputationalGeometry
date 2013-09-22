package maps.model;

import java.util.List;

public class Face {

    private HalfEdge outerComponent;
    private List<HalfEdge> innerComponents;

    public HalfEdge getOuterComponent() {
        return outerComponent;
    }

    public void setOuterComponent(HalfEdge outerComponent) {
        this.outerComponent = outerComponent;
    }

    public List<HalfEdge> getInnerComponents() {
        return innerComponents;
    }

    public void setInnerComponents(List<HalfEdge> innerComponents) {
        this.innerComponents = innerComponents;
    }
}
