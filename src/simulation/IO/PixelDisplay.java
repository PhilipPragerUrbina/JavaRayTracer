package simulation.IO;

import simulation.Alg.Vector3;

//an interface for getting image data from the ray tracer
public interface PixelDisplay {
    //set and get data
    Vector3 getPixel(int x, int y);
    void setPixel(int x, int y, Vector3 rgb);

    //get the dimensions
    int getWidth();
    int getHeight();

    //save or display the data
    void update();

}
