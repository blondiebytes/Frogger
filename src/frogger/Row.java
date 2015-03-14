/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;

public class Row {
    public int carCycle;
    public int carTicker;
    public int lilyCycle;
    public int lilyTicker;
    public int startXPos;
    public int startYPos;
    public int finishXPos;
    public int finishYPos;
    
    public Row(int startX, int startY, int carCycle, int lilyCycle) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = 450;
        this.finishYPos = startY;
        this.carCycle = carCycle;
        this.lilyCycle = lilyCycle;
    }
    
    public Row(int startX, int startY, int finishX, int carCycle, int lilyCycle) {
        this.startXPos = startX;
        this.startYPos = startY;
        this.finishXPos = finishX;
        this.finishYPos = startY;
        this.carCycle = carCycle;
        this.lilyCycle = lilyCycle;
    }
    
    // These methods allow us to keep track of when to add a car or lily 
    // to a row. We return what was added so we can add it to the Car/Lily
    // ArrayLists, which keep track of where the car actually is. 
    
    public Car addCar() {
        
    }
    
    public Lily addLily() {
        
    }
    
    
    
    
    
    
    
    
}
