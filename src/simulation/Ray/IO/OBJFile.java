package simulation.Ray.IO;

import simulation.Alg.Vector3;
import simulation.IO.ReadFile;
import simulation.Ray.Camera;
import simulation.Ray.Tracables.Group;
import simulation.Ray.Tracables.Traceable;
import simulation.Ray.Tracables.Tri;

import java.util.ArrayList;

public class OBJFile implements GeometryFile{

    ArrayList<Vector3> vertexes = new ArrayList<>();
    ArrayList<Vector3> normals = new ArrayList<>();
    ArrayList<Integer> faces = new ArrayList<>();
    ArrayList<Integer> normal_faces = new ArrayList<>();

   public OBJFile(String filename, double scale, boolean flip_normals){
       double flip = 1;
       if(flip_normals){
           flip = -1;
       }

       ReadFile f = new ReadFile(filename);
       ArrayList<String> lines = f.getLines();
       for (String line: lines) {
            if(line.isEmpty()){
                continue;
            }

             if(line.charAt(0) == 'v' && line.charAt(1 ) == 'n'){ //vertex normal
                String[] split = line.split("\\s+");
                if(split.length == 4){//correct amount of values
                    //ignore first since it is v
                    Vector3 v = new Vector3(Double.parseDouble(split[1])*flip, Double.parseDouble(split[2])*flip, Double.parseDouble(split[3])*flip); //parse position
                    normals.add(v);
                }


            }
             else if(line.charAt(0) == 'v'){ //vertex
                String[] split = line.split("\\s+");
                if(split.length == 4){//correct amount of values
                    //ignore first since it is v
                    Vector3 v = new Vector3(Double.parseDouble(split[1])*scale, Double.parseDouble(split[2])*scale, Double.parseDouble(split[3])*scale); //parse position
                    vertexes.add(v);
                }


            }

            else if(line.charAt(0) == 'f'){ //fase
                String[] split = line.split("\\s+|/");
                if(split.length == 4) {//triangle not quad
                    //ignore first split since it is f
                    //parse and add indices
                    faces.add(Integer.parseInt(split[1])-1);
                    faces.add(Integer.parseInt(split[2])-1);
                    faces.add(Integer.parseInt(split[3])-1);

                    normal_faces.add(Integer.parseInt(split[1])-1);
                    normal_faces.add(Integer.parseInt(split[2])-1);
                    normal_faces.add(Integer.parseInt(split[3])-1);

                }
                else if(split.length == 10){//separate normal indices
                    faces.add(Integer.parseInt(split[1])-1);
                    faces.add(Integer.parseInt(split[4])-1);
                    faces.add(Integer.parseInt(split[7])-1);
                    //f index/tex/normal index/tex/normal  index/tex/normal
                    normal_faces.add(Integer.parseInt(split[3])-1);
                    normal_faces.add(Integer.parseInt(split[6])-1);
                    normal_faces.add(Integer.parseInt(split[9])-1);


                }


            }


       }
   }

    @Override
    public Group getScene() {
       ArrayList<Traceable> objects = new ArrayList<>();
       boolean gen_normals = normals.size() != vertexes.size() || normal_faces.size() != faces.size(); //check if vertex normals are given

       //construct triangles out of faces and vertices
        for (int i = 0; i < faces.size(); i+=3) {
            Vector3[] verts = new Vector3[3];
            Vector3[] norms = new Vector3[3];

            //auto generate flat normals
            //get vertices
            Vector3 vert_a = vertexes.get(faces.get(i+0));
            Vector3 vert_b = vertexes.get(faces.get(i+1));
            Vector3 vert_c = vertexes.get(faces.get(i+2));
            //get sides
            Vector3 side_a = vert_a.subtract(vert_b);
            Vector3 side_b = vert_c.subtract(vert_b);
            //take cross product of two sides
            Vector3 auto_normal = side_a.cross( side_b).normalized();

            for (int j = 0; j < 3; j++) {


                verts[j] =  vertexes.get(faces.get(i+j));
                if(gen_normals){
                    norms[j] = auto_normal; //use flat normals
                }else{
                    norms[j] =  normals.get(normal_faces.get(i+j)); //get vertex normals
                }


            }
            objects.add(new Tri(verts,norms));

        }


        return new Group(objects);
    }

    @Override
    public Camera getCamera() {
        return null;
    }
}
