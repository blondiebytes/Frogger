/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;

import java.awt.Color;
import java.util.ArrayList;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

/**
 *
 * @author kathrynhodge
 * @param <D>
 */
public class ObstacleRow<D extends Collideable<D>> extends Row {

    // For tracking colliders

    private int collideableCycle;
    private int collideableTicker;

    // Array of things in the row
    private ArrayList<D> collideables;

    static private int MID = 300; // X-val

    // What direction are we going in FIELD and How FAST are we going?
    private final String direction;
    private int increment;

    // What safe row do we go back to if hit?
    private final int safeRow;

    // Color of background for row
    private final Color color;

    // Starting a new row
    public ObstacleRow(int startX, int startY, int finishX, int finishY, int increment,
            int collideableCycle, ArrayList<D> colliders, int safeRow, int orderNumber, Color color) {
        super(startX, startY, finishX, finishY, orderNumber);
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
        this.color = color;
    }

    // FOR CONTINUING NON-SAFE ROWS --> have to keep track of ticker
    public ObstacleRow(int startX, int startY, int finishX, int finishY, int increment,
            int collideableCycle, int collideableTicker, ArrayList<D> colliders,
            int safeRow, int orderNumber, Color color) {
        super(startX, startY, finishX, finishY, orderNumber);
        this.increment = increment;
        this.collideableCycle = collideableCycle;
        if (startX >= MID) {
            this.direction = "LEFT";
        } else {
            this.direction = "RIGHT";
        }
        this.collideables = colliders;
        this.safeRow = safeRow;
        this.collideableTicker = collideableTicker;
        this.color = color;
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
    
    public int getSafeRow() {
        return this.safeRow;
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

    public Color getColor() {
        return this.color;
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
    //    System.out.println("ticker" + collideableTicker + "cycle" + collideableCycle);
        collideableTicker++;
        return collideableTicker % collideableCycle == 0;
    }

    // A Collider should go away if it goes off screen.
    public boolean shouldGoColliderAway(D collider) {
        // If something hits a collider, it should stay there. 
        // Basically, does it go past the finish line?
        return collider.getXPos() >= this.getFinishX() && collider.getYPos() >= this.getFinishY();

    }

    private ObstacleRow<D> emptyCollisionCopy() {
        return new ObstacleRow(this.getStartX(), this.getStartY(), this.getFinishX(),
                this.getFinishY(), this.increment, this.collideableCycle,
                this.collideableTicker, new ArrayList<>(), this.safeRow,
                this.getOrderNumber(), this.color);
    }

    public ObstacleRow<D> moveCollideables() {
        ObstacleRow<D> newObstacleRow = this.emptyCollisionCopy();
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
    
    public WorldImage draw(WorldImage finalImage) {
        RectangleImage background = new RectangleImage(new Posn(0, this.getStartX()), 1000, (this.getStartY() - this.getFinishY()), this.getColor());
        finalImage = new OverlayImages(finalImage, background);
        for (D d: this.getCollideables()) {
                finalImage = new OverlayImages(finalImage, d.draw());
            }
        return finalImage;
    }

    public ObstacleRow<D> nextObstacleRound(Score score) {
        return this.makeObstaclesHarder(score);
    }

    private ObstacleRow<D> makeObstaclesHarder(Score score) {
        // Figure out new values
        int sizedScore = score.score / 5;

        // Go faster and appear more
        int newIncrement = sizedScore * this.increment;
        int newCycle = this.collideableCycle / 2;

        // Update collideables
        ArrayList<D> newObstacles = new ArrayList<>();
        for (D d : this.collideables) {
            newObstacles.add(d.moreDifficultNextRound(newIncrement));
        }

        // Return the fully updated rows
        return new ObstacleRow(this.getStartX(), this.getStartY(), this.getFinishX(),
                this.getFinishY(), newIncrement, newCycle,
                this.collideableTicker, newObstacles, this.safeRow,
                this.getOrderNumber(), this.color);
    }

    public Frog checkIfCollisionWithFrog(Frog frog, ArrayList<Row> safeRows) {
        //  will change this to check for collisions in current row (or the rows around the Frog)
        Frog newFrog = frog;
        for (D d : this.getCollideables()) {
            // Is it in this row? If so check things
            if (newFrog.getCurrentRow() == this.getOrderNumber()) {
                // If it collides we want to refractor the Frog
                if (newFrog.isCollision(d)) {
                    // We only want to look at the safe rows if there's a collision
                    // Our safeRow placeholder is the row the frog will go back to
                    Row rowSafe = new Row();
                    // Find the safe row
                    for (Row s : safeRows) {
                        if (s.getOrderNumber() == this.getSafeRowNumber()) {
                            // If the identities are right, then we found what
                            // safe row we are going to 
                            rowSafe = s;
                        }
                    }
                    if (!rowSafe.isEmpty()) {
                        newFrog = d.refractorObstacleCollisionWithFrog(newFrog, rowSafe);
                    } else // Runtime exceptions are scary tho.
                    {
                        throw new RuntimeException("No safe row avaliable");
                    }

                }
            }
        }
        return newFrog;
    }

}
