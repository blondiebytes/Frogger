/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;

import java.awt.Color;
import java.util.ArrayList;


public class AssisterRow<D extends Collideable<D>> extends ObstacleRow<D>{
     
    // For init
    public AssisterRow(int startX, int startY, int finishX, int finishY, int increment,
            int collideableCycle, ArrayList<D> colliders, int safeRow, int orderNumber, 
            Color color) {
        super(startX, startY, finishX, finishY, increment,
            collideableCycle, colliders, safeRow, orderNumber, color);
    }
    
    // For continuing
    public AssisterRow(int startX, int startY, int finishX, int finishY, int increment,
            int collideableCycle, int collideableTicker, ArrayList<D> colliders,
            int safeRow, int orderNumber, Color color) {
        super(startX, startY, finishX, finishY, increment,
            collideableCycle, collideableTicker, colliders, safeRow, orderNumber, color);
    }
    
    private AssisterRow<D> emptyCollisionCopy() {
        return new AssisterRow(this.getStartX(), this.getStartY(), this.getFinishX(),
                this.getFinishY(), this.getIncrement(), this.getCollideableCycle(),
                this.getCollideableTicker(), new ArrayList<>(), this.getSafeRow(),
                this.getOrderNumber(), this.getColor());
    }
    
    public AssisterRow<D> moveCollideables() {
         AssisterRow<D> newAssisterRow = this.emptyCollisionCopy();
        for (D d : this.getCollideables()) {
            // Move the collider
            D newCar = d.move();
            // Remove obstacle/collider if it's offscreen
            if (!newCar.isOffScreen()) {
                System.out.println("move");
                newAssisterRow.getCollideables().add(newCar);
            }
        }
        return newAssisterRow;
    }
    
    
    public AssisterRow<D> nextObstacleRound(Score score) {
        return this.makeObstaclesHarder(score);
    }
    
    private AssisterRow<D> makeObstaclesHarder(Score score) {
        int sizedScore = score.score / 100;
        System.out.println("Harder");
        // Go faster and appear less
        int newCycle = this.getCollideableCycle() * score.score;
        int newIncrement = sizedScore * this.getIncrement();
        
        // Update collideables
        ArrayList<D> newAssisters = new ArrayList<>();
        for (D d : this.getCollideables()) {
            System.out.println("addlakdjsf;alsdjkf;askdf");
            newAssisters.add(d.moreDifficultNextRound(newIncrement));
        }
        
        return new AssisterRow(this.getStartX(), this.getStartY(), this.getFinishX(),
                this.getFinishY(), newIncrement, newCycle,
                this.getCollideableTicker(), newAssisters, this.getSafeRow(),
                this.getOrderNumber(), this.getColor());
    }
    
    private Frog refractorBackgroundCollisionWithFrog(Frog frog, Row safeRow) {
        // This means that the frog hit the water/lava/slash whatever
      //  System.out.println("Obstacle Hit" + frog.decrementCurrentRow() + " Background Hit");
        return new Frog(frog.getXPos(), safeRow.getStartY(), frog.getImage(), frog.decrementCurrentRow());
    }
     
    
    @Override
     public Frog checkIfCollisionWithFrog(Frog frog, ArrayList<Row> safeRows) {
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
                    if (s.getOrderNumber() == this.getSafeRowNumber()) {
                        // If the safeRows are right, then we found what
                        // safe row we are going to 
                        froggySafeRow = s;
                    }
                }
                // And put the frog on the safe row
                if (!froggySafeRow.isEmpty()) {
                    //     System.out.println("Obstacle Called" + newFrog.getCurrentRow());
                    newFrog = refractorBackgroundCollisionWithFrog(newFrog, froggySafeRow);
                } else // Runtime exceptions are scary tho.
                {
                    throw new RuntimeException("No safe row avaliable" + this.getSafeRow());
                }
            }

        }

        return newFrog;
    }
     
     

    
    
}
