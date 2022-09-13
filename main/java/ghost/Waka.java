package ghost;

import processing.core.PImage;

public class Waka extends Entities {

  private PImage openState;
  private PImage up;
  private PImage down;
  private PImage closed;
  private PImage left;
  private PImage right;
  private long lives;
  private long speed;
  private String[] queue;

  /**
   * Constructor for the Waka/player
   * queue is defaulted to the left
   * @param x int
   * @param y int
   * @param solid boolean
   * @param up PImage
   * @param down PImage
   * @param left PImage
   * @param right PImage
   * @param closed PImage
   */
  public Waka(int x, int y, boolean solid, 
      PImage up, PImage down, PImage left, PImage right, PImage closed) {
    super(x, y, solid, left);
    this.openState = left;
    this.closed = closed;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
    this.queue = new String[] { "A", "A" };
  }

  /**
   * Controls player movement based off input
   * modifies the pixel position by speed, direction
   * and whether the tile in front is solid.
   * Calls tileUpdate() if yPixel is 0 or xPixel is 0
   */
  @Override
  public void tick() {
    openingCheck();
    if (queue[0].equals("W") 
    && !moddedGrid[row - 1][col].getSolid()) {
      this.openState = this.up;
      yPixel -= speed;

    } else if (queue[0].equals("S") 
    && !moddedGrid[row + 1][col].getSolid()) {
      this.openState = this.down;
      yPixel += speed;

    } else if (queue[0].equals("A") 
    && !moddedGrid[row][col - 1].getSolid()) {
      this.openState = this.left;
      xPixel -= speed;

    } else if (queue[0].equals("D") 
    && !moddedGrid[row][col + 1].getSolid()) {
      this.openState = this.right;
      xPixel += speed;
    }

    //If xPixel/yPixel not in (-16,16), calls tileUpdate to change grid location
    if (xPixel % 16 == 0 && xPixel != 0) {
      tileUpdate(true);
    } else if (yPixel % 16 == 0 && yPixel != 0) {
      tileUpdate(false);
    }
  }

  /**
   * Modifies the grid tile based on waka direction
   * and resets the xPixel and yPixel.
   * If the image of the current tile is a Fruit
   * then the image is set to null.
   * Modifies Entity wakaLocation variable
   * @param check boolean for direction control
   */
  public void tileUpdate(boolean check) {
    if (check) {
      if (this.queue[0].equals("A")) {
        col--;
      } else if (this.queue[0].equals("D")) {
        col++;
      }
    } else {
      if (this.queue[0].equals("W")) {
        row--;
      } else if (this.queue[0].equals("S")) {
        row++;
      }
    }
    this.xPixel = 0;
    this.yPixel = 0;
    //Called if the current tile is not Waka, not solid and not Ghost
    if (moddedGrid[row][col] != this && !moddedGrid[row][col].isGhost()) {
      moddedGrid[row][col].setImage(null);
    }
    setWaka(new int[] { row, col }, queue[0]);
  }

  /**
   * Updates the first position in queue
   * if one tile ahead of direction isn't solid
   * and xPixel or yPixel are correct.
   */
  public void openingCheck() {
    if (queue[1].equals("W") 
    && !moddedGrid[row - 1][col].getSolid() && xPixel == 0) {
      queue[0] = queue[1];

    } else if (queue[1].equals("S") 
    && !moddedGrid[row + 1][col].getSolid() && xPixel == 0) {
      queue[0] = queue[1];

    } else if (queue[1].equals("A") 
    && !moddedGrid[row][col - 1].getSolid() && yPixel == 0) {
      queue[0] = queue[1];

    } else if (queue[1].equals("D") 
    && !moddedGrid[row][col + 1].getSolid() && yPixel == 0) {
      queue[0] = queue[1];
    }
  }

  /**
   * Resets all Waka conditions to default
   */
  @Override
  public void reset() {
    this.row = start[0];
    this.col = start[1];
    this.xPixel = 0;
    this.yPixel = 0;
    this.queue = new String[] { "A", "A" };
  }

  /**
   * Sets the last position in queue
   * to player input
   * @param input String
   */
  public void setMove(String input) {
    queue[1] = input;
  }

  /**
   * Modifies the Entity image
   * based on player input and direction
   */
  public void setSprite(PImage mode) {
    super.sprite = mode;
  }

  /**
   * Retrieves the current graphic
   * @return graphic state PImage
   */
  public PImage getCurrentSprite() {
    return openState;
  }

  /**
   * Checks whether the Waka is closed
   * @return True if closed, else False
   */
  public boolean isClosed() {
    if (super.sprite == closed) {
      return true;
    }
    return false;
  }

  /**
   * Modifies the Entity image to closed 
   */
  public void setClosed() {
    super.sprite = this.closed;
  }

  /**
   * Retrieves the first element in queue
   * @return queue[0] String
   */
  public String getMove() {
    return queue[0];
  }

  /**
   * Sets the Waka speed to specified config
   * @param speed long
   */
  public void setSpeed(long speed) {
    this.speed = speed;
  }

  /**
   * Sets the Waka lives to specified config
   * @param lives lon
   */
  public void setLives(long lives) {
    this.lives = lives;
  }

  /**
   * Retrieves the current lives of Waka
   * @return lives long
   */
  public long getLives() {
    return lives;
  }
}
