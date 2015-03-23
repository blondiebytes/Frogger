package frogger;

import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.GRAY;
import java.util.ArrayList;
import javalib.colors.White;
import javalib.funworld.World;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;
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
    // --> Make each level harder according to score algorithm
    // ------------ ------------ ------------ ------------ ------------
    // Two different types of rows -> one where you avoid stuff (cars) and 
    // one where you try to jump onto the things (lilies). How different are 
    // they really? Create an interface or just do a type check?
    // MAIN MOVING FIELDS
    public Frog frog;

    // Rows keeps track of when new stuff should be added --> 
    // Row is basically like a generator of where stuff should start
    // An arraylist of all the lily rows and an arraylist of all the car rows
    private ArrayList<AssisterRow<Lily>> lilies;
    private ArrayList<ObstacleRow<Car>> cars;
    private ArrayList<Row> safe;
    // VS ArrayList<Row<Collideable>>

    // Score and Lives
    private Score score;
    private Lives lives;

    // When we start a completely new game
    public Frogger() {
        this.frog = new Frog();
        // we should really set up constraints 
        // --> rows won't change after intiliaization
        // --> things will just be added to or removed from the rows
        this.cars = initializeCarRows();
        this.lilies = initializeLilyRows();
        this.safe = initializeSafeRows();
        this.score = new Score();
        this.lives = new Lives();
    }

    // When we start a new round
    public Frogger(Score score, Lives lives, ArrayList<ObstacleRow<Car>> cars, ArrayList<AssisterRow<Lily>> lilies) {
        this.frog = new Frog();
        this.cars = nextRoundCarRows(cars, score);
        this.lilies = nextRoundLilyRows(lilies, score);
        // safe rows don't change between rounds
        this.safe = initializeSafeRows();
        this.score = score;
        this.lives = lives;
    }

    // When we are continuing a game
    public Frogger(Frog frog, ArrayList<ObstacleRow<Car>> cars, ArrayList<AssisterRow<Lily>> lilies, ArrayList<Row> safe, Score score, Lives lives) {
        this.frog = frog;
        this.cars = cars;
        this.lilies = lilies;
        this.safe = safe;
        this.score = score;
        this.lives = lives;
    }

    private ArrayList<ObstacleRow<Car>> initializeCarRows() {
        ArrayList<ObstacleRow<Car>> newCars = new ArrayList<>();
        // DANGER ROW 2 --> CARS
        // StartY, FinishX, FinishY, Increment, collideableCycle, ArrayList<D> colliders, numberOfSafeRowToReturn, numberforOrderInRows
        ObstacleRow<Car> car1 = new ObstacleRow(-75, 150, 500, 50, 2, 500, new ArrayList<>(), 1, 3, BLACK);
        newCars.add(car1);
        return newCars;
    }

    private ArrayList<AssisterRow<Lily>> initializeLilyRows() {
        // DANGER ROW 1 --> LILIES
        // StartY, FinishX, FinishY, Increment, collideableCycle, ArrayList<D> colliders, numberOfSafeRowToReturn, numberforOrderInRows
        ArrayList<AssisterRow<Lily>> newLilies = new ArrayList<>();
        AssisterRow<Lily> lily1 = new AssisterRow(-30, 350, 500, 250, 1, 200, new ArrayList<>(), 0, 1, BLUE);
        newLilies.add(lily1);
        return newLilies;
    }

    private ArrayList<Row> initializeSafeRows() {
        ArrayList<Row> initialRows = new ArrayList<>();
        // StartX, StartY, FinishX, FinishY, numberforOrderInRows
        Row safe0 = new Row(0, 450, 500, 350, 0);
        initialRows.add(safe0);
        // StartX, StartY, FinishX, FinishY, numberOfSafeRow, numberforOrderInRows
        Row safe1 = new Row(0, 250, 500, 150, 2);
        initialRows.add(safe1);
        // StartX, StartY, FinishX, FinishY, numberOfSafeRow, numberforOrderInRows
        Row safe2 = new Row(0, 50, 500, -50, 4);
        initialRows.add(safe2);

        return initialRows;
    }

    // Need an initialization for nextRound stuff with difficulty based on score
    private ArrayList<ObstacleRow<Car>> nextRoundCarRows(ArrayList<ObstacleRow<Car>> prevCars, Score score) {
        ArrayList<ObstacleRow<Car>> newCars = new ArrayList<>();
        // Want a method that changes the cycle based on the score\
        for (ObstacleRow<Car> r : prevCars) {
            ObstacleRow newRow = r.nextObstacleRound(score);
            newCars.add(newRow);
        }
        return newCars;
    }

    private ArrayList<AssisterRow<Lily>> nextRoundLilyRows(ArrayList<AssisterRow<Lily>> prevLilies, Score score) {
        ArrayList<AssisterRow<Lily>> newLilies = new ArrayList<>();
        // Want a method that changes the cycle based on the score
        for (AssisterRow<Lily> r : prevLilies) {
            AssisterRow newRow = r.nextObstacleRound(score);
            newLilies.add(newRow);
            System.out.println("add row");
        }
        return newLilies;
    }

    public boolean gameOver() {
        // If the frog is at the top, the game is over
        //  System.out.println(lives.getLives());
        return this.lives.gameOver();
    }

    public boolean nextRound() {
        // When we are at the top, start the next round
        return this.frog.getYPos() == Frog.YMIN;
    }

    public World onTick() {
        //    System.out.println(lives.getLives());
        if (gameOver()) {
            return new EndGame("art/FroggerBackground.png", this.score);
        }

        if (nextRound()) {
            // Subtly get harder or whole new game? HMMMMHMMMHMM
            // --> the score as a counter for difficulty algorithm???
            Score newScore = this.score.addScore();
            return new Frogger(newScore, this.lives, this.cars, this.lilies);
        }
        // If froggy is on a lily --> froggy moves too, so we create a var for that
        Frog newFrog = this.frog;
        // None of this is really "in place" or "mutative" because of testing
        ArrayList<ObstacleRow<Car>> newCars = new ArrayList<>();
        ArrayList<AssisterRow<Lily>> newLilies = new ArrayList<>();
        Lives newLives = this.lives;

        // Iterate through the rows, moving them all the things in collideables
        // --> and check for collisions
        for (ObstacleRow<Car> r : this.cars) {
            // if a frog is on a lily, then we don't need to check this.
            if ((newFrog.isOnLily.equals("NO"))) {
                Frog newestFrog = r.checkIfCollisionWithFrog(newFrog, this.safe);
                // Checking if we are kicked back
                if (!newestFrog.equal(newFrog)) {
                    newLives = newLives.subtractLife();
                }
                newFrog = newestFrog;
            }
            //The only thing that updates in a row is the items in it
            ObstacleRow<Car> newCarRow = r.moveCollideables();

            // Adding a new collider if it's time
            // Must do this here because cannot add a new D in generics
            if (newCarRow.isTimeForNewCollider()) {
                Car aCar = new Car(newCarRow.getStartX(), newCarRow.getStartY(),
                        newCarRow.getIncrement(), newCarRow.getDirection());
                newCarRow.getCollideables().add(aCar);
            }
            newCars.add(newCarRow);
        }

        for (AssisterRow<Lily> x : this.lilies) {
            if (newFrog.isOnLily.equals("NO")) {
                Frog newestFrog = x.checkIfCollisionWithFrog(newFrog, this.safe);
                if (newestFrog.getCurrentRow() != newFrog.getCurrentRow()) {
                    System.out.println("check froggy");
                    newLives = newLives.subtractLife();
                }
                newFrog = newestFrog;
            } else {
                newFrog = newFrog.tickMoveFroggy(newFrog.getLily());
            }
            //The only thing that updates in a row is the items in it
            AssisterRow<Lily> newLilyRow = x.moveCollideables();

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
        // Score only changes between rounds
        return new Frogger(newFrog, newCars, newLilies, this.safe, this.score, newLives);
    }

    public World onKeyEvent(String key) {
        Frog newFrog = this.frog.reactMoveFroggy(key);
        Lives newLives = this.lives;
        if (newFrog.isOnLily.equals("NO")) {
            for (ObstacleRow<Car> r : this.cars) {
                // Checking for collisions -->
                newFrog = r.checkIfCollisionWithFrog(newFrog, this.safe);
            }
            for (AssisterRow<Lily> x : this.lilies) {
                Frog newestFrog = x.checkIfCollisionWithFrog(newFrog, this.safe);
                if (newestFrog.getCurrentRow() != newFrog.getCurrentRow()) {
                    System.out.println("check froggy2");
                    newLives = newLives.subtractLife();
                }
                newFrog = newestFrog;
            }
        }
        //System.out.println("newFrog current row"+ newFrog.getCurrentRow());
        // Rows don't react to key presses
        // Check score and lives onTick 

        return new Frogger(newFrog, this.cars, this.lilies, this.safe, this.score, newLives);
    }

    public WorldImage makeImage() {
//        //Overlap everything
        WorldImage backgroundELT1 = new FromFileImage(new Posn(0, 0), "art/FroggerBackground.png");
//
//        // Draw background and froggy
        // This doesn't do anything because all the other draws overwrite it
        // but i needed a way to instantiate.... Will come up with a better way later
        WorldImage finalImage = new OverlayImages(backgroundELT1, backgroundELT1);
        
        // Drawing cars
        for (ObstacleRow<Car> r : this.cars) {
            // Iterate through collideables to overlap
            finalImage = new OverlayImages(finalImage, r.draw());
            for (Car c : r.getCollideables()) {
                finalImage = new OverlayImages(finalImage, c.draw());
            }
        }
        // Drawing lilies
        for (AssisterRow<Lily> x : this.lilies) {
            finalImage = new OverlayImages(finalImage, x.draw());
            for (Lily l : x.getCollideables()) {
                finalImage = new OverlayImages(finalImage, l.draw());
            }
        }
        
        for(Row safe : this.safe){
            finalImage = new OverlayImages(finalImage, safe.draw());
        }

        // Drawing Score
        TextImage score2 = new TextImage(new Posn(55, 35), "Score: " + this.score.score, 18, new White());
        finalImage = new OverlayImages(finalImage, score2);

        // Drawing Lives
        finalImage = this.lives.draw(finalImage);

        // Adding froggy last so it's on top
        finalImage = new OverlayImages(finalImage, this.frog.drawFroggy());

        return finalImage;
    }

    public static void main(String[] args) {
        Frogger frogger = new Frogger();
        frogger.bigBang(500, 500, .01);
    }

}
