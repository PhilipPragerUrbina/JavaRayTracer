package simulation.Ray.Acceleration;

import simulation.Alg.Vector3;
import simulation.Ray.Ray;

public class BoundingBox {
    private Vector3 min;
    private Vector3 max;

    //create a bounding box from a min and a max
    public BoundingBox(Vector3 min, Vector3 max){
        this.min = min;
        this.max = max;
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

        if (tzmin > tzmax) {
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

    public Vector3 getMin() {
        return min;
    }

    public Vector3 getMax() {
        return max;
    }


}
