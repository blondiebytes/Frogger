
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
    // Random pattern for rows? HMM Algorithm HMM. 
    // Draw stuffs
    //

    // MAIN MOVING FIELDS
    public Frog frog;
    
    // Cars and Lilies keep track of the movement and placement
    public ArrayList<Car> cars;
    public ArrayList<Lily> lilies;
    
    // Rows keeps track of when new stuff should be added --> basically a 
    // series of tickers so each row has different stuff. 
    // Changing speed of each row could be interesting -> just change 
    // the increment in the lily / car class
    // Row is basically like a generator of where stuff should start
    public ArrayList<Row> rows;
    
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
        // we should really set up constraints 
        // --> rows won't change after intiliaization
        // --> things will just be added to the rows
        this.rows = new ArrayList<>();
    }
    
    public Frogger(Frog frog, ArrayList<Car> cars, ArrayList<Lily> lilies) {
        this.frog = frog;
        this.cars = cars;
        this.lilies = lilies;
    }
    
    public World onTick() {
        
        // If froggy is on a lily --> froggy moves too, so we create a var for that
        Frog newFrog = this.frog;
  
        // Iterate through lilies, moving them all --> and check for collisions
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
        
       //Iterate through cars, moving them all ---> and check for collisions
        ArrayList<Car> newCars = this.cars;
        for (Car c : newCars) {
            c = c.moveCar();
            if (this.frog.isCollision(c)) {
                // Make newFrog return to the last safe row y-pos, keeping same x-pos
            }
        }
        
        
        // Adding Cars, Lilies
        for (Row r : this.rows) {
            Lily l = r.makeNewLily();
            newLilies.add(l);
            Car c = r.makeNewCar();
            newCars.add(c);
        }
        
        // Frog doesn't react to ticks -> just to user input
        return new Frogger(newFrog, newCars, newLilies);
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
        
        // Iterate through lilies to overlap
        for (Lily l: lilies) {
            finalImage = new OverlayImages(finalImage, l.drawLily());
        }
        
        // add this.frog.drawFroggy() last so it is on top
        finalImage = new OverlayImages(finalImage, this.frog.drawFroggy());
        
        return finalImage;
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
