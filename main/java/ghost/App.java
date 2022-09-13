package ghost;

import processing.core.PApplet;

public class App extends PApplet {

  /**
   * Application width
   */
  public static final int WIDTH = 448;

  /**
   * Application height
   */
  public static final int HEIGHT = 576;

  /**
   * Application time
   */
  public static int time = 0;

  /**
   * Instance of GameManager singleton
   */
  public static GameManager manager;

  /**
   * Constructor for App,
   * instantiates a GameManager singleton
   */
  public App() {
    manager = new GameManager("config.json", this);
  }

  /**
   * Sets the frameRate and loads all essential game content
   */
  public void setup() {
    frameRate(60);
    manager.loadGame();
  }

  /**
   * Sets the height and width of the application
   */
  public void settings() {
    size(WIDTH, HEIGHT);
  }

  /**
   * Gets called 60 times a second, drawing the background,
   * calling manager frameCheck and manager draw methods.
   * Resets and reloads game content if end conditions are met
   */
  public void draw() {
    background(0, 0, 0);

    manager.frameCheck(frameCount);
    if (!manager.gameOver()) {
      manager.drawBoard();
    //If 10 seconds have passed and game conditions met, reloads game content
    } else if (manager.getTReset() == 600) {
      manager = new GameManager("config.json", this);
      manager.loadGame();
    }
  }

  /**
   * Take key input and sends it to keyManager
   */
  public void keyPressed() {
    manager.keyManager(keyCode);
  }

  /**
   * Instantiates main PApplet
   */
  public static void main(String[] args) {
    //Runs package -> App
    PApplet.main("ghost.App");
  }
}
