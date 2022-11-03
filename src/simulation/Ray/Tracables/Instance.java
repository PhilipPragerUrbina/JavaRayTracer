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
    private Vector3 scale;

    //Create instance that can scale and translate
    public Instance(Traceable object, Vector3 position, Vector3 scale){
        this.position = position;
        this.scale = scale;
        this.object = object;
    }

    //create instance with only position offset
    public Instance(Traceable object, Vector3 position){
        this(object,position,new Vector3(1));
    }



    //trace a ray against object
    @Override
    public  HitData trace(Ray ray){
        //todo fix scaling, not occluding correctly
        //offset ray to offset object's position and scale without changing the object itself
        Ray offset_ray = new Ray(ray.getOrigin().subtract(position).divide(scale), ray.getDirection());
        return object.trace(offset_ray);
    }

    //pass through min and max, but offset and dilate
    @Override
    public Vector3 getMin() {
        return object.getMin().multiply(scale).add(position);
    }

    @Override
    public Vector3 getMax() {
        return object.getMax().multiply(scale).add(position);
    }

}
