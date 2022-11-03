package simulation.Ray.IO;

import simulation.Alg.Vector3;
import simulation.IO.ReadFile;
import simulation.Ray.Camera;
import simulation.Ray.Tracables.Group;
import simulation.Ray.Tracables.Traceable;
import simulation.Ray.Tracables.Tri;

import java.util.ArrayList;
//todo clean up and make more compatible and add texture coordinates

//parse obj files
public class OBJFile implements GeometryFile{

    //data
    ArrayList<Vector3> vertexes = new ArrayList<>();
    ArrayList<Vector3> normals = new ArrayList<>();
    ArrayList<Vector3> texture_coordinates = new ArrayList<>();

    //indices of data(faces)
    ArrayList<Integer> vert_indexes = new ArrayList<>();
    ArrayList<Integer> norm_indexes = new ArrayList<>();
    ArrayList<Integer> tex_indexes = new ArrayList<>();

    //parse the file itself. Specify to scale and flip the normals. Blender requires to be flipped normals normally.
    //only supports triangles
   public OBJFile(String filename, double scale, boolean flip_normals){
       //flip normals
       double flip = 1;
       if(flip_normals){flip = -1;}

        //open file
       ReadFile f = new ReadFile(filename);
       ArrayList<String> lines = f.getLines();

       //parse line by line
       for (String line: lines) {
            String[] split = line.split("\\s+|/+"); //split by whitespace or slash
            if(split.length < 4){continue;} //empty or invalid line

           //check the type
            if(split[0].equals("v")){ //vertex
                    Vector3 v = new Vector3(Double.parseDouble(split[1])*scale, Double.parseDouble(split[2])*scale, Double.parseDouble(split[3])*scale); //parse position
                    vertexes.add(v);
            }else if(split[0].equals("vn")){ //vertex normal
                Vector3 v = new Vector3(Double.parseDouble(split[1])*scale, Double.parseDouble(split[2])*scale, Double.parseDouble(split[3])*scale); //parse position
                normals.add(v);
            }else if(split[0].equals("vt")){ //texture coordinate
                Vector3 v = new Vector3(Double.parseDouble(split[1])*scale, Double.parseDouble(split[2])*scale, 0); //parse position
                texture_coordinates.add(v);
            }else if(split[0].equals("f")){ //face
                if(split.length == 4) {//triangle not quad
                    //parse and add indices, all the same
                    vert_indexes.add(Integer.parseInt(split[1])-1);
                    vert_indexes.add(Integer.parseInt(split[2])-1);
                    vert_indexes.add(Integer.parseInt(split[3])-1);

                    tex_indexes.add(Integer.parseInt(split[1])-1);
                    tex_indexes.add(Integer.parseInt(split[2])-1);
                    tex_indexes.add(Integer.parseInt(split[3])-1);

                    norm_indexes.add(Integer.parseInt(split[1])-1);
                    norm_indexes.add(Integer.parseInt(split[2])-1);
                    norm_indexes.add(Integer.parseInt(split[3])-1);

                }
                else if(split.length == 10) {//separate normal and texture coordinate indices
                    //f index/tex/normal index/tex/normal  index/tex/normal
                    vert_indexes.add(Integer.parseInt(split[1]) - 1);
                    vert_indexes.add(Integer.parseInt(split[4]) - 1);
                    vert_indexes.add(Integer.parseInt(split[7]) - 1);

                    tex_indexes.add(Integer.parseInt(split[2]) - 1);
                    tex_indexes.add(Integer.parseInt(split[5]) - 1);
                    tex_indexes.add(Integer.parseInt(split[8]) - 1);

                    norm_indexes.add(Integer.parseInt(split[3]) - 1);
                    norm_indexes.add(Integer.parseInt(split[6]) - 1);
                    norm_indexes.add(Integer.parseInt(split[9]) - 1);
                } else if(split.length == 7) { //only special indices for either normal OR texture coords
                    vert_indexes.add(Integer.parseInt(split[1]) - 1);
                    vert_indexes.add(Integer.parseInt(split[3]) - 1);
                    vert_indexes.add(Integer.parseInt(split[5]) - 1);
                    if (line.contains("//")) { //normal specified
                        //f index//norm index//norm  index//norm
                        norm_indexes.add(Integer.parseInt(split[2]) - 1);
                        norm_indexes.add(Integer.parseInt(split[4]) - 1);
                        norm_indexes.add(Integer.parseInt(split[6]) - 1);

                        tex_indexes.add(Integer.parseInt(split[1]) - 1);
                        tex_indexes.add(Integer.parseInt(split[2]) - 1);
                        tex_indexes.add(Integer.parseInt(split[3]) - 1);

                    } else { //texture specified
                        //f index/tex index/tex  index/tex
                        tex_indexes.add(Integer.parseInt(split[2]) - 1);
                        tex_indexes.add(Integer.parseInt(split[4]) - 1);
                        tex_indexes.add(Integer.parseInt(split[6]) - 1);

                        norm_indexes.add(Integer.parseInt(split[1]) - 1);
                        norm_indexes.add(Integer.parseInt(split[2]) - 1);
                        norm_indexes.add(Integer.parseInt(split[3]) - 1);
                    }


                }

            }

       }
   }


   //convert data to traceable
    @Override
    public Group getScene() {
       ArrayList<Traceable> objects = new ArrayList<>();

       //check if it contains vertex normals and texture coordinates
       boolean gen_normals = normals.size() == 0; //check if vertex normals are given
        boolean gen_tex_coords = texture_coordinates.size()==0; //check if vertex normals are given

       //construct triangles out of faces and vertices
        for (int i = 0; i < vert_indexes.size(); i+=3) {
            //gather data
            Vector3[] verts = new Vector3[3];
            Vector3[] norms = new Vector3[3];
            Vector3[] tex = new Vector3[3];

            Vector3 auto_normal = new Vector3();
            if(gen_normals){
                //auto generate flat normals
                //get vertices
                Vector3 vert_a = vertexes.get(vert_indexes.get(i+0));
                Vector3 vert_b = vertexes.get(vert_indexes.get(i+1));
                Vector3 vert_c = vertexes.get(vert_indexes.get(i+2));
                //get sides
                Vector3 side_a = vert_a.subtract(vert_b);
                Vector3 side_b = vert_c.subtract(vert_b);
                //take cross product of two sides
                auto_normal = side_a.cross( side_b).normalized();
            }
            //auto generate texture coordinates
            Vector3 auto_coord = vertexes.get(vert_indexes.get(i+0)).normalized();

            //set values
            for (int j = 0; j < 3; j++) {
                verts[j] =  vertexes.get(vert_indexes.get(i+j)); //set vertex
                //set normal
                if(gen_normals){
                    norms[j] = auto_normal; //use flat normals
                }else{
                    norms[j] =  normals.get(norm_indexes.get(i+j)); //get vertex normals
                }
                //set text coord
                if(gen_tex_coords){
                    tex[j] = auto_coord; //use flat normals
                }else{
                    tex[j] =  texture_coordinates.get(tex_indexes.get(i+j)); //get vertex normals
                }
            }
            objects.add(new Tri(verts,norms));//add triangle

        }
        return new Group(objects); //build bvh and return group
    }

    @Override
    public Camera getCamera() {
        return null;
    }
}
