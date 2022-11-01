package simulation.Ray.Tracables;

import simulation.Alg.Vector3;
import simulation.Ray.HitData;
import simulation.Ray.Ray;

//an object that can be intersected by a ray
public interface Traceable {
    //trace a ray against the object, return the hit data
    HitData trace(Ray ray);

    //get bounds
    Vector3 getMin();
    Vector3 getMax();
}
