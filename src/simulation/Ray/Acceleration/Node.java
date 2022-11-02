package simulation.Ray.Acceleration;

import simulation.Ray.HitData;
import simulation.Ray.Ray;
import simulation.Ray.Tracables.Traceable;

import java.util.ArrayList;
import java.util.Collections;

public class Node {
    private BoundingBox box;

    //binary tree
    private Node child_a;
    private Node child_b;

    private Traceable thing = null; //not null means leaf node

    //recursively create nodes
    public Node(ArrayList<Traceable> remaining_objects){
        //create bounding box
        box = new BoundingBox(remaining_objects);

        //check if leaf node
        if(remaining_objects.size() == 1){
            thing = remaining_objects.get(0);
            return;
        }

        //Assumes the objects are sorted spatially
        //split in half
        ArrayList<Traceable> list_a =new ArrayList<Traceable>(remaining_objects.subList(0,remaining_objects.size()/2));
        ArrayList<Traceable> list_b = new ArrayList<Traceable>(remaining_objects.subList(remaining_objects.size()/2,remaining_objects.size()));

        //create child nodes
        child_a = new Node(list_a);
        child_b = new Node(list_b);


    }

    //recursively check for hits
   public HitData hit(Ray r){
        //todo closest first
        if(box.doesHit(r)){
            if(thing != null){
                //leaf node
                return thing.trace(r);
            }
            //test children, return closest
            HitData a = child_a.hit(r);
            HitData b = child_b.hit(r);
            //todo optimize this monster
            if(a.didHit() && b.didHit()){
                if(a.getDistance() < b.getDistance()){
                    return a;
                }else {
                    return b;
                }
            } else if(a.didHit()){
                return a;
            }else if(b.didHit()){
                return b;
            }
        }
        return new HitData();//did not hit
    }


    BoundingBox getBox(){
        return box;
    }


}
