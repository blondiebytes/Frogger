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
    // ------------ ------------ ------------ ------------ ------------
    // TO DO LIST:
    // --> Make sure that Froggy can't jump into the water in the beginning (add sentinal value?)
    // --> Score and Lives
    // Identities for each object --> for testing
    // ------------ ------------ ------------ ------------ ------------
    // Two different types of rows -> one where you avoid stuff (cars) and 
    // one where you try to jump onto the things (lilies). How different are 
    // they really? Create an interface or just do a type check?
    
    // MAIN MOVING FIELDS
    public Frog frog;

    // Rows keeps track of when new stuff should be added --> 
    // Row is basically like a generator of where stuff should start
    // An arraylist of all the lily rows and an arraylist of all the car rows
    private ArrayList<Row<Lily>> lilies;
    private ArrayList<Row<Car>> cars;
    private ArrayList<Row> safe;
    // VS ArrayList<Row<Collideable>>

    public Frogger() {
        this.frog = new Frog();
        // we should really set up constraints 
        // --> rows won't change after intiliaization
        // --> things will just be added to or removed from the rows
        this.cars = initializeCarRows();
        this.lilies = initializeLilyRows();
        this.safe = initalizeSafeRows();
    }

    public Frogger(Frog frog, ArrayList<Row<Car>> cars, ArrayList<Row<Lily>> lilies, ArrayList<Row> safe) {
        this.frog = frog;
        this.cars = cars;
        this.lilies = lilies;
        this.safe = safe;
    }

    private ArrayList<Row<Car>> initializeCarRows() {
        ArrayList<Row<Car>> newCars = new ArrayList<>();
        // DANGER ROW 2 --> CARS
        // StartY, FinishX, FinishY, Increment, collideableCycle, ArrayList<D> colliders, numberOfSafeRowToReturn, numberforOrderInRows
        Row<Car> car1 = new Row(-100, 200, 500, 100, 5, 100, new ArrayList<>(), 2, 3);
        newCars.add(car1);
        return newCars;
    }

    private ArrayList<Row<Lily>> initializeLilyRows() {
        // DANGER ROW 1 --> LILIES
        // StartY, FinishX, FinishY, Increment, collideableCycle, ArrayList<D> colliders, numberOfSafeRowToReturn
        ArrayList<Row<Lily>> newLilies = new ArrayList<>();
        Row<Lily> lily1 = new Row(-50, 400, 500, 300, 1, 200, new ArrayList<>(), 1, 1);
        newLilies.add(lily1);
        return newLilies;
    }

    private ArrayList<Row> initalizeSafeRows() {
        ArrayList<Row> initialRows = new ArrayList<>();
        // StartX, StartY, FinishX, FinishY, numberOfSafeRow
        Row safe1 = new Row(0, 500, 500, 400, 1, 0);
        initialRows.add(safe1);
        // StartX, StartY, FinishX, FinishY, numberOfSafeRow
        Row safe2 = new Row(0, 300, 500, 200, 2, 2);
        initialRows.add(safe2);
        return initialRows;
    }

    public World onTick() {

        // If froggy is on a lily --> froggy moves too, so we create a var for that
        Frog newFrog = this.frog;
        // None of this is really "in place" or "mutative" because of testing
        ArrayList<Row<Car>> newCars = new ArrayList<>();
        ArrayList<Row<Lily>> newLilies = new ArrayList<>();

        
        // Iterate through the rows, moving them all the things in collideables
        // --> and check for collisions
        for (Row<Car> r : this.cars) {
            if ((newFrog.isOnLily.equals("NO"))) {
                newFrog = r.checkObstacleCollisionsWithFrog(newFrog, this.safe);
            }
            //The only thing that updates in a row is the items in it
            Row<Car> newCarRow = r.moveCollideables();
            
            // Adding a new collider if it's time
            // Must do this here because cannot add a new D in generics
            if (newCarRow.isTimeForNewCollider()) {
                Car aCar = new Car(newCarRow.getStartX(), newCarRow.getStartY(),
                        newCarRow.getIncrement(), newCarRow.getDirection());
                newCarRow.getCollideables().add(aCar);
            }
            newCars.add(newCarRow);
        }

        for (Row<Lily> x : this.lilies) {
            if (newFrog.isOnLily.equals("NO")) {
                newFrog = x.checkAssisterCollisionsWithFrog(newFrog, this.safe);
            } else {
                newFrog = newFrog.tickMoveFroggy(newFrog.getLily());
            }
            //The only thing that updates in a row is the items in it
            Row<Lily> newLilyRow = x.moveCollideables();
            
            // Adding a new collider if it's time
            // Must do this here because we can't initialize a D generic
            if (newLilyRow.isTimeForNewCollider()) {
                // Really would like to put this code somewhere else in a different class, 
                // but.... here it is for now :)
                // It reveals a lot of implementation...
                Lily aLily = new Lily(newLilyRow.getStartX(), newLilyRow.getStartY(),
                        newLilyRow.getIncrement(), newLilyRow.getDirection());
                newLilyRow.getCollideables().add(aLily);
            }
            newLilies.add(newLilyRow);
        }
     //   System.out.println("I looked through the lilies");

        // Safe rows will never change --> we could if we want coins on them later, 
        // but that's for later
        return new Frogger(newFrog, newCars, newLilies, this.safe);
    }

    public World onKeyEvent(String key) {
        Frog newFrog = this.frog.reactMoveFroggy(key);
        if (newFrog.isOnLily.equals("NO")) {
        for (Row<Car> r : this.cars) {
            // Checking for collisions -->
            newFrog = r.checkObstacleCollisionsWithFrog(newFrog, this.safe);
        }
        for (Row<Lily> x : this.lilies) {
            newFrog = x.checkAssisterCollisionsWithFrog(newFrog, this.safe);
            }
        }
        //System.out.println("newFrog current row"+ newFrog.getCurrentRow());
        // Rows don't react to key presses
        
        return new Frogger(newFrog, this.cars, this.lilies, this.safe);
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
        frogger.bigBang(500, 600, .01);
    }

}
