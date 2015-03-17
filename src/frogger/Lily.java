
package frogger;

import javalib.worldimages.FromFileImage;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;


class Lily implements Collideable<Lily>{
    private int xPos;
    private int yPos;
    private double size; // NEED TO SET 
    private String direction;
    private int identity;
    
    // Need to add this part in constructor
    public int increment = 1;
    
    // CONSTANTS:
    static int XMAX = 450;
    static int YMAX = 450;
    static int XMIN = 0;
    static int YMIN = 0;
    static int total = 0;
    
    
    public Lily(int xPos, int yPos, String direction) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = direction;
        this.identity = total;
        total++;
               
    }
   
     public Lily(int xPos, int yPos, String direction, int identity) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = direction;
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
    
    public Lily move() {
        if (this.getDirection().equals("RIGHTLILY")) {
            return new Lily(this.getXPos() + this.getIncrement(), this.getYPos(), this.getDirection(), this.getIdentity());
        } else {
            return new Lily(this.getXPos() - this.getIncrement(), this.getYPos(), this.getDirection(), this.getIdentity());
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
         if (this.getDirection().equals("RIGHTLILY")) {
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/rightLily");
         } else
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/leftLily");
        }
    
    
        
}
