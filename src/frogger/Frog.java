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
    Lily lily; //--> stores a lily that it could leap onto 

    static int XMAX = 500;
    static int YMAX = 450;
    static int XMIN = 0;
    static int YMIN = 50;
    static int incrementUp = 100;
    static int incrementSideToSide = 50;
    static int elipson = 5;

    public Frog() {
        this.xPos = XMAX / 2;
        this.yPos = YMAX;
        this.image = "UP";
        this.color = "green";
        // figure out way to avoid this 
        this.isOnLily = "NO";
        this.lily = null;
        this.currentRow = 0;
    }
    
    // For when we are worrying if we are on a lily
    public Frog(int xPos, int yPos, String image, String isOnLily, Lily lily, int currentRow) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = image;
        this.lily = lily;
        this.currentRow = currentRow;
        this.isOnLily = isOnLily;
    }
    
    // For other times when we aren't checking if we are on a lily
    public Frog(int xPos, int yPos, String image, int currentRow) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = image;
        this.currentRow = currentRow;
        this.lily = null;
        this.isOnLily = "NO";
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
    
    
    
    public int decrementCurrentRow() {
        if (currentRow - 1 < 0) {
            return 0;
        } else 
            return currentRow - 1;
    }
    
    public int incrementCurrentRow() {
        if (currentRow + 1 < 0) {
            return 0;
        } else 
            return currentRow + 1;
    }
    
    public int getCurrentRow() {
        if (currentRow < 0) {
            return 0;
        } else {
            return currentRow;
        }
    }
    
    public Lily getLily() {
        return lily;
    }
    
    // When we press keys and move froggy
    public Frog reactMoveFroggy(String key) {
        // Check what key was pressed
        if (key.equals("up")) {
            // If we weren't facing up
            if (!this.image.equals("UP")) {
                // Make the frog face up
                return new Frog(xPos, yPos, "UP", this.isOnLily, this.lily, currentRow).checkBounds();
            } else // If we were already facing up, we can go up
            {
                return new Frog(xPos, yPos - incrementUp, "UP",incrementCurrentRow()).checkBounds();
            }
        } // Check for all cases
        else if (key.equals("down")) {
            if (!this.image.equals("DOWN")) {
                return new Frog(xPos, yPos, "DOWN", this.isOnLily, this.lily, currentRow).checkBounds();
            }
            return new Frog(xPos, yPos + incrementUp, "DOWN", decrementCurrentRow()).checkBounds();
        } else if (key.equals("right")) {
            if (!this.image.equals("RIGHT")) {
                return new Frog(xPos, yPos, "RIGHT", this.isOnLily, this.lily, currentRow).checkBounds();
            } else {
                return new Frog(xPos + incrementSideToSide, yPos, "RIGHT",currentRow).checkBounds();
            }
        } else if (key.equals("left")) {
            if (!this.image.equals("LEFT")) {
                return new Frog(xPos, yPos, "LEFT", this.isOnLily, this.lily, currentRow).checkBounds();
            } else {
                return new Frog(xPos - incrementSideToSide, yPos, "LEFT", currentRow).checkBounds();
            }
        } else {
            return this;
        }
    }
    
    // When froggy is on a lily and has to move with it
    public Frog tickMoveFroggy(Lily l) {
        switch(this.isOnLily) {
                case "RIGHT": 
                    return new Frog(this.xPos + l.getIncrement(), this.yPos, this.image, "RIGHT", l, this.currentRow).checkBounds();
                case "LEFT": 
                    return new Frog(this.xPos - l.getIncrement(), this.yPos, this.image, "LEFT", l, this.currentRow).checkBounds();
                default:
                    return this;
            }
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
