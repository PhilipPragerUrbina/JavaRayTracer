package simulation.Ray.Tracables;

import simulation.Alg.Vector3;
import simulation.Ray.HitData;
import simulation.Ray.Ray;

public class Tri implements Traceable{
    private Vector3[] verts;
    private Vector3[] normals;

    public Tri(Vector3[] verts, Vector3[] normals){
        if(verts.length == 3){
            this.verts = verts;
            this.normals = normals;
        }
    }

    @Override
    public HitData trace(Ray ray) {
        double dist = -1;
        Vector3 uv = new Vector3();

        Vector3 v0v1 = verts[1].subtract(verts[0]);
        Vector3 v0v2 = verts[2].subtract( verts[0]);
        Vector3 pvec = ray.getDirection().cross(v0v2);
        double det = v0v1.dot(pvec);

        //floating point error range. Larger for larger objects to avoid speckling problem.
		final double epsilon = 0.000001f;
        if (Math.abs(det) < epsilon) return new HitData();

        double invdet = 1.0f / det;
        Vector3 tvec = ray.getOrigin().subtract(verts[0]);
        uv.x = tvec.dot(pvec) * invdet;
        if (uv.x < 0.0f || uv.x > 1.0f) return new HitData();

        Vector3 qvec = tvec.cross(v0v1);
        uv.y = ray.getDirection().dot(qvec) * invdet;
        if (uv.y < 0.0f || uv.x + uv.y > 1.0f) return new HitData();

        dist = v0v2.dot(qvec) * invdet;
		final double delta = 0.0001f;
        if (dist > delta) //check if in small range. this is to stop ray from intersecting with triangle again after bounce.
        {
            //todo check order of operations

            Vector3 normal = new Vector3(1.0f - uv.x - uv.y) .multiply( normals[0]).add( new Vector3(uv.x).multiply( normals[1])).add( new Vector3(uv.y).multiply( normals[2]));


            HitData hit = new HitData(new Vector3(0.7,0.7,0.7),normal,dist);
            return hit;
        }
        return new HitData();
    }


    @Override
    public Vector3 getMax() {
        Vector3 max = verts[0]; //start with first

        for (int i = 1; i < 3; i++) {
            max = max.max(verts[i]);
        }
        return max;
    }

    @Override
    public Vector3 getMin() {
        Vector3 min = verts[0]; //start with first

        for (int i = 1; i < 3; i++) {
            min = min.min(verts[i]);
        }
        return min;
    }
}
