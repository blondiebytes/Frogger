
package frogger;

interface Obstacle extends Collideable{
    
    public int getXPos();
    public int getYPos();
    public String getDirection();
    public double getSize();
    
}
