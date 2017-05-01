package chess;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Title:        chess
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author till zoppke
 * @version 1.0
 */

public class Game {

  public Game() {
  }

  public static void main(String args[]) {

    System.out.println("Welcome to hello world chess.");
    Position position = new Position();
    position.reset();
    Engine engine = new DefaultEngine();
    engine.setPosition(position);

    // variables for while loop
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Move move = null;
    Move[] moves;
    String input = "";
    boolean legalMoveFlag;

    while (true) {
      System.out.println(position);
      legalMoveFlag = false;
      while (!legalMoveFlag) {
        System.out.print("> ");
        try { input = reader.readLine(); }
        catch(IOException e) { e.printStackTrace(); }
        move = new Move(input);
        if (position.isLegalMove(move)) {
          legalMoveFlag = true;
        }
        else {
          System.out.println("illegal move!");
        }
      }
      position.move(move);
      System.out.println(position);
      move = engine.computeMove();
      position.move(move);
    }
  }
}