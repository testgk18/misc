package chess;

/**
 * Title:        chess
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author till zoppke
 * @version 1.0
 */

public class Move {

  public Field startField = new Field();
  public Field destField = new Field();
  public short pawnImprovePiece = Piece.NO_PIECE;

  public Move() {}

  public Move(String s) {
    this.startField = new Field(s.charAt(1), s.charAt(0));
    this.destField = new Field(s.charAt(3), s.charAt(2));

    // look whether there is pawn improvement
    if (s.length() == 5) {
      this.pawnImprovePiece = Piece.charToByte(s.charAt(4));
    }
  }

  public Move(Field startField, Field destField) {
    this.startField = startField;
    this.destField = destField;
  }

  public Move(Field startField, Field destField, short pawnImprovePiece) {
    this(startField, destField);
    this.pawnImprovePiece = pawnImprovePiece;
  }

  public Move(Field startField, byte[] offset) {
    this(startField, offset[0], offset[1]);
  }

  public Move(Field startField, byte rowOffset, byte colOffset) {
    this.startField = startField;
    this.destField = new Field (
      (byte) (startField.row + rowOffset), (byte) (startField.col + colOffset));
  }

  public Move (Field startField, byte[] offset, byte pawnImprovePiece) {
    this(startField, offset);
    this.pawnImprovePiece = pawnImprovePiece;
  }

  public boolean equals(Move m) {
    return this.startField.equals(m.startField) && this.destField.equals(m.destField)
            && this.pawnImprovePiece == m.pawnImprovePiece;
  }

  public String toString() {
    return this.startField.toString() + this.destField.toString();
  }
}