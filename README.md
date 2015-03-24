# Frogger: Spring Break Project
The game frogger is just a series of rows -> all constantly going -> and the user, as the Frog, moves around each row via the arrow keys. This program was created more as a template for "future Froggers" than as a self contained game. There are three types of rows - Row, ObstacleRow, and AssisterRow. 

1. In Row, the row is considered a "safe" row where the Frog cannot be hit by an obstacle.

2. ObstacleRow has everything a Row has except it has a series of obstacles that go across (or down) the row and if the user hits the obstacle while in the row, the Frog must go back to the last safe row. In this program, the ObstacleRow holds cars, but it could have an Collideable Object that the programmer wants to exhibit this type of Row Behavior. 

3. AssisterRow has everything a Row and ObstacleRow has except it overrides several methods in ObstacleRow. In jumping towards an AssisterRow, the user must jump onto a Collidable Object, such as a Lily, in order to stay in the AssisterRow. If the user does not jump onto the Collidable Object, then the Frog is sent back to the last safe row. In this program the AssisterRow holds lilies, but like the ObstacleRow, it could hold any Collideable Object that the programmer wants to exhibit this type of Row Behavior. 

This program is optimized so that the game can have any number of Safe, Obstacle, or Assister Rows and the Assister and Obstacle Rows, in turn, can have any number of Collideables.

Each time the player makes it to the end of the Frogger level, the user gets 10 points. Every time the player hits an obstacle or is not on an Assister while in an AssisterRow, the player loses a life. After losing three lives, the player loses the game, but by pressing the spacebar, the user can start a new game. 

I've attempted to optimize the graphics so that the programmer can just input specific and the graphics will just appear automatically. 

