/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;

import java.util.ArrayList;

public class Row<D extends Collideable> {
   
    
    // Cycling of When To Add FIELDS --> or we could make it randomized.... 
    //                              --> or "seemingly randomized" with a pattern
    private int collideableCycle;
    private int collideableTicker;
    
    
    // Array of things in the row
    private ArrayList<D> collideables;
    
    // Where should the thing in the row start/finish FIELDS
    private int startXPos;
    private int startYPos;
    private int finishXPos;
    private int finishYPos;
     
    // What direction are we going in FIELD and How FAST are we going?
    public String direction; // "RIGHT" "LEFT" "SAFE"
    private int increment;
    
    // What type are we?
    /// WE NEED TO SET THIS SOMEHOW TO KNOW WHAT IS IN OUR ROWS
    private int type; // "0" = Car "1" = Lily "2" = Safe Row
    
    // FOR SAFE ROWS
    // Safe rows don't have colliders, and thus, don't collide with stuff
    public Row(int startX, int startY, int finishX) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = startY;
        this.direction = "SAFE";
        this.collideables = new ArrayList<>();
    }
    
    // FOR NON-SAFE ROWS
    // Have a cycle of when stuff appears, have a storage of stuffs
    public Row(int startX, int startY, int finishX, int increment, int collideableCycle, ArrayList<D> colliders) {
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

        public D makeNewCollider() {
        this.collideableTicker++;
        // If ticker is in the right place for the cycle
        if (this.collideableTicker % this.collideableCycle == 0) {
            // is this a thing?
            switch (this.getType()) {
                case 0:
                    // For some reason the "extends collideable" part isn't working, so we have to cast.. BLAH
                    return (D) new Car(this.startXPos, this.startYPos, this.increment, this.direction);
                case 1:
                    return (D) new Lily(this.startXPos, this.startYPos, this.increment, this.direction);
                default:
                    return null;
            }
                    
        } else
        // Otherwise return null <-- BLAH I don't like nil/null/none/pointer/nope :(
            return null;       
    }
    
    // Update Rows in the Game
    public Row updateRow() {
        if (this.getDirection().equals("SAFE")) {
            return this;
        } else {
            // Add a new collider
            ArrayList<D> newColliderSet = this.collideables;
            D newCollider = this.makeNewCollider();
            if (newCollider != null) {
                // Let's add it if it isn't null
                newColliderSet.add(newCollider);
            }
            return new Row(this.startXPos, this.startYPos, 
                    this.finishXPos, this.increment, this.collideableCycle, newColliderSet);
        }
    }
    
    
    // A Collider should go away if it goes off screen.
    public boolean shouldGoColliderAway(D collider) {
        // If something hits a collider, it should stay there. 
        // Basically, does it go past the finish line?
           return collider.getXPos() >= this.finishXPos && collider.getYPos() >= this.finishYPos;
        
    }
    
    
    
    
    
    
    
    
}
