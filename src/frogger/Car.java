
package frogger;

import static java.awt.Color.BLUE;
import static java.awt.Color.GRAY;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

public class Car implements Collideable<Car> {
    private int xPos;
    private int yPos;
    private double size = 75;
    private String direction; // "RIGHT" // "LEFT"
    // Create an identity for each thing
    final private int identity;
    private int increment;
    
    // CONSTANTS:
    static int XMAX = 500;
    static int YMAX = 450;
    static int XMIN = -50;
    static int YMIN = 0;
    static int total = 0;
   
    
    public Car(int xPos, int yPos, int increment, String direction) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.increment = increment;
        this.direction = direction;
        this.identity = total;
        total++;
    }
    
     public Car(int xPos, int yPos, String direction, int increment, int identity) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = direction;
        this.increment = increment;
        this.identity = identity;
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
    
    public int getIncrement() {
        return increment;
    }
    
    public int getIdentity() {
        return identity;
    }
    
    public Car move() {
        if (this.getDirection().equals("RIGHT")) {
            return new Car(getXPos() + getIncrement(), getYPos(), getDirection(), getIncrement(), getIdentity());
        } else {
            return new Car(getXPos() - getIncrement(), getYPos(), getDirection(), getIncrement(), getIdentity());
        }
    }
    
    public Car moreDifficultNextRound(int newIncrement) {
        return new Car(this.xPos, this.yPos, this.direction, newIncrement, this.identity);
    }
    
    public boolean isOffScreen() {
        return this.xPos >= XMAX;
    }
    
    public Frog refractorObstacleCollisionWithFrog(Frog frog, Row safeRow) {
        return  new Frog(frog.getXPos(), safeRow.getStartY(), frog.image, frog.decrementCurrentRow());
    }
    
    public Frog refractorAssisterCollisionWithFrog(Frog frog) {
        throw new RuntimeException("A Car isn't a assister");
    }
   
    // Could add functionality for colors? WOO
     public WorldImage draw() {
         if (this.getDirection().equals("RIGHT")) {
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/rightcar.png");
         } else {
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/leftcar.png");
         }
        }

 
    
        
}
