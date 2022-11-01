package simulation.Ray.Acceleration;

// a bounding volume hierarchy

import simulation.Alg.Vector3;
import simulation.Ray.HitData;
import simulation.Ray.Ray;
import simulation.Ray.Tracables.Traceable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BVH implements Traceable{
    private Node root_node;

    public BVH(ArrayList<Traceable> objects){
        //sort objects by a spatial dimension for splitting(x)
        Collections.sort(objects, new Comparator<Traceable>() {
            @Override
            public int compare(Traceable lhs, Traceable rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                //todo get bounding middle
                return lhs.getMin().x > rhs.getMin().x ? -1 : (lhs.getMin().x < rhs.getMin().x) ? 1 : 0;
            }});

            //create nodes
            root_node = new Node(objects);

    }

    @Override
    public HitData trace(Ray ray) {
        return root_node.hit(ray);
    }

    @Override
    public Vector3 getMin() {
        return root_node.getBox().getMin();
    }
    @Override
    public Vector3 getMax() {
        return root_node.getBox().getMax();
    }
}
