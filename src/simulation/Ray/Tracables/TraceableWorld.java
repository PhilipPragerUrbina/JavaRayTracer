package simulation.Ray.Tracables;

import simulation.Alg.Vector3;
import simulation.Ray.Acceleration.BVH;
import simulation.Ray.HitData;
import simulation.Ray.Ray;

import java.util.ArrayList;
// a world that can be traced
public class TraceableWorld {
    private ArrayList<Traceable> objects;
    private BVH acceleration = null;
    //create an empty world
    public  TraceableWorld(){
        objects = new ArrayList<>();
    }
    //create a world from a list of objects
    public TraceableWorld(ArrayList<Traceable> objects){
        this.objects = objects;
    }

    public void addObject(Traceable object){
        objects.add(object);
    }

    //build the acceleration structure
    public void buildBVH(){
        acceleration = new BVH(objects);
    }

    //trace a ray against all possible objects and return data
    public  HitData trace(Ray ray){
        if(acceleration != null) { //use acceleration structure if built
            return acceleration.trace(ray);

        }
        //fall back on classic hit
        HitData closest = new HitData(); //empty/invalid hit data
        for (Traceable o :
                objects) {
            HitData hit = o.trace(ray);
            if(hit.didHit()) {
                if (hit.getDistance() < closest.getDistance() || !closest.didHit())
                    closest = hit;
            }
        }
        return closest;
    }
}
