package simulation.Ray;

import simulation.Alg.Vector3;

//a ray, yay

public class Ray {
    private Vector3 origin;

    private Vector3 direction;

    //create a new ray with an origin, and a direction(make sure it's normalized)
    public Ray(Vector3 origin, Vector3 direction){
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public Vector3 getDirection() {
        return direction;
    }
}
