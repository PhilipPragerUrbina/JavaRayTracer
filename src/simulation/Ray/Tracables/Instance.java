package simulation.Ray.Tracables;

import simulation.Alg.Vector3;
import simulation.Ray.Acceleration.BVH;
import simulation.Ray.HitData;
import simulation.Ray.Ray;

import java.util.ArrayList;


//Move or scale an object without even re instantiating it
//basically a group with one object
//works by changing the ray,rather than the object itself, meaning you can re-use a single object with different transforms
public class Instance implements Traceable {
    private Traceable object;
    private Vector3 position;

    //Create instance that can scale and translate
    public Instance(Traceable object, Vector3 position){
        this.position = position;
        this.object = object;
    }





    //trace a ray against object
    @Override
    public  HitData trace(Ray ray){
        //todo fix scaling, not occluding correctly
        //offset ray to offset object's position and scale without changing the object itself
        Ray offset_ray = new Ray(ray.getOrigin().subtract(position), ray.getDirection());
        return object.trace(offset_ray);
    }

    //pass through min and max, but offset and dilate
    @Override
    public Vector3 getMin() {
        return object.getMin().add(position);
    }

    @Override
    public Vector3 getMax() {
        return object.getMax().add(position);
    }

}
