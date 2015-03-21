/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frogger;

import javalib.colors.White;
import javalib.funworld.World;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;
import javalib.worldimages.WorldImage;

public class EndGame extends World {
    
    private WorldImage background;
    private int score;
    
    public EndGame(String str) {
        this.background = new FromFileImage(new Posn(0, 0), str);
    }
    
    public World onTick() {
        // the world doesn't change onTick
        return this;
    }
    
    public World onKeyEvent(String key) {
         if (key.equals(" ")){
             return new Frogger();
         }
         else {
             return this;
         }
    }
    
    public WorldImage makeImage() {
         WorldImage gameOverText = new OverlayImages(new TextImage(new Posn(235, 225), "Game Over!", 40, new White()),
                    new TextImage(new Posn(235, 275), "Score: " + 50, 40, new White()));
         WorldImage playAgainText = new OverlayImages(gameOverText, new TextImage(new Posn(235, 325), "Press the space bar to play again!", 20, new White()) );
         WorldImage finalImage = new OverlayImages(background, playAgainText);
         return finalImage;
    }
    
    
}
