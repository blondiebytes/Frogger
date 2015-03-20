
package frogger;

import javalib.worldimages.FromFileImage;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;


class Lily implements Collideable<Lily>{
    private int xPos;
    private int yPos;
    private double size = Math.sqrt(xPos^2 + yPos^2);
    private String direction; // "RIGHT" // "LEFT"
    private int identity;
    protected final int increment;
    
    // CONSTANTS:
    static int XMAX = 550;
    static int YMAX = 450;
    static int XMIN = -50;
    static int YMIN = 0;
    static int total = 0;
    
    
    public Lily(int xPos, int yPos, int increment, String direction) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.increment = increment;
        this.direction = direction;
        this.identity = total;
        total++;
               
    }
   
     public Lily(int xPos, int yPos, String direction, int increment, int identity) {
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
    
    public Lily move() {
        if (this.getDirection().equals("RIGHT")) {
            return new Lily(this.getXPos() + this.getIncrement(), this.getYPos(), this.getDirection(), this.getIncrement(), this.getIdentity());
        } else {
            return new Lily(this.getXPos() - this.getIncrement(), this.getYPos(), this.getDirection(), this.getIncrement(), this.getIdentity());
        }
    }
    
     public boolean isOffScreen() {
        return this.xPos >= XMAX;
    }
     
     public Frog refractorCollisionWithFrog(Frog frog, Row safeRow) {
            Frog newFrog;
            // Is the frog on a lily?
            if (frog.isCollision(this)) {
            // If so, make it so. Set direction so we know that the frog is on a lily
              // THen tick the frog so it moves at the same rate as the lily
              newFrog = new Frog(frog.getXPos(), this.getYPos(), frog.getImage(), this.getDirection(), frog.getCurrentRow()).tickMoveFroggy(this);
            } else {
                newFrog = new Frog(frog.getXPos(), safeRow.getStartY(), frog.getImage(), frog.decrementCurrentRow());
            }
            return newFrog;
     
     }
   
    // Could add functionality for colors? WOO
     public WorldImage draw() {
             return new FromFileImage(new Posn(this.xPos, this.yPos), "art/lily.png");
        }
    
    
        
}
