package ghost;

import processing.core.PImage;

public class Whim extends Ghost {

  /**
   * Constructor for Whim
   * @param x coordinate
   * @param y coordinate
   * @param solid boolean
   * @param sprite PImage
   * @param fSprite PImage (scared image)
   * @param rSprite PImage (soda image)
   */
  public Whim(int x, int y, boolean solid, 
      PImage sprite, PImage fSprite ,PImage rSprite) {
    super(x, y, solid, sprite);
    scared = fSprite;
    spook = rSprite;
  }

  /**
   * Returns a bottom right corner coordinate if mode is true,
   * otherwise takes chaser coordinates, and tracks location
   * based off chaser and Waka.
   * and calculates distance.
   * Updates current line coordinate
   * @param row int
   * @param col int
   * @return corner coordinate or target destination
   */
  public int[] updateStep(int row, int col) {
    if (mode) {
      setLine(36, 28);
      return new int[] { 35 - row, 27 - col };
    }
    //Locating the tile of Waka and modifying by two, based on Waka direction
    int[] num = wakaCheck(wakaLocation[0], wakaLocation[1], 2);

    int y = num[0] - chaserCoords[0];
    int x = num[1] - chaserCoords[1];
    setLine(num[0] + y, num[1] + x);

    return new int[] { (num[0] - row) + y, (num[1] - col) + x };
  }
}
