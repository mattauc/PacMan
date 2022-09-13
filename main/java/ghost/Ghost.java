package ghost;

import java.lang.Math;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import processing.core.PImage;

public abstract class Ghost extends Entities {

  /**
   * Current Scatter/Search mode, default true
   */
  protected static boolean mode = true;

  /**
   * The time of frightened based off config specifications
   */
  protected static long frigthenedLength;

  /**
   * The speed based off config specifications
   */
  protected static long speed;

  /**
   * Mode settings based off config specifications
   */
  protected static int[] modeLength;

  /**
   * Length of evil based off config specifications
   */
  protected static long evilLength;

  /**
   * Ghost scared image
   */
  protected static PImage scared;

  /**
   * Ghost evil image
   */
  protected static PImage spook;

  /**
   * Time since game start
   */
  protected static int time;

  /**
   * Index to traverse mode specifications
   */
  protected static int index = 0;

  /**
   * Grid coordinates for chaser location
   */
  protected static int[] chaserCoords = new int[] { 0, 0 };

  /**
   * Boolean for frightened mode, default false
   */
  protected static boolean frightened = false;

  /**
   * Boolean for invisible mode, default false
   */
  protected static boolean invisible = false;

  private final PImage colour;
  private static int timeStart;
  private static int evilStart;
  private String input = "A";

  /**
   * Constructor for ghost
   * @param x int
   * @param y int 
   * @param solid boolean
   * @param sprite PImage
   */
  public Ghost(int x, int y, boolean solid, PImage sprite) {
    super(x, y, solid, sprite);
    this.colour = sprite;
  }

  /**
   * Controls Ghost movement based Waka location
   * modifies the pixel position by speed, direction
   * and whether the tile in front is solid.
   * Calls tileUpdate() if yPixel is 0 or xPixel is 0.
   * Checks whether collectible is active
   */
  @Override
  public void tick() {
    if (!alive) 
      return;
      
    if (input.equals("W")) {
      yPixel -= speed;

    } else if (input.equals("S")) {
      yPixel += speed;

    } else if (input.equals("A")) {
      xPixel -= speed;

    } else if (input.equals("D")) {
      xPixel += speed;
    }

    //If xPixel/yPixel not in (-16,16), calls tileUpdate to change grid location
    if (xPixel % 16 == 0 && xPixel != 0) {
      tileUpdate(true);
    } else if (yPixel % 16 == 0 && yPixel != 0) {
      tileUpdate(false);
    }
    collectibleCheck();
  }

    /**
   * Modifies the grid tile based on Ghost direction
   * and resets the xPixel and yPixel.
   * openingCheck called to search for next direction
   * @param check boolean for direction control
   */
  public void tileUpdate(boolean check) {
    if (check) {
      if (this.input.equals("A")) {
        col--;
      } else if (this.input.equals("D")) {
        col++;
      }
    } else {
      if (this.input.equals("W")) {
        row--;
      } else if (this.input.equals("S")) {
        row++;
      }
    }
    this.xPixel = 0;
    this.yPixel = 0;
    openingCheck();
  }

  /**
   * Collects a HashMap of optional moves if not frightened,
   * otherwise collects random input.
   * If not frightened, will iterate through HashMap and find
   * the shortest direction, then modifies Ghost direction.
   */
  public void openingCheck() {
    HashMap<String, Double> check;
    if (!frightened) {
      check = intersectionCheck();
    } else {
      this.input = randomMovement();
      return;
    }

    //Searches for the smallest value and modifies the current input
    Double min = null;
    String key = this.input;
    for (Map.Entry<String, Double> entry : check.entrySet()) {
      if (min == null) {
        min = entry.getValue();
      }

      if (entry.getValue() <= min) {
        min = entry.getValue();
        key = entry.getKey();
      }
    }
    this.input = key;
  }

  /**
   * Creates a HashMap of moves based on all openings,
   * calls a random numbers to choose specified direction
   * @return random direction String
   */
  public String randomMovement() {
    HashMap<Integer, String> move = new HashMap<>();
    Random random = new Random();

    if (!moddedGrid[row - 1][col].getSolid() && !input.equals("S")) 
      move.put(move.size(), "W");

    if (!moddedGrid[row + 1][col].getSolid() && !input.equals("W")) 
      move.put(move.size(), "S");

    if (!moddedGrid[row][col - 1].getSolid() && !input.equals("D")) 
      move.put(move.size(), "A");

    if (!moddedGrid[row][col + 1].getSolid() && !input.equals("A")) 
      move.put(move.size(), "D");
    
    if (move.size() == 0) {
      frightenedTurn();
      return this.input;
    }
    int randNum = random.nextInt(move.size());
    return move.get(randNum);
  }

  /**
   * Creates a HashMap of moves based on all openings,
   * calls locateStep to then detect designated tile
   * @return HashMap of optional directions with distances
   */
  public HashMap<String, Double> intersectionCheck() {
    HashMap<String, Double> intersection = new HashMap<>();

    if (!moddedGrid[row - 1][col].getSolid() && !input.equals("S")) 
        intersection.put("W", locateStep(row - 1, col));

    if (!moddedGrid[row + 1][col].getSolid() && !input.equals("W")) 
        intersection.put("S", locateStep(row + 1, col));

    if (!moddedGrid[row][col - 1].getSolid() && !input.equals("D")) 
        intersection.put("A", locateStep(row, col - 1));

    if (!moddedGrid[row][col + 1].getSolid() && !input.equals("A")) 
        intersection.put("D", locateStep(row, col + 1));

    return intersection;
  }

