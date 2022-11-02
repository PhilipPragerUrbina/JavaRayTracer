package simulation.Ray.Acceleration;

import simulation.Alg.Vector3;
import simulation.Ray.HitData;
import simulation.Ray.Ray;
import simulation.Ray.Tracables.Traceable;

import java.util.ArrayList;

public class BoundingBox {
    private Vector3 min;
    private Vector3 max;

    //create a bounding box from a min and a max
    public BoundingBox(Vector3 min, Vector3 max){
        this.min = min;
        this.max = max;
    }

    //get a bounding box from a list of objects
    public BoundingBox(ArrayList<Traceable> objects){
        //initialize with first object
        min = objects.get(0).getMin();
        max = objects.get(0).getMax();

        for (Traceable t : objects) {
            //expand bounds
            min = min.min(t.getMin());
            max= max.max(t.getMax());
        }
    }

    //ray intersection
    public boolean doesHit(Ray r){
        double tmin = (min.x - r.getOrigin().x) / r.getDirection().x;
        double tmax = (max.x - r.getOrigin().x) / r.getDirection().x;

        if (tmin > tmax) {
            double temporary = tmin;
            tmin = tmax;
            tmax = temporary;
        };

        double tymin = (min.y - r.getOrigin().y) / r.getDirection().y;
        double tymax = (max.y - r.getOrigin().y) / r.getDirection().y;

        if (tymin > tymax) {
            double temporary = tymin;
            tymin = tymax;
            tymax = temporary;
        }

        if ((tmin > tymax) || (tymin > tmax))
            return false;

        if (tymin > tmin)
            tmin = tymin;

        if (tymax < tmax)
            tmax = tymax;

        double tzmin = (min.z - r.getOrigin().z) / r.getDirection().z;
        double tzmax = (max.z - r.getOrigin().z) / r.getDirection().z;

        if (tzmin >tzmax) {
            double temporary = tzmin;
            tzmin = tzmax;
            tzmax = temporary;
        }

        if ((tmin > tzmax) || (tzmin > tmax))
            return false;

        if (tzmin > tmin)
            tmin = tzmin;

        if (tzmax < tmax)
            tmax = tzmax;

        return true;
    }

    //ray intersection with distance
    public double hit(Ray r){
        Vector3 tMin = (min.subtract(r.getOrigin())).divide( r.getDirection());
        Vector3 tMax = (max.subtract(r.getOrigin())).divide( r.getDirection());
        Vector3 t1 = tMin.min( tMax);
        Vector3 t2 = tMin.max( tMax);
        double tNear = Math.max(Math.max(t1.x, t1.y), t1.z);
        double tFar = Math.min(Math.min(t2.x, t2.y), t2.z);
        return tFar > Math.max(tNear, 0.0) ? tNear : -1;
    }



    public Vector3 getMin() {
        return min;
    }

    public Vector3 getMax() {
        return max;
    }




}
