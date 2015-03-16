
package frogger;

import javalib.worldimages.FromFileImage;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;


class Lily implements Collideable{
    public int xPos;
    public int yPos;
    public double size; // NEED TO SET 
    public String direction;
    
    // CONSTANTS:
    static int XMAX = 450;
    static int YMAX = 450;
    static int XMIN = 0;
    static int YMIN = 0;
    
    public int increment = 1;
    
    public Lily(int xPos, int yPos, String direction) {
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
    
    public int getIncrement() {
        return increment;
    }
    
    public Collideable move() {
        if (this.direction.equals("RIGHTLILY")) {
            return new Lily(this.getXPos() + this.getIncrement(), this.getYPos(), this.getDirection());
        } else {
            return new Lily(this.getXPos() - this.getIncrement(), this.getYPos(), this.getDirection());
        }
    }
    
     public boolean isOffScreen() {
        return this.xPos >= XMAX;
    }
     
     public Frog refractorCollisionWithFrog(Frog frog) {
            Frog newFrog = frog;
            // Is the frog on a lily?
            if (frog.isCollision(this)) {
            // If so, make it so. Set direction so we know that the frog is on a lily
              // THen tick the frog so it moves at the same rate as the lily
              newFrog = new Frog(frog.xPos, this.yPos, frog.image, this.getDirection()).tickMoveFroggy(this);
            }
            return newFrog;
     
     }
   
    // Could add functionality for colors? WOO
     public WorldImage draw() {
         if (this.direction.equals("RIGHTLILY")) {
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/rightLily");
         } else
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/leftLily");
        }
    
    
        
}
