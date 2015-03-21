
package frogger;

public class Score {
    int score;
    
    public Score() {
        score = 0;
    }
    
    public Score(int score) {
        this.score = score;
    }
    
    public int getScore() {
        return score;
    }
    
    public Score addScore() {
        return new Score(this.score + 10);
    }
    
     public Score subtractScore() {
         return new Score( this.score - 10);
    }
    
    // NEED TO IMPLEMENT
    public void draw() {
        
    }
}
