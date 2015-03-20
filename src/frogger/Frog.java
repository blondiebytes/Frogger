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
    int currentRow;
    double size = Math.sqrt(xPos^2 + yPos^2);

    static int XMAX = 500;
    static int YMAX = 500;
    static int XMIN = 50;
    static int YMIN = 50;
    static int incrementUp = 100;
    static int incrementSideToSide = 50;
    static int elipson = 5;

    public Frog() {
        this.xPos = XMAX / 2;
        this.yPos = YMAX;
        this.image = "UP";
        this.color = "green";
        this.isOnLily = "NO";
        this.currentRow = 0;
    }
    
    // For when we are worrying if we are on a lily
    public Frog(int xPos, int yPos, String image, String isOnLily, int currentRow) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = image;
        this.isOnLily = isOnLily;
        this.currentRow = currentRow;
    }
    
    // For other times when we aren't checking if we are on a lily
    public Frog(int xPos, int yPos, String image, int currentRow) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = image;
        this.currentRow = currentRow;
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
    
    public String getImage() {
        return image;
    }
    
    public int getIncrementUp() {
        return incrementUp;
    }
    
    public int getIncrementSideToSide() {
        return incrementSideToSide;
    }
    
    public int getCurrentRow(){
        return currentRow;
    }
    
    public int decrementCurrentRow() {
        return currentRow - 1;
    }
    
    public int incrementCurrentRow() {
        return currentRow + 1;
    }
    
    // When we press keys and move froggy
    public Frog reactMoveFroggy(String key) {
        // Check what key was pressed
        if (key.equals("up")) {
            // If we weren't facing up
            if (!this.image.equals("UP")) {
                // Make the frog face up
                return new Frog(xPos, yPos, "UP", currentRow).checkBounds();
            } else // If we were already facing up, we can go up
            {
                return new Frog(xPos, yPos - incrementUp, "UP", incrementCurrentRow()).checkBounds();
            }
        } // Check for all cases
        else if (key.equals("down")) {
            if (!this.image.equals("DOWN")) {
                return new Frog(xPos, yPos, "DOWN", currentRow).checkBounds();
            }
            return new Frog(xPos, yPos + incrementUp, "DOWN", decrementCurrentRow()).checkBounds();
        } else if (key.equals("right")) {
            if (!this.image.equals("RIGHT")) {
                return new Frog(xPos, yPos, "RIGHT", currentRow).checkBounds();
            } else {
                return new Frog(xPos + incrementSideToSide, yPos, "RIGHT", currentRow).checkBounds();
            }
        } else if (key.equals("left")) {
            if (!this.image.equals("LEFT")) {
                return new Frog(xPos, yPos, "LEFT", currentRow).checkBounds();
            } else {
                return new Frog(xPos - incrementSideToSide, yPos, "LEFT", currentRow).checkBounds();
            }
        } else {
            return this;
        }
    }
    
    // When froggy is on a lily and has to move with it
    public Frog tickMoveFroggy(Lily l) {
        if (this.isOnLily.equals("RIGHT")) {
            return new Frog(this.xPos + l.getIncrement(), this.yPos, this.image, "RIGHT", this.currentRow).checkBounds();
        } else if (this.isOnLily.equals("LEFT")) {
            return new Frog(this.xPos - l.getIncrement(), this.yPos, this.image, "LEFT", this.currentRow).checkBounds();
        } else
            return this;
    }

    public Frog checkBounds() {
        if (xPos >= XMAX) {
            return new Frog(XMAX, this.yPos, this.image, this.currentRow);
        } else if (xPos <= XMIN) {
            return new Frog(XMIN, this.yPos, this.image, this.currentRow);
        } else if (yPos >= YMAX) {
            return new Frog(this.xPos, YMAX, this.image, this.currentRow);
        } else if (yPos <= YMIN) {
            return new Frog(this.xPos, YMIN, this.image, this.currentRow);
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
            return new FromFileImage(new Posn(this.xPos, this.yPos), "art/upfrog.png");
        } else if (this.image.equals("DOWN")) {
            return new FromFileImage(new Posn(this.xPos, this.yPos), "art/downfrog.png");
        } else if (this.image.equals("RIGHT")) {
            return new FromFileImage(new Posn(this.xPos, this.yPos), "art/rightfrog.png");
        } else {
            return new FromFileImage(new Posn(this.xPos, this.yPos), "art/leftfrog.png");
        }
    }

}
