package simulation.IO;

import simulation.Alg.Vector3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//buffered image wrapper
public class Image {
    private BufferedImage image; //data

    //create new image with width and height
    public Image(int w, int h){
        image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB );
    }

    //set a pixel to a rgb color, does not set if out of bounds
    public  void setPixel(int x, int y, Vector3 color){
        if(inBounds(x,y)) {
            image.setRGB(x,y, new Color((int)color.x,(int)color.y,(int)color.z).getRGB());
        }
    }

    //is within image
    public boolean inBounds(int x, int y){
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    public  int getWidth(){
        return  image.getWidth();
    }
    public  int getHeight(){
        return  image.getHeight();
    }

    //save the image as a jpg
    public void saveImageJPG(String filename) throws IOException {
        File output = new File(filename);
        ImageIO.write(image, "jpg",output);;
    }

}
