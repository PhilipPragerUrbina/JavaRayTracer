package simulation.Ray;

import simulation.Alg.Vector3;
//a  camera that emits rays
public class Camera {
    //world
    private Vector3 position;
    private Vector3 direction;
    double fov;
    Vector3 up = new Vector3(0,1,0); //y will be up

    //Create a camera at a position facing a direction. Also set its width and height in the world.
    //currently, fov is just how mich bigger the camera should be in units, not degrees or radians
    public Camera(Vector3 position, Vector3 direction, double fov){
        this.direction = direction.normalized();
        this.position = position;
        this.fov = fov; //todo angle fov
    }

    //get a ray at a certain screen position(screen space to world space)
    //x and y should be between 0 and 1
    public Ray getRay(double x, double y){
        //take into account fov
        x *= fov;
        y*=fov;
        //offset x and y to center
        x -= 0.5 * fov;
        y -= 0.5 * fov;
        //get ray direction
        Vector3 ray_direction =direction;

        //rotate position
        Vector3 horizontal_rotated = up.cross(direction).normalized(); //get basis vectors for x and y after rotation
        Vector3 vertical_rotated = direction.cross(horizontal_rotated);
        Vector3 ray_position = horizontal_rotated .multiply(new Vector3(x)).add(vertical_rotated.multiply(new Vector3(y))); //rotated x and y, but at origin

        ray_position = ray_position.add(position); //offset to camera position


        //offset origin
        ray_position  =ray_position.add(position);
        return new Ray(ray_position,ray_direction.normalized().add(Vector3.randomVector().divide(new Vector3(5000)))); //simple camera for now
    }
}
