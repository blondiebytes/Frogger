
package frogger;

import javalib.worldimages.FromFileImage;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

public class Car implements Obstacle{
    public int xPos;
    public int yPos;
    public double size; // NEED TO SET 
    public String direction;
    
    // CONSTANTS:
    public static int increment = 1;
    static int XMAX = 450;
    static int YMAX = 450;
    static int XMIN = 0;
    static int YMIN = 0;
    
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
    
    public Car moveCar() {
        if (this.direction.equals("RIGHTCAR")) {
            return new Car(xPos + increment, yPos, direction);
        } else {
            return new Car(xPos - increment, yPos, direction);
        }
    }
    
    public boolean isOffScreen() {
        return this.xPos >= XMAX;
    }
   
    // Could add functionality for colors? WOO
     public WorldImage drawCar() {
         if (this.direction.equals("RIGHTCAR")) {
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/rightCar");
         } else
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/leftCar");
        }
    
        
}
