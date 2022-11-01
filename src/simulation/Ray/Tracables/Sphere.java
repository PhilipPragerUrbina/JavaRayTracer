package simulation.Ray.Tracables;

import simulation.Alg.Vector3;
import simulation.Ray.HitData;
import simulation.Ray.Ray;
// a simple sphere
public class Sphere implements Traceable{

    private Vector3 position;
    private double radius;
    private Vector3 color;

    public Sphere(Vector3 position, double radius, Vector3 color) {
        this.position = position;
        this.radius = radius;
        this.color = color;
    }



    @Override
    public HitData trace(Ray ray) {

        Vector3 oc = ray.getOrigin().subtract(position) ;
        double a = ray.getDirection().dot( ray.getDirection());
        double b = 2.0 * oc.dot(ray.getDirection());
        double c = oc.dot(oc) - radius*radius;

        double discriminant = b*b - 4*a*c;

        if(discriminant > 0){
            //hit, calculate surface normal is just direction of centre to hitpoint
            HitData data =  new HitData(color, null,   (-b -Math. sqrt(discriminant) ) / (2.0*a));
            data.setNormal(data.getHitPoint(ray).subtract(position));
            return data;
        }

        return new HitData();
    }
}
