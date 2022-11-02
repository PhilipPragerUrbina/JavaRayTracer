package simulation.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//a file reader
public class ReadFile {
    private File file;


    public ReadFile(String filename)  {
        file = new File(filename);
        if(!file.exists()){
            System.err.println("Warning: File does not exist " + filename);
        }

    }

    public ArrayList<String> getLines(){
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lines;

    }



}
