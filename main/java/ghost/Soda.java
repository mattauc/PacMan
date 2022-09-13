package ghost;

import java.util.Arrays;
import processing.core.PImage;

public class Soda extends Collectibles {

  private static boolean active = false;

  /**
   * Constructor for Soda
   * @param x int
   * @param y int
   * @param solid boolean
   * @param sprite PImage
   */
  public Soda(int x, int y, boolean solid, PImage sprite) {
    super(x, y, solid, sprite);
  }

  /**
   * Checks whether the Soda is active
   * @return active boolean
   */
  public static boolean isActive() {
    return active;
  }

  /**
   * Sets the active state to check
   * @param check boolean
   */
  public static void setActive(boolean check) {
    active = check;
  }

  /**
   * Checks whether Waka is on Soda
   * and Collectible isn't tagged, then modifies
   * active and tagged state
   */
  @Override
  public void tick() {
    if (Arrays.equals(wakaLocation, getTile()) && !tagged) {
      setActive(true);
      tagged = true;
    }
  }
}
