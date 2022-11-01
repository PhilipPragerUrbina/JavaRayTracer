package simulation.Alg;

import java.util.Random;

//3d vector class
public class Vector3{
    //values
    public double x;
    public double y;
    public double z;

    //constructors
    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //from scalar
    public Vector3(double scalar){
        this.x = scalar;
        this.y = scalar;
        this.z = scalar;
    }
    //null vector
    public Vector3(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    //operators
    public Vector3 add(Vector3 b){
        return new Vector3(this.x+b.x, this.y+b.y, this.z+b.z);
    }
    public Vector3 subtract(Vector3 b){
        return new Vector3(this.x-b.x, this.y-b.y, this.z-b.z);
    }
    public Vector3 multiply(Vector3 b){
        return new Vector3(this.x*b.x, this.y*b.y, this.z*b.z);
    }
    public Vector3 divide(Vector3 b){
        return new Vector3(this.x/b.x, this.y/b.y, this.z/b.z);
    }

    //comparison
    public boolean equalsExact(Vector3 b){
        return this.x == b.x && this.y == b.y && this.z == b.z;
    }
    //use delta for floating point comparison
    public boolean equals(Vector3 b, double delta){
        return deltaDouble(this.x , b.x, delta) && deltaDouble(this.y , b.y, delta) && deltaDouble(this.z , b.z, delta);
    }

    //utilities
    //compare two floating-point numbers based on epsilon
    private static boolean deltaDouble(double a, double b, double d){
        return Math.abs(a-b) < d;
    }

    public double distance(Vector3 b){
        return Math.sqrt(Math.pow((this.x-b.x),2)+Math.pow((this.y-b.y),2)+Math.pow((this.z-b.z),2));
    }

    public Vector3 abs(){
        return new Vector3(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    //reflect a vector by a normal
    public Vector3 reflect(Vector3 n){
        return this.subtract(new Vector3( 2*dot(n)).multiply(n));
    }
    Vector3 randomInUnitSphere() {
        while (true) {
            Vector3 random_vec = randomVector().multiply(new Vector3(0.0));//get random vector in between -1 and 1
            if (Math.pow(random_vec.length(),2) >= 1) continue; //outside of sphere, try again
            return random_vec;
        }
    }

    //get  right-hand rule perpendicular vector
    public Vector3 cross(Vector3 other){
        return new Vector3(this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x);
    }

    public double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    public Vector3 normalized(){
        return this.divide(new Vector3(this.length()));
    }

    //random components between -0.5 and 0.5
    public static Vector3 randomVector(){
        return new Vector3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
    }

    //random components between min and max
    //min inclusive, max exclusive
    public static Vector3 randomVector(double min, double max){
        double diff = max - min;
        return new Vector3((Math.random() * diff)+min, (Math.random() * diff)+min, (Math.random() * diff)+min);
    }

    //get random normalized direction
    public static Vector3 randomDirection(){
        return randomVector().normalized();
    }

    public double dot(Vector3 b){
        return this.x * b.x + this.y * b.y + this.z * b.z;
    }

    //move in direction by distance
    Vector3 move(Vector3 dir, double dist){
        return this.add(dir.multiply(new Vector3(dist)));
    }

//get the smallest and largest components of two vectors
    Vector3 min(Vector3 other){
        return new Vector3(Math.min(x,other.x), Math.min(y,other.y),Math.min(z,other.z));
    }
    Vector3 max(Vector3 other){
        return new Vector3(Math.max(x,other.x), Math.max(y,other.y),Math.max(z,other.z));
    }


    @Override
    public String toString() {
        return "(" + "," + x + "," + y + "," + z + ')';
    }
}

