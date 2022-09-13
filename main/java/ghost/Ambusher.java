package ghost;

import processing.core.PImage;

public class Ambusher extends Ghost {

  /**
   * Constructor for Ambusher
   * @param x coordinate
   * @param y coordinate
   * @param solid boolean
   * @param sprite PImage
   * @param fSprite PImage (scared image)
   * @param rSprite PImage (soda image)
   */
  public Ambusher(int x,int y,boolean solid,
      PImage sprite,PImage fSprite,PImage rSprite) {
    super(x, y, solid, sprite);
    scared = fSprite;
    spook = rSprite;
  }

  /**
   * Returns a top right corner coordinate if mode is true,
   * otherwise tracks four spaces ahead of Waka and calculates distance.
   * Updates current line coordinate
   * @param row int
   * @param col int
   * @return corner coordinate or target destination
   */
  public int[] updateStep(int row, int col) {
    int a = wakaLocation[0] - row;
    int b = wakaLocation[1] - col;
    if (mode) {
      setLine(0, 28);
      return new int[] { row, 27 - col };
    }
    //Locating the tile of Waka and modifying by four, based on Waka direction
    int[] num = wakaCheck(a, b, 4);
    setLine(num[0] + row, num[1] + col);

    return new int[] { num[0], num[1] };
  }
}
