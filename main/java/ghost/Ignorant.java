package ghost;

import processing.core.PImage;

public class Ignorant extends Ghost {

  /**
   * Constructor for Ignorant
   * @param x coordinate
   * @param y coordinate
   * @param solid boolean
   * @param sprite PImage
   * @param fSprite PImage (scared image)
   * @param rSprite PImage (soda image)
   */
  public Ignorant(int x, int y, boolean solid, 
      PImage sprite, PImage fSprite, PImage rSprite) {
    super(x, y, solid, sprite);
    scared = fSprite;
    spook = rSprite;
  }

  /**
   * Returns a bottom left corner coordinate if mode is true,
   * or within 8 tiles from Waka location,
   * otherwise tracks Waka and calculates distance.
   * Updates current line coordinate
   * @param row int
   * @param col int
   * @return corner coordinate or target destination
   */
  public int[] updateStep(int row, int col) {
    int a = wakaLocation[0] - row;
    int b = wakaLocation[1] - col;

    //If distance is less than 8 tiles, corner will be returned
    if (Math.sqrt((a * a) + (b * b)) < 8 || mode) {
      setLine(36, 0);
      return new int[] { 35 - row, col };
    }
    setLine(a + row, b + col);
    return new int[] { a, b };
  }
}
