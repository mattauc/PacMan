package ghost;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import processing.core.PImage;

public class BoardFormat {

  private char[][] board;
  private String boardFile;
  private Waka player;
  private ArrayList<Ghost> ghosts = new ArrayList<>();

  /**
   * Constructor for BoardFormat,
   * sets the board to size (y=36,x=28)
   * @param boardFile String
   */
  public BoardFormat(String boardFile) {
    this.board = new char[36][28];
    this.boardFile = boardFile;
  }

  /**
   * Reads a map.txt file,
   * then add the elements of the file to their
   * corresponding board coordinate
   * @return true if successful, else false
   */
  public boolean readMap() {
    if (boardFile == null) {
      return false;
    }
    File file = new File(boardFile);

    try {
      Scanner in = new Scanner(file);
      in.useDelimiter("");

      int count = 0;
      int vert = -1;
      
      //Iterates through file characters, modifying board coordinates to emulate the file
      while (in.hasNext()) {
        String character = in.next();
        if (character.equals("\n") || character.equals("\r")) {
          continue;
        }
        //Incrementing vertical coordinate everytime end of file hit
        if (count % 28 == 0) {
          vert++;
        }
        board[vert][count % 28] = character.charAt(0);
        count++;
      }
      in.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return true;
  }

  /**
   * Calls readMap to read the file,
   * then iterates through the character board,
   * assigning an Entity based off character
   * with necessary graphics.
   * @param graphics Key = Character, value = PImage
   * @return A 2D grid of type Entities
   */
  public Entities[][] gameBoard(HashMap<Character, PImage> graphics) {
    readMap();

    Entities[][] game = new Entities[36][28];

    for (int y = 0; y < game.length; y++) {
      for (int x = 0; x < game[0].length; x++) {

        if (board[y][x] == '0') {
          game[y][x] = new Walls(x, y, false, null);

        } else if (board[y][x] >= '1' && board[y][x] <= '6') {
          game[y][x] = new Walls(x, y, true, 
          graphics.get(board[y][x]));

        } else if (board[y][x] == '7') {
          game[y][x] = new Fruit(x, y, false, 
          graphics.get(board[y][x]));

        } else if (board[y][x] == '8') {
          game[y][x] = new SuperFruit(x, y, false, 
          graphics.get(board[y][x]));

        } else if (board[y][x] == '9') {
          game[y][x] = new Soda(x, y, false, 
          graphics.get(board[y][x]));

        } else if (board[y][x] == 'p') {
          this.player = new Waka(x, y, false,
              graphics.get('p'),
              graphics.get('d'),
              graphics.get('l'),
              graphics.get('r'),
              graphics.get('k'));

          game[y][x] = player;

        } else if (board[y][x] == 'a') {
          ghosts.add(new Ambusher(x, y, false,
              graphics.get('a'),
              graphics.get('f'),
              graphics.get('s')));

          game[y][x] = ghosts.get(ghosts.size() - 1);

        } else if (board[y][x] == 'c') {
          ghosts.add(new Chaser(x, y, false,
              graphics.get('c'),
              graphics.get('f'),
              graphics.get('s')));

          game[y][x] = ghosts.get(ghosts.size() - 1);

        } else if (board[y][x] == 'i') {
          ghosts.add(new Ignorant(x, y, false,
              graphics.get('i'),
              graphics.get('f'),
              graphics.get('s')));

          game[y][x] = ghosts.get(ghosts.size() - 1);

        } else if (board[y][x] == 'w') {
          ghosts.add(new Whim(x, y, false,
              graphics.get('w'),
              graphics.get('f'),
              graphics.get('s')));

          game[y][x] = ghosts.get(ghosts.size() - 1);
        }
      }
    }
    return game;
  }

  /**
   * Retrieves player of type Waka
   * @return player Waka
   */
  public Waka getPlayer() {
    return player;
  }

  /**
   * Retrieves a list of ghosts
   * @return ghosts ArrayList<Ghost>
   */
  public ArrayList<Ghost> getGhosts() {
    return ghosts;
  }
}
