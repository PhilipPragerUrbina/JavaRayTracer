package simulation.Ray.Tracables;

import simulation.Alg.Vector3;
import simulation.Ray.Acceleration.BVH;
import simulation.Ray.HitData;
import simulation.Ray.Ray;

import java.util.ArrayList;
// a bunch of traceable that are part of a common object that can be traced
//Is a traceable object, can be instanced, can be nested
//Use lots of these, help BVH be more efficient, since it contains its own sub bvh
public class Group implements Traceable {
    private ArrayList<Traceable> objects;
    private BVH acceleration;

    //create a group of objects, and build its acceleration structure.
    public Group(ArrayList<Traceable> objects){
        this.objects = objects;
        buildBVH();
    }

    //re-build the acceleration structure
    public void buildBVH(){
        acceleration = new BVH(objects);
    }

    //trace a ray against all possible objects and return data
    @Override
    public  HitData trace(Ray ray){
            return acceleration.trace(ray);
    }

    @Override
    public Vector3 getMin() {
        return acceleration.getMin();
    }

    @Override
    public Vector3 getMax() {
        return acceleration.getMax();
    }
}
