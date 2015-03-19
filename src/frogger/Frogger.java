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
 
    public ArrayList<Row<Collideable>> rows;
    // Keep track of the current row so we only look at stuff in the next row for collisions
    public int currentRow;

    public Frogger() {
        this.frog = new Frog();
        // we should really set up constraints 
        // --> rows won't change after intiliaization
        // --> things will just be added to the rows
        this.rows = initializeRows();
    }

    public Frogger(Frog frog, ArrayList<Row<Collideable>> rows) {
        this.frog = frog;
        this.rows = rows;
    }

    public ArrayList<Row<Collideable>> initializeRows() {
        ArrayList<Row<Collideable>> newRows = new ArrayList<>();
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
        ArrayList<Row<Collideable>> newRows = this.rows;
        // None of this is really "in place" or "mutative" because of testing

        // Iterate through the rows, moving them all the things in collideables
        // --> and check for collisions
        for (Row r : newRows) {
            // Checking for collisions --> will change this to just check for 
            // this in the current row (or the rows around the Frog)
            
            // TO DO: Figure out a way to make this casting less sloppy.
            for (Object c : r.getCollideables()) {
                // Change frog if it collides
                Collideable collidingC = (Collideable) c;
                if (this.frog.isCollision(collidingC)) {
                    newFrog = collidingC.refractorCollisionWithFrog(newFrog);
                }
                
                // Move the collider
                collidingC = collidingC.move();
                
                // Remove obstacle/collider if it's offscreen
                if (collidingC.isOffScreen()) {
                    r.getCollideables().remove(c);
                }
                
            }
            
            //The only thing that updates in a row is the items in it
            // Adding a new collider and removing a colldier if it's time
            Row newRow = r.updateRow();
            newRows.add(newRow);
         }
        return new Frogger(newFrog, newRows);
    }

    public World onKeyEvent(String key) {
        Frog newFrog = this.frog.reactMoveFroggy(key);

        // Rows don't react to key presses
        return new Frogger(newFrog, this.rows);
    }

    public WorldImage makeImage() {
        //Overlap everything
        WorldImage backgroundELT1 = new FromFileImage(new Posn(0, 0), "art/FroggerBackground.png");
        WorldImage backgroundELT2 = new FromFileImage(new Posn(0, 0), "art/FroggerBackground.png");
        WorldImage finalImage = new OverlayImages(backgroundELT1, backgroundELT2);

        
        for (Row r : this.rows) {
            // Iterate through collideables to overlap
            // TO DO: Figure out a way to make this casting less sloppy.
            for (Object c : r.getCollideables()) {
                Collideable collidingC = (Collideable) c;
                finalImage = new OverlayImages(finalImage, collidingC.draw());
            }
        }
      
        // add this.frog.drawFroggy() last so it is on top
        finalImage = new OverlayImages(backgroundELT1, this.frog.drawFroggy());

        return finalImage;

    }

    public static void main(String[] args) {
        Frogger frogger = new Frogger();
        frogger.bigBang(500, 500, .01);
    }

}
