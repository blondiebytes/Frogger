
package frogger;

import javalib.worldimages.FromFileImage;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

public class Car implements Collideable{
    public int xPos;
    public int yPos;
    public double size; // NEED TO SET 
    public String direction;
    // Create an identity for each thing
    
    // CONSTANTS:
    public static int increment = 1;
    static int XMAX = 450;
    static int YMAX = 450;
    static int XMIN = 0;
    static int YMIN = 0;
    static int total = 0;
   
    
    public Car(int xPos, int yPos, String direction) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = direction;
    }
   
    public int getXPos() {
        return xPos;
    }
    
    public int getYPos() {
        return yPos;
    }
    
    public double getSize() {
        return size; 
    }
    
    public String getDirection() {
        return direction;
    }
    
    public Collideable move() {
        if (this.direction.equals("RIGHTCAR")) {
            return new Car(xPos + increment, yPos, direction);
        } else {
            return new Car(xPos - increment, yPos, direction);
        }
    }
    
    public boolean isOffScreen() {
        return this.xPos >= XMAX;
    }
    
    public Frog refractorCollisionWithFrog(Frog frog) {
//            if (this.frog.isCollision(c)) {
//                // Make newFrog return to the last safe row y-pos, keeping same x-pos
//            }
//             c = c.moveCar();
    }
   
    // Could add functionality for colors? WOO
     public WorldImage draw() {
         if (this.direction.equals("RIGHTCAR")) {
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/rightCar");
         } else
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/leftCar");
        }

 
    
        
}
