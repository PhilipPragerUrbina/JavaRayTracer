package simulation.Ray.IO;


import simulation.Ray.Camera;
import simulation.Ray.Tracables.Group;

// a file with scene data in it
public interface GeometryFile {

    Group getScene();

    Camera getCamera();
}
