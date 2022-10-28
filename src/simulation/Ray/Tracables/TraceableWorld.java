package simulation.Ray.Tracables;

import simulation.Alg.Vector3;
import simulation.Ray.HitData;
import simulation.Ray.Ray;

import java.util.ArrayList;
// a world that can be traced
public class TraceableWorld {
    private ArrayList<Traceable> objects;
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

    //trace a ray against all possible objects and return data
    public  HitData trace(Ray ray){
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
