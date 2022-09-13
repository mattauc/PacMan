package ghost;

import java.util.ArrayList;
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class GameManager extends Game {

  private HashMap<Character, PImage> objects = new HashMap<>();
  private ArrayList<Ghost> ghosts = new ArrayList<>();
  private BoardFormat bFormat;
  private PFont font;
  private int timeReset = 0;
  private boolean win = false;
  private boolean lose = false;
  private PApplet app;
  private Waka player;
  private boolean debugger = false;
  private Entities[][] board;

  /**
   * Constructor for GameManager,
   * calls Game parseFile() and creates instance of BoardFormat
   * @param config String
   * @param app PApplet
   */
  public GameManager(String config, PApplet app) {
    super(config);
    this.app = app;
    parseFile();
    bFormat = new BoardFormat(mapFile);
  }

  /**
   * Loads all the game requirements,
   * sets Entity conditions based off specifications
   */
  public void loadGame() {
    loadImages();
    font = app.createFont("src/main/resources/PressStart2P-Regular.ttf", 24, false);

    this.board = bFormat.gameBoard(objects);
    this.player = bFormat.getPlayer();
    this.ghosts = bFormat.getGhosts();

    Ghost.setConditions(modeLengths, frightenedLength, speed, evilLength);
    player.setSpeed(speed);
    player.setLives(lives);
    Entities.setGrid(board);
  }

  /**
   * Loads all images and places them within 
   * a HashMap of character key and PImage value
   * @return true boolean
   */
  public boolean loadImages() {
    objects.put('1', app.loadImage("src/main/resources/horizontal.png"));
    objects.put('2', app.loadImage("src/main/resources/vertical.png"));
    objects.put('3', app.loadImage("src/main/resources/upLeft.png"));
    objects.put('4', app.loadImage("src/main/resources/upRight.png"));
    objects.put('5', app.loadImage("src/main/resources/downLeft.png"));
    objects.put('6', app.loadImage("src/main/resources/downRight.png"));
    objects.put('7', app.loadImage("src/main/resources/fruit.png"));
    objects.put('8', app.loadImage("src/main/resources/cherry.png"));
    objects.put('9', app.loadImage("src/main/resources/soda.png"));
    objects.put('s', app.loadImage("src/main/resources/spook.png"));
    objects.put('a', app.loadImage("src/main/resources/ambusher.png"));
    objects.put('c', app.loadImage("src/main/resources/chaser.png"));
    objects.put('i', app.loadImage("src/main/resources/ignorant.png"));
    objects.put('f', app.loadImage("src/main/resources/frightened.png"));
    objects.put('w', app.loadImage("src/main/resources/whim.png"));
    objects.put('p', app.loadImage("src/main/resources/playerUp.png"));
    objects.put('d', app.loadImage("src/main/resources/playerDown.png"));
    objects.put('l', app.loadImage("src/main/resources/playerLeft.png"));
    objects.put('r', app.loadImage("src/main/resources/playerRight.png"));
    objects.put('k', app.loadImage("src/main/resources/playerClosed.png"));
    return true;
  }

  /**
   * Iterates through the board, calls Entity logic
   * and draws Entity graphic.
   * Checks whether end conditions are met, draws lives
   * and resets eaten.
   */
  public void drawBoard() {
    for (int y = 0; y < board.length; y++) {
      for (int x = 0; x < board[0].length; x++) {

        if (board[y][x] != player) 
            board[y][x].tick();

        if (board[y][x].getImage() != null && !board[y][x].isGhost()) 
          board[y][x].draw(app);
      }
    }

    winCheck();
    player.draw(app);

    //Resets Ghost and Waka conditions if collision
    if (dynamicAction()) {
      resetConditions();
      return;
    }

    drawLives();
    Fruit.resetEaten();
  }

  /**
   * Checks whether the eaten fruit equals
   * the total fruit population (win), else false
   * @return true if win, else false
   */
  public boolean winCheck() {
    if (Fruit.eaten == Fruit.population) {
      win = true;
      endConditions();
      return true;
    }
    return false;
  }

  /**
   * Calls collectibles, iterates through Ghost list,
   * draws line if debugger mode and draws Ghost.
   * If not frightened and Waka hit, return true, 
   * else if frightened, kills Ghost.
   * @return true if Ghost hits Waka, else false
   */
  public boolean dynamicAction() {
    collectibles();

    for (int i = 0; i < ghosts.size(); i++) {
      if (debugger && ghosts.get(i).isAlive() && !Ghost.isFrightened()) 
        ghosts.get(i).drawLine(app);
      
      if (ghosts.get(i).getImage() != null) 
        ghosts.get(i).draw(app);
      
      if (wakaHit(i) && Ghost.isFrightened()) {
        ghosts.get(i).killGhost();

      } else if (wakaHit(i)) {
        return true;
      }
    }
    player.tick();
    return false;
  }

  /**
   * Checks whether the player pixels are within
   * range of Ghost pixels.
   * @param index int
   * @return true if in range, else false
   */
  public boolean wakaHit(int i) {
    if (Math.abs(player.getPixels()[0]-ghosts.get(i).getPixels()[0]) < 16 
    && Math.abs(player.getPixels()[1]-ghosts.get(i).getPixels()[1]) < 16) {
      return true;
    }
    return false;
  }

  /**
   * Checks whether SuperFruit or Soda is active,
   * sets frightened or invisible mode for Ghost if true
   * @return true if successful, else false
   */
  public boolean collectibles() {
    if (SuperFruit.isActive() && !Ghost.isFrightened()) {
      Ghost.setFrightened(true);
      SuperFruit.setActive(false);
      return true;
    }
    if (Soda.isActive() && !Ghost.isInvisible()) {
      Ghost.setInvisible(true);
      Soda.setActive(false);
      return true;
    }
    return false;
  }

  /**
   * Modifies the openState of Waka every 8 frames,
   * and increments time every 60 frames, while updating Ghost time
   * @param frameCount int
   */
  public void frameCheck(int frameCount) {
    if (frameCount % 8 == 0) {
      if (player.isClosed()) {
        player.setSprite(player.getCurrentSprite());
      } else {
        player.setClosed();
      }
    }
    //If frameCount hits 60, increments the time by one second
    if (frameCount % 60 == 0 && frameCount != 0) {
      App.time++;
      Ghost.timeCount(App.time);
    }
  }

  /**
   * Updates the Waka direction based off keyCode input from keyPressed()
   * and/or turns debugger mode on if spacebar
   * @param input (keyCode) int
   * @return true if move is arrow key or spacebar, else false
   */
  public boolean keyManager(int input) {
    if ((input < 37 || input > 40) && input != 32) {
      return false;
    }
    if (input == 38) {
      player.setMove("W");
    } else if (input == 40) {
      player.setMove("S");
    } else if (input == 37) {
      player.setMove("A");
    } else if (input == 39) {
      player.setMove("D");
    }
    //Spacebar turns debugger off and on
    if (input == 32) {
      if (debugger) {
        this.debugger = false;
      } else {
        this.debugger = true;
      }
    }
    return true;
  }

  /**
   * Modifies lose mode and calls game restart
   * if lives are empty. Iterates through Ghosts and
   * resets all Ghost and Waka conditions, while 
   * decreasing Waka live count.
   */
  public void resetConditions() {
    if (player.getLives() == 1) {
      this.lose = true;
      gameOver();
    }
    player.reset();
    for (int i = 0; i < ghosts.size(); i++) {
      ghosts.get(i).reset();
    }
    player.setLives(player.getLives() - 1);
  }

  /**
   * Iterates through the grid at row 34, 
   * setting the image the specified lives.
   * Performs a loop to reverse the displayed lives
   * based on game updates
   */
  public void drawLives() {
    for (int i = 1; i < lives * 2 + 1; i += 2) {
      board[34][i].setImage(objects.get('r'));
    }
    //Reversing down the lives, starting at the far right
    for (int i = (int) lives * 2 - 2; i >= player.getLives() * 2; i -= 2) {
      if (i >= 0) {
        board[34][i + 1].setImage(null);
      }
    }
  }

  /**
   * Resets all Ghost, Waka and Fruit conditions
   * for end-game state
   */
  public void endConditions() {
    ghosts.stream().forEach((Ghost g) -> g.reset());
    player.reset();
    Fruit.resetPop();
  }

  /**
   * If win or lose condition and the restart
   * time condition has not reached 10, lose/win
   * text will be drawn on the application 
   * and time will increment towards 10
   * @return true if lose or win, else false
   */
  public boolean gameOver() {
    if (lose && getTReset() != 600) {
      //Draws text by font, calls end condition and increments timeReset
      app.textFont(font, 24);
      app.text("GAME OVER", 115, 230);
      endConditions();
      timeReset++;
      return true;

    } else if (win && getTReset() != 600) {
      //Draws text by font, calls end condition and increments timeReset
      app.textFont(font, 24);
      app.text("YOU WIN", 135, 230);
      endConditions();
      timeReset++;
      return true;
    }
    timeReset = 0;
    return false;
  }

  /**
   * Retrieves the timeReset variable
   * @return timeReset int
   */
  public int getTReset() {
    return timeReset;
  }
}
