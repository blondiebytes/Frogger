/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;

import javalib.worldimages.WorldImage;


public interface Collideable<D extends Collideable> {
    public int getXPos();
    public int getYPos();
    public String getDirection();
    public int getIncrement();
    public int getIdentity();
    public double getSize();
    public boolean isOffScreen();
    public Frog refractorCollisionWithFrog(Frog frog, Row safeRow);
    public D move();
    public WorldImage draw();
}
