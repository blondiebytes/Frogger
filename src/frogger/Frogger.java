
package frogger;

import java.util.ArrayList;
import javalib.funworld.World;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.WorldImage;

/**
 *
 * @author kathrynhodge
 */
public class Frogger extends World{
    
    // TO DO LIST:
    // Make rows
    // Figure out tickers --> different cycles for each row?
    // Draw stuffs
    //

    // MAIN MOVING FIELDS
    public Frog frog;
    public ArrayList<Car> cars;
    public ArrayList<Lily> lilies;
    
    // CONSTANTS
    // Make a new car every cycle
    public static int carCycle = 10000;
    // Tick the ticker every tick
    public static int carTicker = 0;
    // Make a new lily every cycle
    public static int lilyCycle = 10000;
    // Tick the ticker every tick
    public static int lilyTicker = 0;
    
    // NEED CONSTANTS FOR EACH ROW TO PUT STUFF IN
    // I.E. ROW 1 has yPos = 5-, moving left; ROW 2 has yPos = 100, moving right; 
    // and need an algorithm for mixing lilies and cars/obstacles in the same row;
    // ALSO need safe rows versus non-safe/obstacle/collideable rows
    
    // Different cycles for each row?
    
    public Frogger() {
        this.frog = new Frog();
        this.cars = new ArrayList<>();
        this.lilies = new ArrayList<>();
    }
    
    public Frogger(Frog frog, ArrayList<Car> cars, ArrayList<Lily> lilies) {
        this.frog = frog;
        this.cars = cars;
        this.lilies = lilies;
    }
    
    public World onTick() {
        
        // If froggy is on a lily --> froggy moves too, so we create a var for that
        Frog newFrog = this.frog;
  
        // Iterate through lilies, moving them all
        ArrayList<Lily> newLilies = this.lilies;
        for (Lily l : newLilies) {
            // Is the frog on a lily?
            if (this.frog.isCollision(l)) {
                // If so, make it so. Set direction so we know that the frog is on a lily
                newFrog = new Frog(this.frog.xPos, this.frog.yPos, this.frog.image, l.getDirection());
            }
            // Move the lily and frog together
            l = l.moveLily();
            newFrog = newFrog.tickMoveFroggy();
           
        }
        
       //Iterate through cars, moving them all
        ArrayList<Car> newCars = this.cars;
        for (Car c : newCars) {
            c = c.moveCar();
            if (this.frog.isCollision(c)) {
                // Make newFrog return to the last safe row y-pos, keeping same x-pos
            }
        }
        
        
        // Adding Cars, Lilies
        newLilies = addNewLily(newLilies);
        newCars = addNewCar(newCars);
        
        
        
        // Frog doesn't react to ticks
        return new Frogger(newFrog, newCars, newLilies);
    }
    
    public ArrayList<Car> addNewCar(ArrayList<Car> currentCars) {
        // Add a new car if it's time
        carTicker++;
        if (carTicker % carCycle == 0) {
            // Add a new car in a random row, keep track of the rows
            carTicker = 0;
        }
    }
    
    public ArrayList<Lily> addNewLily(ArrayList<Lily> currentLilies) {
        //Iterate through lilies, moving them all
        ArrayList<Lily> newLilies = this.lilies;
        for (Lily l : newLilies) {
            l.moveLily();
        }
        lilyTicker++;
        if (lilyTicker % lilyCycle == 0) {
            // Add a new lily
            lilyTicker = 0;
        }
    }
    
    
    
    public World onKeyEvent(String key) {
        Frog newFrog = this.frog.reactMoveFroggy(key);
        
        // Cars and lilies don't react to key presses
        return new Frogger(newFrog, cars, lilies);
    }
    
    public WorldImage makeImage() {
        //Overlap everything
        WorldImage backgroundELT1; // need to find a background
        WorldImage backgroundELT2;
        WorldImage finalImage = new OverlayImages(backgroundELT1, backgroundELT2);
        // Iterate through cars to overlap
        for (Car c : cars) {
            finalImage = new OverlayImages(finalImage, c.drawCar());
        }
        
        // add this.frog.drawFroggy() last
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
