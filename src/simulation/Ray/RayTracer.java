package simulation.Ray;

import simulation.Alg.Vector3;
import simulation.IO.Image;
import simulation.Ray.Tracables.Sphere;
import simulation.Ray.Tracables.Traceable;
import simulation.Ray.Tracables.TraceableWorld;

import java.io.IOException;

public class RayTracer {
    final static int MAX_DEPTH = 10;
    final static  int RESOLUTION = 1000;
    public static void main(String[] args) {

        Image output = new Image(RESOLUTION,RESOLUTION);    //image to output to
        TraceableWorld world = getWorld(); //world to trace into
        Camera camera = new Camera(new Vector3(0,0,-30), new Vector3(0,0,1),5);        //camera to use
        for (int x = 0; x < output.getWidth(); x++) {
            //output done percentage
            System.out.println(" " + (int)((double)x / output.getWidth() * 100) + "% done");

            for (int y = 0; y < output.getHeight(); y++) {
                //image shader
                double w_x = ((double)x / (double)output.getWidth()); //convert the pixel values to double between 0 and 1
                double w_y = ((double)y / (double)output.getHeight());
                Ray r = camera.getRay(w_x, w_y); //get camera ray
                Vector3 luminance = trace(world,r, 0);  //trace the ray
                output.setPixel(x, y,luminance.abs().multiply(new Vector3(255))); //convert luminance to 255 color and set in image
            }
        }
        System.out.println(" 100% done");
        //save image
        try {
            output.saveImageJPG("output.jpg");
            System.out.println("Saved Image");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //set up the world
    private static TraceableWorld getWorld(){
        TraceableWorld world = new  TraceableWorld();
        world.addObject(new Sphere(new Vector3(0), 1.0, new Vector3(0.7,0.3,0.3)));
        world.addObject(new Sphere(new Vector3(0,2,0), 0.5, new Vector3(0.5)));
        world.addObject(new Sphere(new Vector3(3,0,0), 1.2, new Vector3(0.3,0.3,0.7)));
        return world;
    }

    //trace a ray and return a color
    private static Vector3 trace(TraceableWorld world, Ray r, int depth) {
        depth++;
        HitData data = world.trace(r);
        if(depth > MAX_DEPTH){
            return data.getLuminance();
        }
        if(data.didHit()){
            Ray new_ray = new Ray(data.getHitPoint(r), r.getDirection().reflect(data.getSurface_normal()));
            return   data.getLuminance().multiply(trace(world,new_ray, depth));
        }
        if(r.getDirection().y > 0.5){
            return new Vector3(0.9); //light
        }
        return new Vector3(0.3,0.3,0.3); //return background
    }


}
