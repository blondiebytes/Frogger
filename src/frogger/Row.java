/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;

public class Row {
    
    // Cycling of When To Add FIELDS --> or we could make it randomized.... 
    //                              --> or "seemingly randomized" with a pattern
    public int carCycle;
    public int carTicker;
    public int lilyCycle;
    public int lilyTicker;
    
    // Where should the thing in the row start/finish FIELDS
    public int startXPos;
    public int startYPos;
    public int finishXPos;
    public int finishYPos;
    
    // What direction are we going in FIELD
    public String direction; // "RIGHT" "LEFT"
    
    public Row(int startX, int startY, int carCycle, int lilyCycle) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = 450;
        this.finishYPos = startY;
        this.carCycle = carCycle;
        this.lilyCycle = lilyCycle;
    }
    
    public Row(int startX, int startY, int finishX, int carCycle, int lilyCycle) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = startY;
        this.carCycle = carCycle;
        this.lilyCycle = lilyCycle;
    }
    
    // These methods allow us to keep track of when and where to add a car or lily 
    // to a row. We return what was added so we can add it to the Car/Lily
    // ArrayLists, which keep track of where the car actually is during game play
    
    
    // Can we abstract this to make it for all collideables? Not exactly, because
    // we later add to an arraylist in Frogger.java of that thing
    // ... and casting is scary. But we....
    // could have the arraylist as a parameter, and add it in --> make the class
    // generic, but then it would all be mutable and internal and hard to test. 
    // HMMM. 
    
    public Car makeNewCar() {
        carTicker++;
        // If ticker is in the right place for the cycle
        if (carTicker % carCycle == 0) {
            // make a new car in the appropriate place
            return new Car(this.startXPos, this.startYPos, this.direction);
        } else
        // Otherwise return null <-- BLAH I don't like nil/null/none/pointer/nope :(
            return null;
                
    }
    
    public Lily makeNewLily() {
        lilyTicker++;
        // If ticker is right
        if (lilyTicker % lilyCycle == 0) {
            // make a new lily in the appropriate place
            return new Lily(this.startXPos, this.startYPos, this.direction);
        } else 
          // Otherwise return null;
            return null;
    }
    
    // A Collider should go away if it goes off screen.
    public boolean shouldGoColliderAway(Collideable collider) {
        // If something hits a collider, it should stay there. 
        // Basically, does it go past the finish line?
        return collider.getXPos() >= this.finishXPos && collider.getYPos() >= this.finishYPos;
    }
    
    
    
    
    
    
    
    
}
