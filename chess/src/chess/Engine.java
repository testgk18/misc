package chess;

/**
 * Title:        chess
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author till zoppke
 * @version 1.0
 */

public interface Engine {

  public void setPosition(Position p);
  public Move computeMove();
}