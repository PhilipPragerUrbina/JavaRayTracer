package simulation.Ray;

import simulation.Alg.Vector3;
import simulation.IO.Image;
import simulation.IO.PixelDisplay;
import simulation.Ray.Shader.Cell;
import simulation.Ray.Shader.ShaderTask;
import simulation.Ray.Tracables.Sphere;
import simulation.Ray.Tracables.TraceableWorld;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RayTracer {

    final static  int RESOLUTION = 1000; //number of pixels for width of image
    final static int NUM_CELLS = 10; //number of regions to split the image into
    final static int NUM_THREADS = 8; //how many threads to process those regions

    public static void main(String[] args) {

        PixelDisplay output = new Image(RESOLUTION,RESOLUTION,"render.jpg");    //image to output to
        System.out.println("Initialized display adapter");
        TraceableWorld world = getWorld(); //world to trace into
        System.out.println("Generated World");
        world.buildBVH(); //build acceleration structure
        System.out.println("Built acceleration structure");
        Camera camera = new Camera(new Vector3(0,-200,-600), new Vector3(0,0,1),600);        //camera to use
        camera.setLookAt(new Vector3(0));
        System.out.println("Created camera");

        //get regions
        ArrayList<Cell> cells = new ArrayList<>();
        for (int i = 0; i < NUM_CELLS; i++) {
            cells.add(new Cell(i,NUM_CELLS,output));
        }
        System.out.println("Split cells");
        //execute
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        //add tasks
        for (Cell cell : cells) {
            pool.execute(new ShaderTask(world,camera,cell));
        }

        pool.shutdown(); //make the pool finish up
        //sync the pool
        try {
            pool.awaitTermination(100000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished render");

        //save region data
        for (Cell cell : cells) {
            cell.saveToDisplay();;
        }
        System.out.println("Copied Data");

        //save image
      output.update();
            System.out.println("Saved Image");

    }

    //set up the world
    private static TraceableWorld getWorld(){
        TraceableWorld world = new  TraceableWorld();
        world.addObject(new Sphere(new Vector3(0), 1.0, new Vector3(0.7,0.3,0.3)));
        world.addObject(new Sphere(new Vector3(0,2,0), 0.5, new Vector3(0.5)));
        world.addObject(new Sphere(new Vector3(3,0,0), 1.2, new Vector3(0.3,0.3,0.7)));

        //random spheres
        for (int i = 0; i < 10000; i++) {
            Vector3 position = Vector3.randomVector(-200,200);
            Vector3 color = Vector3.randomVector(0.5,0.9);
            double radius = (Math.random() * 3) + 1;
            world.addObject(new Sphere(position,radius,color));

        }

        return world;
    }




}
