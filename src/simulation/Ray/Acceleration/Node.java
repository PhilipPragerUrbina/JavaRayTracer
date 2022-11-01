package simulation.Ray.Acceleration;

import simulation.Ray.Tracables.Traceable;

public class Node {
    BoundingBox box;
    Node[] children;
    Traceable thing = null; //not null means leaf node

    public Node(BoundingBox box, Node[] children, Traceable thing) {
        this.box = box;
        this.children = children;
        this.thing = thing;
    }

    public Node(BoundingBox box, Node[] children) {
        this.box = box;
        this.children = children;
    }

}
