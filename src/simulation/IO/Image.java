package simulation.IO;

import simulation.Alg.Vector3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//buffered image wrapper
public class Image implements PixelDisplay {
    private BufferedImage image; //data
    String filepath;//location

    //create new image with width and height
    public Image(int w, int h, String filepath_jpg){
        image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB );
        filepath = filepath_jpg;
    }

    //set a pixel to a rgb color, does not set if out of bounds
    @Override
    public  void setPixel(int x, int y, Vector3 color){
        if(inBounds(x,y)) {
            image.setRGB(x,y, new Color((int)color.x,(int)color.y,(int)color.z).getRGB());
        }
    }

    @Override
    public Vector3 getPixel(int x, int y) {
        if (inBounds(x,y)) {
            Color color = new Color(image.getRGB(x,y)); //convert to vector3
            return new Vector3(color.getRed(),color.getBlue(), color.getGreen());
        }
        return new Vector3();
    }

    //is within image
    public boolean inBounds(int x, int y){
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }
@Override
    public  int getWidth(){
        return  image.getWidth();
    }
    @Override
    public  int getHeight(){
        return  image.getHeight();
    }

    @Override
    public void update() {
        try {
            saveImageJPG(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //save the image as a jpg
    public void saveImageJPG(String filename) throws IOException {
        File output = new File(filename);
        ImageIO.write(image, "jpg",output);;
    }



}
