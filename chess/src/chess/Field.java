package chess;

/**
 * Title:        chess
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author till zoppke
 * @version 1.0
 */

public class Field {

  public byte row;
  public byte col;

  public Field() {}

  public Field(char row, char col) {
    this ((byte) (row - 49), (byte) (col - 97));
  }

  public Field(String s) {
    this(s.charAt(0), s.charAt(1));
  }

  public Field(byte row, byte col) {
    this.row = row;
    this.col = col;
  }

  /*public Field(Field f, byte[] offset) {
    this(f.row + offset[0], f.col + offset[1]);
  }*/

  public boolean equals(Field field) {
    return this.row == field.row && this.col == field.col;
  }

  public String toString() {
    return new Character ((char) (this.col + 97)) + Integer.toString(this.row + 1);
  }
}