/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;


public interface Collideable {
    public int getXPos();
    public int getYPos();
    public String getDirection();
    public double getSize();
    public boolean isOffScreen();
}
