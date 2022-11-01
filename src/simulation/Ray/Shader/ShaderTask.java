package simulation.Ray.Shader;

import simulation.Alg.Vector3;
import simulation.IO.PixelDisplay;
import simulation.Ray.Camera;
import simulation.Ray.HitData;
import simulation.Ray.Ray;
import simulation.Ray.Tracables.TraceableWorld;

//actual ray tracing shader, render in certain x and y values
public class ShaderTask implements Runnable {
    final static int MAX_DEPTH = 5; //max ray bounces
    final static int NUM_SAMPLES = 3; //max ray bounces

    private Camera camera;
    private Cell region;
    private TraceableWorld world;



    public ShaderTask( TraceableWorld world, Camera camera, Cell region ){
        this.camera = camera;
        this.world = world;
        this.region = region;
    }

    @Override
    public void run() {
        for (int x = region.getMinX(); x < region.getMaxX(); x++) {
            for (int y = region.getMinY(); y < region.getMaxY(); y++) {
                //image shader
                double w_x = ((double)x / (double)region.getWidth()); //convert the pixel values to double between 0 and 1
                double w_y = ((double)y / (double)region.getHeight());

                Vector3 luminance = new Vector3();
                for (int i = 0; i < NUM_SAMPLES; i++) {
                    Ray r = camera.getRay(w_x, w_y); //get camera ray
                    luminance  = luminance.add(trace(world,r, 0));  //trace the ray
                }
                region.setColor(x,y,luminance.divide(new Vector3(NUM_SAMPLES)).abs().multiply(new Vector3(255)));//convert luminance to 255 color and set in region
            }
        }
    }

    //trace a ray and return a color
    private static Vector3 trace(TraceableWorld world, Ray r, int depth) {
        depth++;
        HitData data = world.trace(r);

        if(data.didHit()){
            Ray new_ray = new Ray(data.getHitPoint(r), r.getDirection().reflect(data.getSurface_normal()));

            if(depth > MAX_DEPTH){
                return data.getLuminance();
            }
            return   data.getLuminance().multiply(trace(world,new_ray, depth));
        }
        if(r.getDirection().y < -0.3){
            return new Vector3(0.9); //light
        }
        return new Vector3(0.3,0.3,0.3); //return background
    }
}
