package ghost;

import processing.core.PImage;

public class Walls extends Entities {
  
  /**
   * Constructor for a Wall Entity
   * @param x int
   * @param y int
   * @param solid boolean
   * @param sprite PImage
   */
  public Walls(int x, int y, boolean solid, PImage sprite) {
    super(x, y, solid, sprite);
  }
}
