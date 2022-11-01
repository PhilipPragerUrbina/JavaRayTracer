package simulation.Ray.Shader;

import simulation.Alg.Vector3;
import simulation.IO.PixelDisplay;

//represents a portion of the screen to render
public class Cell{
    private int min_x;
    private int min_y;
    private int max_x;
    private int max_y;
    private PixelDisplay screen;

    public int getWidth() {
        return screen.getWidth();
    }

    public int getHeight() {
        return screen.getHeight();
    }



    //actual pixel data
    private Vector3[][] data;

    //create a representation of a portion of a screen. Automatically split up screen vertically, based on a number of regions
    public Cell(int index, int total_regions,PixelDisplay screen) {
        //divide screen into vertical bars
        int stride = screen.getWidth()/total_regions;
        this.min_x = index * stride;
        this.min_y = 0;
        this.max_x = index * stride + stride ;
        this.max_y = screen.getHeight();
        this.data = new Vector3[getXRange()][getYRange()];
        this.screen = screen;

    }

    //create a representation of a portion of a screen
    public Cell(int min_x, int min_y, int max_x, int max_y,PixelDisplay screen) {
        this.min_x = min_x;
        this.min_y = min_y;
        this.max_x = max_x;
        this.max_y = max_y;
        this.data = new Vector3[getXRange()][getYRange()];
        this.screen = screen;

    }

    //set pixel values(coordinates are relative whole screen, not to cell)
    public void setColor(int x, int y, Vector3 color){
        data[x - min_x][y - min_y] = color;
    }

    //copy data to actual screen
    public void saveToDisplay(){
        for (int x = min_x; x < max_x; x++) {
            for (int y = min_y; y < max_y; y++) {
                screen.setPixel(x,y,data[x-min_x][y-min_y]);
            }
        }
    }


    public int getXRange(){
        return max_x - min_x;
    }

    public int getYRange(){
        return max_y - min_y;
    }

    public int getMinX() {
        return min_x;
    }

    public int getMinY() {
        return min_y;
    }

    public int getMaxX() {
        return max_x;
    }

    public int getMaxY() {
        return max_y;
    }


}