package ghost;

import processing.core.PImage;

public class Fruit extends Entities {

  /**
   * Eaten tracks the number of eaten fruit
   */
  public static int eaten;

  /**
   * Population tracks the total number of fruit
   */
  public static int population;

    /**
   * Constructor for a Fruit Entity,
   * increments the population by one
   * @param x int
   * @param y int
   * @param solid boolean
   * @param sprite PImage
   */
  public Fruit(int x, int y, boolean solid, PImage sprite) {
    super(x, y, solid, sprite);
    population++;
  }

  /**
   * Increments eaten number if graphic is null
   */
  public void tick() {
    if (getImage() == null) {
      eaten++;
    }
  }

  /**
   * Resets the eaten count to 0
   */
  public static void resetEaten() {
    eaten = 0;
  }

  /**
   * Resets the population count to 0
   */
  public static void resetPop() {
    population = 0;
  }
}
