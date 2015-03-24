/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;

import java.awt.Color;
import static java.awt.Color.GRAY;
import static java.awt.Color.GREEN;
import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.MAGENTA;
import static java.awt.Color.YELLOW;
import java.util.ArrayList;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

public class Row<D extends Collideable<D>> {

    // Where should the thing in the row start/finish FIELDS
    private final int startXPos;
    private final int startYPos;
    private final int finishXPos;
    private final int finishYPos;
    
    // What safeRow do we return to?.... Wait! Instead of an identity 
    // we could just store the row... then we get access to everything, but
    // that could take up alot of space... rather than just a hash type of thing
    // to the right row --> except it isn't really a hash.. it's like we 
    // are storing the hash. 
    private final int orderNumber;
    
    // Color for background of row
    private final Color color;

    // FOR EMPTY ROW
    public Row() {
        startXPos = 0;
        startYPos = 0;
        finishXPos = 0;
        finishYPos = 0;
        orderNumber = -1;
        this.color = null;
    }

    // For INIT safe row
    public Row(int startX, int startY, int finishX, int finishY, int orderNumber) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = finishY;
        this.orderNumber = orderNumber;
        this.color = GREEN;
    }

    protected int getStartX() {
        return this.startXPos;
    }

    protected int getStartY() {
        return this.startYPos;
    }

    protected int getFinishX() {
        return this.finishXPos;
    }

    protected int getFinishY() {
        return this.finishYPos;
    }

    protected int getOrderNumber() {
        return this.orderNumber;
    }
    
    protected Color getColor() {
        return this.color;
    }
    
    public boolean isEmpty() {
        return this.getOrderNumber() < 0;
    }

    public WorldImage draw() {
        RectangleImage background = new RectangleImage(new Posn(0, this.startYPos), 1000, 100, this.getColor());
        return background;
    }
    
    

}
