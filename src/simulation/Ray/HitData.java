package simulation.Ray;

import simulation.Alg.Vector3;

public class HitData {
    private Vector3 luminance; //color
    private Vector3 surface_normal;
    private double distance; //hit distance

    //invalid HitData
    public HitData(){
        distance = -1;
    }

    public HitData(Vector3 luminance, Vector3 surface_normal, double distance) {
        this.luminance = luminance;
        this.surface_normal = surface_normal;
        this.distance = distance;
    }

    public boolean didHit(){
        return  distance >0.0;
    }

    public Vector3 getLuminance() {
        return luminance;
    }

    public Vector3 getSurface_normal() {
        return surface_normal;
    }

    public double getDistance() {
        return distance;
    }

    ///normalize and set the normal
    public void setNormal(Vector3 n){
        surface_normal = n.normalized();
    }

    //get where the position of where the hit, ... well hit, based on the original ray
    public Vector3 getHitPoint(Ray original_ray){
        return (original_ray.getDirection().multiply(new Vector3(distance))).add(original_ray.getOrigin());
    }
}
