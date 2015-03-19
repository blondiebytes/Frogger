package frogger;

import java.util.ArrayList;
import javalib.funworld.World;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

/**
 *
 * @author kathrynhodge
 */
public class Frogger extends World {
    
    // The game frogger is just a series of rows -> all constantly going
    // The user jumps around the rows via Frog.

    // TO DO LIST:
    // Initialize Rows
    // Make sure that Froggy can't jump into the water. 
    // Score and Lives
    // Identities for each object --> for testing
    // However, if we keep track of the current row --> we can see who we need 
    // to check for collisiosn (making it faster)
    
    // Two different types of rows -> one where you avoid stuff (cars) and 
    // one where you try to jump onto the things (lilies). How different are 
    // they really? Create an interface or just do a type check?
    
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
 
    // Generics aren't working for some reason so resorting to this...
    // An array list of all the lily rows and an arraylist of all the car rows
    private ArrayList<Row<Lily>> lilies;
    private ArrayList<Row<Car>> cars;
    private ArrayList<Row> safe;
    // VS ArrayList<Row<Collideable>>
    
    // Keep track of the current row so we only look at stuff in the next row for collisions
    private int currentRow;

    public Frogger() {
        this.frog = new Frog();
        // we should really set up constraints 
        // --> rows won't change after intiliaization
        // --> things will just be added to the rows
        this.cars = initializeCarRows();
        this.lilies = initializeLilyRows();
        this.safe = initializeSafeRows();
      //  this.rows = initializeRows();
    }

    public Frogger(Frog frog, ArrayList<Row<Car>> cars, ArrayList<Row<Lily>> lilies) {
        this.frog = frog;
        this.cars = cars;
        this.lilies = lilies;
       // this.rows = rows;
    }

    private ArrayList<Row<Car>> initializeCarRows() {
        ArrayList<Row<Car>> newCars = new ArrayList<>();
        // DANGER ROW 2 --> CARS
        // StartY, FinishX, FinishY, Increment, collideableCycle, ArrayList<D> colliders, type
        Row<Car> car1 = new Row(0, 200, 450, 0, 5, 10000, new ArrayList<>(), 1);

//        
        return newCars;
    }
    
    private ArrayList<Row<Lily>> initializeLilyRows() {
        // DANGER ROW 1 --> LILIES
        // StartY, FinishX, FinishY, Increment, collideableCycle, ArrayList<D> colliders, type
       ArrayList<Row<Lily>> newLilies = new ArrayList<>();
       ArrayList<Lily> starterLily = new ArrayList<>();
       starterLily.add(new Lily(0, 400, 5, "RIGHT"));
       Row<Lily> lily1 = new Row(0, 400, 450, 300, 5, 10000, starterLily, 2);
       System.out.println(lily1.getCollideables().size());
       newLilies.add(lily1);
       return newLilies;
    }
    
    private ArrayList<Row> initializeSafeRows() {
     // SAFE ROW 1 // StartX, StartY, FinishX, FinishY
       ArrayList<Row> newRows = new ArrayList<>();
//       Row row1 = new Row<>(0, 500, 450, 400);
//       newRows.add(row1);
//       
//     // SAFE ROW 2
//      Row row2 = new Row(0, 300, 450, 200);
//      newRows.add(row2);
//        
       return newRows;
    }

    public World onTick() {

        // If froggy is on a lily --> froggy moves too, so we create a var for that
        Frog newFrog = this.frog;
        ArrayList<Row<Car>> newCars = this.cars;
        ArrayList<Row<Lily>> newLilies = this.lilies;
        // None of this is really "in place" or "mutative" because of testing

        // Iterate through the rows, moving them all the things in collideables
        // --> and check for collisions
        
        // Hoping to figure out a way to do this more efficiently.... 
        // Calling Cars and Lilies at the same time versus in seperate loops
        for (Row<Car> r : newCars) {
            // Checking for collisions --> will change this to just check for 
            // this in the current row (or the rows around the Frog)
            for (Car c : r.getCollideables()) {
                // Change frog if it collides
                if (this.frog.isCollision(c)) {
                    newFrog = c.refractorCollisionWithFrog(newFrog);
                }
                
                // Move the collider
                c = c.move();
                
                // Remove obstacle/collider if it's offscreen
                if (c.isOffScreen()) {
                    r.getCollideables().remove(c);
                }
            }
            
            //The only thing that updates in a row is the items in it
            // Adding a new collider and removing a colldier if it's time
            Row newCarRow = r.updateRow();
            newCars.add(newCarRow);
        }
       
        for (Row<Lily> x : newLilies ) {
             // Checking for collisions --> will change this to just check for 
            // this in the current row (or the rows around the Frog)
            for (Lily l : x.getCollideables()) {
                // Change frog if it collides
                if (this.frog.isCollision(l)) {
                    newFrog = l.refractorCollisionWithFrog(newFrog);
                }
                
                // Move the collider
                l = l.move();
                
                // Remove obstacle/collider if it's offscreen
                if (l.isOffScreen()) {
                    x.getCollideables().remove(l);
                }
        }
            
            //The only thing that updates in a row is the items in it
            // Adding a new collider and removing a colldier if it's time
            Row<Lily> newLilyRow = x.updateRow();
            newLilies.add(newLilyRow);
         }
        
        return new Frogger(newFrog, newCars, newLilies);
    }

    public World onKeyEvent(String key) {
        Frog newFrog = this.frog.reactMoveFroggy(key);

        // Rows don't react to key presses
        return new Frogger(newFrog, this.cars, this.lilies);
    }

    public WorldImage makeImage() {
        //Overlap everything
        WorldImage backgroundELT1 = new FromFileImage(new Posn(0, 0), "art/FroggerBackground.png");
        WorldImage backgroundELT2 = new FromFileImage(new Posn(0, 0), "art/FroggerBackground.png");
        WorldImage finalImage = new OverlayImages(backgroundELT1, backgroundELT2);

        
        for (Row<Car> r : this.cars) {
            // Iterate through collideables to overlap
            // TO DO: Figure out a way to make this casting less sloppy.
            for (Car c : r.getCollideables()) {
                finalImage = new OverlayImages(finalImage, c.draw());
            }
        }
        
        for (Row<Lily> x : this.lilies) {
            for (Lily l : x.getCollideables()) {
                finalImage = new OverlayImages(finalImage, l.draw());
            }
        }
        
        // add this.frog.drawFroggy() last so it is on top
        finalImage = new OverlayImages(finalImage, this.frog.drawFroggy());

        return finalImage;

    }

    public static void main(String[] args) {
        Frogger frogger = new Frogger();
        frogger.bigBang(500, 500, .01);
    }

}