  /**
   * Locates the direction of Waka and modifies
   * coordinate by num
   * @param y coordinate int 
   * @param x coordinate int
   * @param num offset int
   * @return modified (y,x) coordinate int[]
   */
  public int[] wakaCheck(int y, int x, int num) {
    if (wakaDirection.equals("A")) {
      x -= num;
    } else if (wakaDirection.equals("D")) {
      x += num;
    } else if (wakaDirection.equals("W")) {
      y -= num;
    } else if (wakaDirection.equals("S")) {
      y += num;
    }
    return new int[] { y, x };
  }

  /**
   * Reverses Ghost input if frightened
   * and stuck down a dead end
   * @return true if successful, else false
   */
  public boolean frightenedTurn() {
    if (!frightened) {
      return false;
    }
    if (input.equals("A")) {
      this.input = "D";
    } else if (input.equals("D")) {
      this.input = "A";
    } else if (input.equals("W")) {
      this.input = "S";
   } else if (input.equals("S")) {
      this.input = "W";
    }
    return true;
  }

  /**
   * Retrieves coordinates from updateStep
   * to perform sqrt(a^2+b^2) calculation
   * @param row int
   * @param col int
   * @return sqrt(a^2 + b^2) distance double
   */
  public double locateStep(int row, int col) {
    int[] value = updateStep(row, col);
    return Math.sqrt((value[0] * value[0]) + (value[1] * value[1]));
  }

  /**
   * Abstract method employed by
   * Ghost subclasses to locate designated location
   * @param row int
   * @param col int
   * @return default array int[]
   */
  public abstract int[] updateStep(int row, int col);
  

  /**
   * Method used to swap between Scatter and Search mode
   * based on timeStart, modeLength and Index variables.
   * If check is True, it restarts  mode conditions
   * @param check boolean
   */
  public static void setMode(boolean check) {
    if (!frightened) {
      if (timeStart == modeLength[index] && index % 2 == 0) {
        mode = false;
        index++;
        timeStart = 0;
      } else if (timeStart == modeLength[index] && index % 2 == 1) {
        mode = true;
        index++;
        timeStart = 0;
      }
      //Resets Ghost mode conditions
      if (index > modeLength.length || check) {
        mode = true;
        index = 0;
        timeStart = 0;
      }
    }
  }

  /**
   * Checks wether frightened/invisible condition has been met.
   * Modifies images depending on the current state of modes
   */
  public void collectibleCheck() {
    if (isFrightened() && timeStart == frigthenedLength) {
      setFrightened(false);
      timeStart = 0;

    } else if (isInvisible() && evilStart == evilLength) {
      setInvisible(false);
    }
    //Modifies graphics for correct mode
    if (isInvisible()) {
      setImage(spook);
    } else if (isFrightened()) {
      setImage(scared);
    } else {
      setImage(colour);
    }
  }

  /**
   * Sets all the base Ghost conditions based
   * on config specifications
   * @param modeL int[]
   * @param frightenedL long
   * @param num long
   * @param evil long
   */
  public static void setConditions(int[] modeL, long frightenedL, 
      long num, long evil) {
    frigthenedLength = frightenedL;
    modeLength = modeL;
    speed = num;
    evilLength = evil;
  }

  /**
   * Updates the time, timeStart and evilStart,
   * calls setMode to check condition variables
   * @param value int
   */
  public static void timeCount(int value) {
    setMode(false);
    timeStart++;
    evilStart++;
    time = value;
  }


  /**
   * Checks whether Ghost is frightened
   * @return frightened boolean
   */
  public static boolean isFrightened() {
    return frightened;
  }

  /**
   * Checks whether Ghost is invisible
   * @return invisible boolean
   */
  public static boolean isInvisible() {
    return invisible;
  }

  /**
   * If check is True, sets frightened mode
   * to true and timeStart to 0, else frightened
   * to False
   * @param check boolean
   */
  public static void setFrightened(boolean check) {
    if (check) {
      timeStart = 0;
      frightened = true;
    } else {
      frightened = false;
    }
  }

  /**
   * If check is True, sets invisible mode
   * to true and evilStart to 0, else invisible
   * to False
   * @param check boolean
   */
  public static void setInvisible(boolean check) {
    if (check) {
      evilStart = 0;
      invisible = true;
    } else {
      invisible = false;
    }
  }

  /**
   * Checks whether Ghost is alive
   * @return alive boolean
   */
  public boolean isAlive() {
    return alive;
  }

  /**
   * Modifies the chaser tracking coordinate 
   * @param coordinates int[]
   */
  public static void setChaser(int[] coords) {
    chaserCoords = coords;
  }

  /**
   * Overrides Entities isGhost() method
   * and return True
   * @return true boolean
   */
  @Override
  public boolean isGhost() {
    return true;
  }

  /**
   * Resets all the Ghost conditions back
   * to their default settings
   */
  @Override
  public void reset() {
    setFrightened(false);
    setInvisible(false);
    setMode(true);
    alive = true;
    setImage(colour);
    this.row = start[0];
    this.col = start[1];
    this.xPixel = 0;
    this.yPixel = 0;
    this.input = "A";
  }
}
