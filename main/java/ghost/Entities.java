package ghost;

import com.google.errorprone.annotations.ForOverride;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class Entities {
  /**
   * The Entity modified game grid
   */
  protected static Entities[][] moddedGrid;

  /**
   * The (y,x) coordinates of Waka
   */
  protected static int[] wakaLocation = new int[] { 0, 0 };

  /**
   * Waka's current direction
   * default state is "A"
   */
  protected static String wakaDirection = "A";

  /**
   * The coordinates required for ghost line
   */
  protected int[] line = new int[] { 0, 0 };

  /**
   * The alive state of an Entity
   * default state for alive is true
   */
  protected boolean alive = true;

  /**
   * Fixed starting coordinates
   */
  protected final int[] start;

  /**
   * Column (x) tile coordinate
   */
  protected int col;

  /**
   * Row (y) tile coordinate
   */
  protected int row;

  /**
   * Row (y) pixel coordinate
   */
  protected int yPixel;

  /**
   * Column (x) pixel coordinate
   */
  protected int xPixel;

  /**
   * Solid state of Entity
   */
  protected boolean solid;

  /**
   * Entity graphic
   */
  protected PImage sprite;

  /**
   * Constructor for an Entity
   * @param x coordinate
   * @param y coordinate
   * @param solid boolean
   * @param sprite PImage
   */
  public Entities(int x, int y, boolean solid, PImage sprite) {
    this.solid = solid;
    this.yPixel = 0;
    this.xPixel = 0;
    this.col = x;
    this.row = y;
    this.start = new int[] { y, x };
    this.sprite = sprite;
  }

  /**
   * An overriden method that determines
   * primary logic for each Entity
   */
  @ForOverride
  public void tick() {
    return;
  }

  /**
   * Retrieves the pixels for specified Entity
   * @return pixel array[2]
   */
  public int[] getPixels() {
    return new int[] { (row * 16 + yPixel), (col * 16 + xPixel) };
  }

  /**
   * Checks whether Entity is a ghost
   * @return True if ghost
   */
  public boolean isGhost() {
    return false;
  }

  /**
   * Modifies the current image of Entity
   * @param new PImage
   */
  public void setImage(PImage newImage) {
    this.sprite = newImage;
  }

  /**
   * Provides Entities class with the game grid
   * @param game grid
   */
  public static void setGrid(Entities[][] grid) {
    moddedGrid = grid;
  }

  /**
   * Updates Entities waka coordinates
   * and waka direction
   * @param Waka coordinates
   * @param Waka direction
   */
  public static void setWaka(int[] coordinates, String direction) {
    wakaLocation = coordinates;
    wakaDirection = direction;
  }

  /**
   * Sets the ghost image to null
   * moves its' coordinates off the map
   * sets alive to false
   */
  public void killGhost() {
    setImage(null);
    this.alive = false;
    this.row = 0;
    this.col = 0;
  }

  /**
   * Checks whether Entity is solid
   * @return True if solid
   */
  public boolean getSolid() {
    return solid;
  }

  /**
   * Retrieves current Entity graphic
   * @return Current PImage
   */
  public PImage getImage() {
    return sprite;
  }

  /**
   * Changes line coordinates based off Entity
   * @param y coordinate
   * @param x coordiante
   */
  public void setLine(int y, int x) {
    this.line = new int[] { y, x };
  }

  /**
   * Method is overidden by subclasses
   * modifies specific conditions to default
   */
  public void reset() {
    return;
  }

  /**
   * Retrieves Entity location on grid
   * @return Entity coordinates
   */
  public int[] getTile() {
    return new int[] { row, col };
  }

  /**
   * Draws a line from Entity to
   * line destination coordinates
   * @param app PApplet
   */
  public void drawLine(PApplet app) {
    app.line((col * 16 + xPixel), (row * 16 + yPixel),
    line[1] * 16 + 8, line[0] * 16 + 8);
    app.stroke(255);
  }

  /**
   * Draws the image onto the application screen
   * takes the grid coordinates, applies a pixel offset
   * applies an image correction based off graphic ratio
   * @param app PApplet
   */
  public void draw(PApplet app) {
    app.image(this.sprite,
      (col * 16 + xPixel) - (sprite.width - 16) / 2,
      (row * 16 + yPixel) - (sprite.height - 16) / 2);
  }
}
