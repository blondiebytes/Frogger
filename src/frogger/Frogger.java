package frogger;

import java.util.ArrayList;
import javalib.funworld.World;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.WorldImage;

/**
 *
 * @author kathrynhodge
 */
public class Frogger extends World {

    // TO DO LIST:
    // Make each row with its own collideable object. Make it generic. 
    // Then each row keeps track of its own stuff
    // The game frogger is just a series of rows -> all constantly going
    // However, if we keep track of the current row --> we can see who we need 
    // to check for collisiosn (making it faster)
    
    // Two different types of rows -> one where you avoid stuff (cars) and 
    // one where you try to jump onto the things (lilies)
    
    // Random pattern for rows? HMM Algorithm HMM. 
    // Draw stuffs
    //
    // MAIN MOVING FIELDS
    public Frog frog;

    // Rows keeps track of when new stuff should be added --> basically a 
    // series of tickers so each row has different stuff. 
    // Changing speed of each row could be interesting -> just change 
    // the increment in the lily / car class
    // Row is basically like a generator of where stuff should start
    
    // NEED CONSTANTS FOR EACH ROW TO PUT STUFF IN
    // I.E. ROW 1 has yPos = 5-, moving left; ROW 2 has yPos = 100, moving right; 
    // and need an algorithm for mixing lilies and cars/obstacles in the same row;
    // ALSO need safe rows versus non-safe/obstacle/collideable rows
    // Different cycles for each row?
    public ArrayList<Row> rows;
    // Keep track of the current row so we only look at stuff in the next row for collisions
    public int currentRow;

    public Frogger() {
        this.frog = new Frog();
        // we should really set up constraints 
        // --> rows won't change after intiliaization
        // --> things will just be added to the rows
        this.rows = initializeRows();
    }

    public Frogger(Frog frog) {
        this.frog = frog;
    }

    public ArrayList<Row> initializeRows() {
        ArrayList<Row> newRows = new ArrayList<>();
//        // set up all the rows
//        
//        // SAFE ROW 1
//        Row row1 = new Row(0, 450, 450);
//        
//        // DANGER ROW 1 --> CARS
//        Row row2 = new Row(0, 400, 450, 50000);
//        
//        // SAFE ROW 2
//        Row row3 = new Row(450, 350, 0);
//        
//        // DANGER ROW 2 --> LILIES
//        // SOmething to say in this row we have lilies 
//        // so if frog doesn't land on it we got problems
//        Row row4 = new Row(0, 400, 450, 0);
//        
        return newRows;
    }

    public World onTick() {

        // If froggy is on a lily --> froggy moves too, so we create a var for that
        Frog newFrog = this.frog;

        // Iterate through the rows, moving them all the things in collideables
        // --> and check for collisions
        for (Row r : rows) {
            // Why does it want me to change Collideable c to Object c??
            for (Collideable c : r.collideables) {
                if (this.frog.isCollision(c)) {
                    newFrog = c.refractorCollisionWithFrog(newFrog);
                }
            }
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
        for (Lily l : lilies) {
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
