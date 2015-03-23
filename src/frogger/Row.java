/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;

import java.awt.Color;
import static java.awt.Color.GRAY;
import static java.awt.Color.GREEN;
import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.MAGENTA;
import static java.awt.Color.YELLOW;
import java.util.ArrayList;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

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
    private final int orderNumber;
    
    // Color for background of row
    private final Color color;

    // FOR EMPTY ROW
    public Row() {
        startXPos = 0;
        startYPos = 0;
        finishXPos = 0;
        finishYPos = 0;
        increment = 0;
        safeRow = -1;
        orderNumber = -1;
        this.direction = "EMPTY";
        this.color = null;
    }

    // For INIT safe row
    public Row(int startX, int startY, int finishX, int finishY, int safeRow, int orderNumber) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = finishY;
        this.direction = "SAFE";
        this.collideables = new ArrayList<>();
        this.safeRow = safeRow;
        this.orderNumber = orderNumber;
        this.color = LIGHT_GRAY;
    }

    // Figure out a way to limit constructors
    // FOR INIT NON-SAFE ROWS
    public Row(int startX, int startY, int finishX, int finishY, int increment,
            int collideableCycle, ArrayList<D> colliders, int safeRow, int orderNumber, Color color) {
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
        this.orderNumber = orderNumber;
        this.color = color;
    }

    // FOR CONTINUING NON-SAFE ROWS
    public Row(int startX, int startY, int finishX, int finishY, int increment,
            int collideableCycle, int collideableTicker, ArrayList<D> colliders,
            int safeRow, int orderNumber, Color color) {
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
        this.orderNumber = orderNumber;
        this.color = color;
    }

    private Row<D> emptyCollisionCopy() {
        return new Row(this.startXPos, this.startYPos, this.finishXPos,
                this.finishYPos, this.increment, this.collideableCycle,
                this.collideableTicker, new ArrayList<D>(), this.safeRow, 
                this.orderNumber, this.color);
    }
    
    private Row<D> makeObstaclesHarder(Score score) {
        // Figure out new values
        int sizedScore = score.score / 10;
        
        // Go faster and appear more
        int newIncrement = sizedScore * this.increment;
        int newCycle = this.collideableCycle / sizedScore;
        
        // Update collideables
        ArrayList<D> newObstacles = new ArrayList<>();
        for (D d : this.collideables) {
            newObstacles.add(d.moreDifficultNextRound(newIncrement));
        }
        
        // Return the fully updated rows
        return new Row(this.startXPos, this.startYPos, this.finishXPos,
                this.finishYPos, newIncrement, newCycle,
                this.collideableTicker, newObstacles, this.safeRow, 
                this.orderNumber, this.color);
    }
    
    private Row<D> makeAssistersHarder(Score score) {
        int sizedScore = score.score / 100;
        
        // Go faster and appear less
        int newCycle = this.collideableCycle * score.score;
        int newIncrement = sizedScore * this.increment;
        
        // Update collideables
        ArrayList<D> newAssisters = new ArrayList<>();
        for (D d : this.collideables) {
            newAssisters.add(d.moreDifficultNextRound(newIncrement));
        }
        
        return new Row(this.startXPos, this.startYPos, this.finishXPos,
                this.finishYPos, newIncrement, newCycle,
                this.collideableTicker, newAssisters, this.safeRow, 
                this.orderNumber, this.color);
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

    public int getOrderNumber() {
        return this.orderNumber;
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
        return this.getSafeRowNumber() < 0;
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
    
    public Row<D> nextObstacleRound(Score score) {
        return this.makeObstaclesHarder(score);
    }
    
    public Row<D> nextAssisterRound(Score score) {
        return this.makeAssistersHarder(score);
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
        Frog newFrog = frog;
        for (D d : this.getCollideables()) {
            // Is it in this row? If so check things
            if (newFrog.getCurrentRow() == this.getOrderNumber()) {
                // If it collides we want to refractor the Frog
                if (newFrog.isCollision(d)) {
                    // We only want to look at the safe rows if there's a collision
                    // Our safeRow placeholder is the row the frog will go back to
                    Row safeRow = new Row();
                    // Find the safe row
                    for (Row s : safeRows) {
                        if (s.getSafeRowNumber() == this.getSafeRowNumber()) {
                            // If the identities are right, then we found what
                            // safe row we are going to 
                            safeRow = s;
                        }
                    }
                    if (!safeRow.isEmpty()) {
                        newFrog = d.refractorObstacleCollisionWithFrog(newFrog, safeRow);
                    } else // Runtime exceptions are scary tho.
                    {
                        throw new RuntimeException("No safe row avaliable");
                    }

                }
            }
        }
        return newFrog;
    }

    public Frog checkAssisterCollisionsWithFrog(Frog frog, ArrayList<Row> safeRows) {
        Frog newFrog = frog;
        if (frog.getCurrentRow() == this.getOrderNumber()) {
            boolean hitLily = false;
            for (D d : this.getCollideables()) {
                // if frog is in that row --> check for collision
                if (newFrog.isCollision(d)) {
                    // If there is a collision, then put the frog on the lily
                    //   System.out.println("Assister Called" + newFrog.getCurrentRow());
                    newFrog = d.refractorAssisterCollisionWithFrog(newFrog);
                    hitLily = true;
                    break;
                }
            }
            if (!hitLily) {
                // Otherwise, we hit the water --> need to find saferow
                Row froggySafeRow = new Row();
                for (Row s : safeRows) {
                    if (s.getSafeRowNumber() == this.getSafeRowNumber()) {
                        // If the safeRows are right, then we found what
                        // safe row we are going to 
                        froggySafeRow = s;
                    }
                }
                // And put the frog on the safe row
                if (!froggySafeRow.isEmpty()) {
                    //     System.out.println("Obstacle Called" + newFrog.getCurrentRow());
                    newFrog = froggySafeRow.refractorObstacleBackgroundCollisionWithFrog(newFrog);
                } else // Runtime exceptions are scary tho.
                {
                    throw new RuntimeException("No safe row avaliable" + this.safeRow);
                }
            }

        }

        return newFrog;
    }

    public Frog refractorObstacleBackgroundCollisionWithFrog(Frog frog) {
        // This means that the frog hit the water/lava/slash whatever
      //  System.out.println("Obstacle Hit" + frog.decrementCurrentRow() + " Background Hit");
        return new Frog(frog.getXPos(), this.getStartY(), frog.image, frog.decrementCurrentRow());
    }
    
    public WorldImage draw() {
        RectangleImage background = new RectangleImage(new Posn(0, this.startYPos), 1000, 100, this.color);
        return background;
    }

}
