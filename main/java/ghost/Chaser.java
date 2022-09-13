package ghost;

import processing.core.PImage;

public class Chaser extends Ghost {

  /**
   * Constructor for Chaser
   * @param x coordinate
   * @param y coordinate
   * @param solid boolean
   * @param sprite PImage
   * @param fSprite PImage (scared image)
   * @param rSprite PImage (soda image)
   */
  public Chaser(int x,int y,boolean solid,
      PImage sprite,PImage fSprite,PImage rSprite) {
    super(x, y, solid, sprite);
    scared = fSprite;
    spook = rSprite;
  }

  /**
   * Returns a top left corner coordinate if mode is true,
   * otherwise tracks Waka and calculates distance.
   * Updates current line coordinate
   * @param row int
   * @param col int
   * @return corner coordinate or target destination
   */
  public int[] updateStep(int row, int col) {
    int a = wakaLocation[0] - row;
    int b = wakaLocation[1] - col;

    if (mode) {
      setLine(0, 0);
      return new int[] { row, col };
    }
    setLine(a + row, b + col);
    setChaser(new int[] { row, col });

    return new int[] { a, b };
  }
}
