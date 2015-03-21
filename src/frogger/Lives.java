
package adventure;

import javalib.worldimages.FromFileImage;
import javalib.worldimages.OverlayImages;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

/**
 *
 * @author kathrynhodge
 */
public class Lives {
        
    int life;
    
    public Lives() {
        life = 3;
    }
    
    public Lives(int life) {
        this.life = life;
    }
    
    public Lives subtractLife() {
        return new Lives(this.life - 1);
    }
    
    public boolean gameOver() {
        return this.life <= 0;
    }
   
   
    public WorldImage draw (WorldImage finalImage) {
        WorldImage finalImage2 = finalImage;
        switch (life) {
            case 0:
                break;
            case 1: finalImage2 = new OverlayImages(finalImage, livesImage(30,60));
                break;
            case 2: finalImage2 = new OverlayImages(finalImage,livesImage(30,60));
                    finalImage2 = new OverlayImages(finalImage2,livesImage(55,60));
                break;
            case 3: finalImage2 = new OverlayImages(finalImage, livesImage(30,60));
                     finalImage2 = new OverlayImages(finalImage2, livesImage(55,60));
                      finalImage2 = new OverlayImages(finalImage2, livesImage(80,60));
                break;
            default:
                break;
        }
        return finalImage2;
    }
    
    public WorldImage livesImage(int width, int height) {
            return new FromFileImage(new Posn(width, height), "art/heart.png");
    }
}
