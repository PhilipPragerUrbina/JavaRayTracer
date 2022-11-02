package simulation.Ray.Tracables;

import simulation.Alg.Vector3;
import simulation.Ray.Acceleration.BVH;
import simulation.Ray.HitData;
import simulation.Ray.Ray;

import java.util.ArrayList;
// a bunch of traceable that are part of a common object that can be traced
//each group can be instanced into a different position, and has it's own bvh
//groups can and should be nested
public class Group implements Traceable {
    private ArrayList<Traceable> objects;
    private BVH acceleration;
    private Vector3 position;


    //create a group of objects, and build its acceleration structure. Can be offset into a position
    public Group(ArrayList<Traceable> objects, Vector3 position){
        this.position = position;
        this.objects = objects;
        buildBVH();
    }

    //create a group of objects, and build its acceleration structure. is at origin.
    public Group(ArrayList<Traceable> objects){
        this(objects,new Vector3());
    }

    //re-build the acceleration structure
    public void buildBVH(){
        acceleration = new BVH(objects);
    }



    //trace a ray against all possible objects and return data
    @Override
    public  HitData trace(Ray ray){
        //offset ray to offset whole group's position
        Ray offset_ray = new Ray(ray.getOrigin().subtract(position), ray.getDirection());

            return acceleration.trace(offset_ray);
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
