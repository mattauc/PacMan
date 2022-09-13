package ghost;

import processing.core.PImage;

public abstract class Collectibles extends Entities {

  /**
   * Tagged state of the Collectible
   */
  protected boolean tagged = false;

  /**
   * Constructor for Collectible
   * @param x int
   * @param y int
   * @param solid boolean
   * @param sprite PImage
   */
  public Collectibles(int x, int y, boolean solid, PImage sprite) {
    super(x, y, solid, sprite);
  }
  
}
