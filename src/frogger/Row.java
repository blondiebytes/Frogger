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
    public String direction; // "RIGHT" "LEFT" "SAFE" "EMPTY"
    private int increment;
    
    // What safeRow do we return to?.... Wait! Instead of an identity 
    // we could just store the row... then we get access to everything, but
    // that could take up alot of space... rather than just a hash type of thing
    // to the right row --> except it isn't really a hash.. it's like we 
    // are storing the hash. 
     private final int safeRow; 
    
    // FOR SAFE ROWS
    // Safe rows don't have colliders, and thus, don't collide with stuff
    public Row() {
        startXPos = 0;
        startYPos = 0;
        finishXPos = 0;
        finishYPos = 0;
        increment = 0;
        safeRow = -1;
        this.direction = "EMPTY";
    }
    
    public Row(int startX, int startY, int finishX, int finishY, int safeRow) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = finishY;
        this.direction = "SAFE";
        this.collideables = new ArrayList<>();
        this.safeRow = safeRow;
    }
    
    public Row(int startX, int startY, int finishX, int finishY, int increment, int collideableCycle, ArrayList<D> colliders, int safeRow) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = finishY;
        this.increment = increment;
        this.collideableCycle = collideableCycle;
        if (startX >= 400) {
            this.direction = "LEFT";
        } else {
            this.direction = "RIGHT";
        }
        this.collideables = colliders;
        this.collideableTicker = 0;
        this.safeRow = safeRow;
    }
    
    
    // FOR INIT NON-SAFE ROWS
    // Have a cycle of when stuff appears, have a storage of stuffs
    public Row(int startX, int startY, int finishX, int finishY, int increment, int collideableCycle, int collideableTicker, ArrayList<D> colliders, int safeRow) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = finishY;
        this.increment = increment;
        this.collideableCycle = collideableCycle;
        if (startX >= 400) {
            this.direction = "LEFT";
        } else {
            this.direction = "RIGHT";
        }
        this.collideables = colliders;
        this.safeRow = safeRow;
        this.collideableTicker = collideableTicker;
    }
    
    public Row<D> emptyCollisionCopy() {
        return new Row(this.startXPos, this.startYPos, this.finishXPos, 
                this.finishYPos, this.increment, this.collideableCycle, 
                this.collideableTicker, new ArrayList<D>(), this.safeRow);
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
    
    public int getSafeRowNumber() {
        return this.safeRow;
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
    
    public boolean isEmpty() {
        return this.getDirection().equals("EMPTY");
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
    
     public Row<D> moveCollideables() {
        Row<D> newObstacleRow = this.emptyCollisionCopy();
        for (D d : this.getCollideables()) {
                // Move the collider
                D newCar = d.move();
                // Remove obstacle/collider if it's offscreen
                if (!newCar.isOffScreen()) {
                    newObstacleRow.getCollideables().add(newCar);
                }
        }
        return newObstacleRow;
    }
  
    public Frog checkObstacleCollisionsWithFrog(Frog frog, ArrayList<Row> safeRows) {
      //  will change this to check for collisions in current row (or the rows around the Frog)
        Row<D> newObstacleRow = this.emptyCollisionCopy();
        Frog newFrog = frog;
        for (D d : this.getCollideables()) {
            // Change frog if it collides
            // if (newFrog.currentRow == this.identityRow) 
            // --> must initialize identity rows first
            if (newFrog.isCollision(d)) {
               // We only want to look at the safe rows if there's a collision
               // Our safeRow placeholder is the row the frog will go back to
                Row safeRow = new Row();
                    for (Row s : safeRows) {
                        if (s.getSafeRowNumber() == this.getSafeRowNumber()) {
                            // If the identities are right, then we found what
                            // safe row we are going to 
                            safeRow = s;
                        }
                    }
                    if (!safeRow.isEmpty()) {
                        newFrog = d.refractorCollisionWithFrog(newFrog, safeRow);
                    } else // Runtime exceptions are scary tho.
                    {
                        throw new RuntimeException("No safe row avaliable");
                    }

            }
        }
        return newFrog;
    }
    
    // NEED TO FIX
    public Frog checkAssisterCollisionsWithFrog(Frog frog, ArrayList<Row> safeRows) {
        Frog newFrog = frog;
        Row<D> newObstacleRow = this.emptyCollisionCopy();
            for (D d : newObstacleRow.getCollideables()) {
                // Change frog if it doesn't collide, but it's in the row
                Row safeRow = new Row();
                // if frog is in that row --> check for collision 
                // (if collide, put on lily); otherwise return to safe row.
                if (newFrog.isCollision(d)) {
                    newFrog = d.refractorCollisionWithFrog(newFrog, safeRow);
                }
                    
                    
//                    for (Row s : this.safe) {
//                        if (s.getSafeRowNumber() == x.getSafeRowNumber()) {
//                            // If the safeRows are right, then we found what
//                            // safe row we are going to 
//                            safeRow = x;
//                        }
//                    }
//                }
//                
//                if (!safeRow.isEmpty()) {
//                        newFrog = l.refractorCollisionWithFrog(newFrog, safeRow);
//                    } else // Runtime exceptions are scary tho.
//                    {
//                        throw new RuntimeException("No safe row avaliable");
//                    }
            }
        return newFrog;
    }
    
  
    
    
    
    
    
    
    
}
