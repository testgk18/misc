package chess;

/**
 * Title:        chess
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author till zoppke
 * @version 1.0
 */

public class DefaultEngine implements Engine {

  private Position position;

  public DefaultEngine() {}

  public void setPosition(Position p) {
    this.position = p;
  }

  public Move computeMove() {
    return this.position.generateMoves()[0];
  }
}