package ghost;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class Game {

  /**
   * String containing the map file
   */
  protected static String mapFile;

  /**
   * Long containing Waka lives
   */
  protected static long lives;

  /**
   * Long containing Ghost and Waka speed
   */
  protected static long speed;

  /**
   * Long containing the fightened duration
   */
  protected static long frightenedLength;

  /**
   * Array of integers containg mode duration
   */
  protected static int[] modeLengths;

  /**
   * Long containing the duration of evil mode
   */
  protected static long evilLength;

  private String jsonFile;

  /**
   * Constructor for Game
   * @param jsonFile String
   */
  public Game(String jsonFile) {
    this.jsonFile = jsonFile;
  }

  /**
   * Reads a JSON file and define class variables
   */
  public void parseFile() {
    JSONParser jsonParser = new JSONParser();
    try {
      FileReader reader = new FileReader(this.jsonFile);
      JSONObject config = (JSONObject) jsonParser.parse(reader);

      mapFile = (String) config.get("map");
      lives = (long) config.get("lives");
      speed = (long) config.get("speed");
      frightenedLength = (long) config.get("frightenedLength");
      evilLength = (long) config.get("evilLength");

      //Creates a JSONArray and iterates through contents, converting long into int
      JSONArray mLength = (JSONArray) config.get("modeLengths");
      modeLengths = new int[mLength.size()];
      for (int i = 0; i < mLength.size(); i++) {
        modeLengths[i] = (int) (long) mLength.get(i);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Abstract method for GameManager to draw board
   */
  public abstract void drawBoard();

  /**
   * Abstract method for GameManager to frameCheck
   * @param frames int
   */
  public abstract void frameCheck(int frameCount);

  /**
   * Abstract method for GameManager to manage key input
   * @param input int
   * @return true if successful, else false
   */
  public abstract boolean keyManager(int input);
}
