package simulation.Ray;

import simulation.Alg.Vector3;
import simulation.IO.Image;
import simulation.IO.PixelDisplay;
import simulation.Ray.IO.OBJFile;
import simulation.Ray.Shader.Cell;
import simulation.Ray.Shader.ShaderTask;
import simulation.Ray.Tracables.Instance;
import simulation.Ray.Tracables.Sphere;
import simulation.Ray.Tracables.Group;
import simulation.Ray.Tracables.Traceable;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RayTracer {

    final static  int RESOLUTION = 1000; //number of pixels for width of image
    final static int NUM_CELLS = 10; //number of regions to split the image into
    final static int NUM_THREADS = 8; //how many threads to process those regions

    public static void main(String[] args) {
        long start = System.nanoTime(); //time the execution
 //todo lightmap

        PixelDisplay output = new Image(RESOLUTION,RESOLUTION,"render.jpg");    //image to output to
        System.out.println("Initialized display adapter");
        Group world = getWorld(); //world to trace into
        System.out.println("Generated World");
        world.buildBVH(); //build acceleration structure
        System.out.println("Built acceleration structure");
        Camera camera = new Camera(new Vector3(40,8,-20), new Vector3(0,0,1),10);        //camera to use
        camera.setLookAt(new Vector3(0,2,0));
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
            pool.awaitTermination(100000000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished render");

        //save region data
        for (Cell cell : cells) {
            cell.saveToDisplay();;
        }
        System.out.println("Copied Data");

        long spent = System.nanoTime() - start;;
        System.out.println("Render finished in: " + TimeUnit.NANOSECONDS.toMillis(spent) + " ms");

        //save image
      output.update();
            System.out.println("Saved Image");

    }

    //set up the world
    private static Group getWorld(){
        /*
       ArrayList<Traceable> objects = new ArrayList<>();
        objects.add(new Sphere(new Vector3(0), 1.0, new Vector3(0.7,0.3,0.3)));
        objects.add(new Sphere(new Vector3(0,2,0), 0.5, new Vector3(0.5)));
        objects.add(new Sphere(new Vector3(3,0,0), 1.2, new Vector3(0.3,0.3,0.7)));

        Random random = new Random(200); //fixed seed for testing
        //random spheres
        for (int i = 0; i < 10000; i++) {
            Vector3 position = Vector3.randomVector(-200,200, random);
            Vector3 color = Vector3.randomVector(0.5,0.9, random);
            double radius = (random.nextDouble() * 3) + 1;
            objects.add(new Sphere(position,radius,color));

        }*/

        ArrayList<Traceable> world = new ArrayList<>();
        world.add(new Sphere(new Vector3(0,4,0), 1.0, new Vector3(0.7,0.3,0.3)));
        world.add(new Sphere(new Vector3(5,2,0), 0.5, new Vector3(0.5)));
        world.add(new Sphere(new Vector3(5,0,0), 1.2, new Vector3(0.3,0.3,0.7)));



        OBJFile file = new OBJFile("test.obj",1.0, true);
        Traceable obj = file.getScene();;
        world.add(obj);

        Random random = new Random(200); //fixed seed for testing

    //    for (int i = 0; i < 100; i++) {
     //       Vector3 position = Vector3.randomVector(-10,10, random).add(new Vector3(0,0,10));
       //     world.add(new Instance(obj, position));

     //   }

       // world.add(new Instance(obj, new Vector3(-5,0,0)));
      //  world.add(new Instance(obj, new Vector3(-5,4,0)));
      //  world.add(new Instance(obj, new Vector3(0,0,0)));

        world.add(obj);

        return new Group(world);
    }




}
