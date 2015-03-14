package frogger;

import javalib.worldimages.FromFileImage;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

class Frog {

    String color;
    String image; // "UP" "RIGHT" "LEFT" "DOWN
    String isOnLily; // "RIGHT" "LEFT" "NO"
    int xPos;
    int yPos;
    double size; // NEED TO SET

    static int XMAX = 450;
    static int YMAX = 450;
    static int XMIN = 0;
    static int YMIN = 0;
    static int incrementUp = 25;
    static int incrementSideToSide = 25;
    static int elipson = 5;

    public Frog() {
        this.xPos = XMAX / 2;
        this.yPos = YMAX;
        this.image = "UP";
        this.color = "green";
        this.isOnLily = "NO";
    }
    
    // For when we are worrying if we are on a lily
    public Frog(int xPos, int yPos, String image, String isOnLily) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = image;
        this.isOnLily = isOnLily;
    }
    
    // For other times when we aren't checking if we are on a lily
    public Frog(int xPos, int yPos, String image) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = image;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public double getSize() {
        return size;
    }
    
    // When we press keys and move froggy
    public Frog reactMoveFroggy(String key) {
        // Check what key was pressed
        if (key.equals("up")) {
            // If we weren't facing up
            if (!this.image.equals("UP")) {
                // Make the frog face up
                return new Frog(xPos, yPos, "UP");
            } else // If we were already facing up, we can go up
            {
                return new Frog(xPos, yPos + incrementUp, "UP").checkBounds();
            }
        } // Check for all cases
        else if (key.equals("down")) {
            if (!this.image.equals("DOWN")) {
                return new Frog(xPos, yPos, "DOWN");
            }
            return new Frog(xPos, yPos - incrementUp, "DOWN");
        } else if (key.equals("right")) {
            if (!this.image.equals("RIGHT")) {
                return new Frog(xPos, yPos, "RIGHT");
            } else {
                return new Frog(xPos + incrementSideToSide, yPos, "RIGHT");
            }
        } else if (key.equals("left")) {
            if (!this.image.equals("LEFT")) {
                return new Frog(xPos, yPos, "LEFT");
            } else {
                return new Frog(xPos - incrementSideToSide, yPos, "LEFT");
            }
        } else {
            return this;
        }
    }
    
    // When froggy is on a lily and has to move with it
    public Frog tickMoveFroggy() {
        if (this.isOnLily.equals("RIGHT")) {
            return new Frog(this.xPos + Lily.increment, this.yPos, this.image, "RIGHT");
        } else if (this.isOnLily.equals("LEFT")) {
            return new Frog(this.xPos - Lily.increment, this.yPos, this.image, "LEFT");
        } else
            return this;
    }

    public Frog checkBounds() {
        if (xPos >= XMAX) {
            return new Frog(XMAX, this.yPos, this.image);
        } else if (xPos <= XMIN) {
            return new Frog(XMIN, this.yPos, this.image);
        } else if (yPos >= YMAX) {
            return new Frog(this.xPos, YMAX, this.image);
        } else if (yPos <= YMIN) {
            return new Frog(this.xPos, YMIN, this.image);
        } else {
            return this;
        }
    }

    // Getting the distance between two things
    public int distance(Collideable thing) {
        return (int) Math.sqrt(
                (this.getXPos() - thing.getXPos())
                * (this.getXPos() - thing.getXPos())
                + (this.getYPos() - thing.getYPos())
                * (this.getYPos() - thing.getYPos()));

    }

    // If the distance between the frog and an obstacle is less than 
    // the size of both the objects, then we have problems
    public boolean isCollision(Collideable obst) {
         return this.distance(obst) <= (this.getSize() + obst.getSize());
    }

    public WorldImage drawFroggy() {
        if (this.image.equals("UP")) {
            return new FromFileImage(new Posn(this.xPos, this.yPos), "art/upfrog");
        } else if (this.image.equals("DOWN")) {
            return new FromFileImage(new Posn(this.xPos, this.yPos), "art/downfrog");
        } else if (this.image.equals("RIGHT")) {
            return new FromFileImage(new Posn(this.xPos, this.yPos), "art/rightfrog");
        } else {
            return new FromFileImage(new Posn(this.xPos, this.yPos), "art/leftfrog");
        }
    }

}
