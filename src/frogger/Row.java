/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;

import java.util.ArrayList;

public class Row<D extends Collideable<D>> {
   
    
    // Cycling of When To Add FIELDS --> or we could make it randomized.... 
    //                              --> or "seemingly randomized" with a pattern
    private int collideableCycle;
    private int collideableTicker;
    
    
    // Array of things in the row
    private ArrayList<D> collideables;
    
    // Where should the thing in the row start/finish FIELDS
    private final int startXPos;
    private final int startYPos;
    private final int finishXPos;
    private final int finishYPos;
     
    // What direction are we going in FIELD and How FAST are we going?
    public String direction; // "RIGHT" "LEFT" "SAFE"
    private int increment;
    
    // What number are we?
    private int identity; 
    
    // FOR SAFE ROWS
    // Safe rows don't have colliders, and thus, don't collide with stuff
    public Row(int startX, int startY, int finishX, int finishY, int identity) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = finishY;
        this.direction = "SAFE";
        this.collideables = new ArrayList<>();
        this.identity = identity;
    }
    
    public Row(int startX, int startY, int finishX, int finishY, int increment, int collideableCycle, ArrayList<D> colliders, int identity) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = startY;
        this.increment = increment;
        this.collideableCycle = collideableCycle;
        if (startX >= 400) {
            this.direction = "LEFT";
        } else {
            this.direction = "RIGHT";
        }
        this.collideables = colliders;
        this.collideableTicker = 0;
        this.identity = identity;
    }
    
    
    // FOR INIT NON-SAFE ROWS
    // Have a cycle of when stuff appears, have a storage of stuffs
    public Row(int startX, int startY, int finishX, int finishY, int increment, int collideableCycle, int collideableTicker, ArrayList<D> colliders, int identity) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = startY;
        this.increment = increment;
        this.collideableCycle = collideableCycle;
        if (startX >= 400) {
            this.direction = "LEFT";
        } else {
            this.direction = "RIGHT";
        }
        this.collideables = colliders;
        this.identity = identity;
        this.collideableTicker = collideableTicker;
    }
    
    public Row<D> emptyCollisionCopy() {
        return new Row(this.startXPos, this.startYPos, this.finishXPos, this.finishYPos, this.increment, this.collideableCycle, this.collideableTicker, new ArrayList<D>(), this.type);
    }
    
    public int getStartX() {
        return this.startXPos;
    }
    
    public int getStartY() {
        return this.startYPos;
    }
    
    public int getFinishX() {
        return this.finishXPos;
    }
    
    public int getFinishY() {
        return this.finishYPos;
    }
    
    public int getIncrement() {
        return increment;
    }
    
    public int getType() {
        return this.type;
    }
    
    public String getDirection() {
        return this.direction;
    }
    
    public int getCollideableCycle() {
        return this.collideableCycle;
    }
    
    public int getCollideableTicker() {
        return this.collideableTicker;
    }
   
    public ArrayList<D> getCollideables() {
        return this.collideables;
    }
    
    
    // THE THINKING INVOLVED IN MAKING THIS KINDA GENERIC:
    // Can we abstract this to make it for all collideables? Not exactly, because
    // we later add to an arraylist in Frogger.java of that thing
    // ... and casting is scary. But we....
    // could have the arraylist as a parameter, and add it in --> make the class
    // generic, but then it would all be mutable and internal and hard to test. 
    // HMMM. 
    
    
    // These methods allow us to keep track of when and where to add a car or lily 
    // to a row. We return what was added so we can make sure it's correct
    // in testing


  public boolean isTimeForNewCollider() {
      collideableTicker++;
      return collideableTicker % collideableCycle == 0;
  }
    
    // A Collider should go away if it goes off screen.
    public boolean shouldGoColliderAway(D collider) {
        // If something hits a collider, it should stay there. 
        // Basically, does it go past the finish line?
           return collider.getXPos() >= this.finishXPos && collider.getYPos() >= this.finishYPos;
        
    }
    
    
    
    
    
    
    
    
}
