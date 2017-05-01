package chess;

/**
 * Title:        chess
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author till zoppke
 * @version 1.0
 */

public class Board {

  // 64 fields on the board
  private byte[][] fields = new byte[8][8];

  public Board() {}

  public void init() {
    for (int i=0; i<8; i++) {
      for (int j=0; i<8; i++) {
        this.fields[i][j] = 0;
      }
    }
  }

  public void move(Move move) {
    if (move.pawnImprovePiece == Piece.NO_PIECE) {
      this.fields[move.destField.row][move.destField.col]
        = this.fields[move.startField.row][move.startField.col];
    }
    else {
      this.fields[move.destField.row][move.destField.col] = (byte) move.pawnImprovePiece;
    }
    this.fields[move.startField.row][move.startField.col] = Piece.NO_PIECE;
  }

  public byte getPiece(Field field) {
    return this.fields[field.row][field.col];
  }

  public String toString() {
    StringBuffer sb = new StringBuffer(136);
    for (int rows = 7; rows >= 0; rows--) {
      for (int cols = 0; cols < 8; cols++) {
        sb.append(Piece.PIECE_CHARS[this.fields[rows][cols]]);
        sb.append(' ');
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  public void setField(Field f, Piece p) {
    this.fields[f.row][f.col] = p.byteValue;
  }

  public void setBoard(String s) {
    for (int i=0; i<64; ++i)
      this.fields[i/8][i%8] = Piece.charToByte(s.charAt(i));
  }

  public boolean isInCheck(short color) {
    short antiColor = (short) (1 - color);
    Field kingField = this.getKingField(color);
    byte row = kingField.row;
    byte col = kingField.col;

    // check for knight
    for (int i=0; i<8; ++i) {
      if (this.getPiece(kingField, Piece.KNIGHT_MOVES[i]) == Piece.KNIGHT[antiColor])
        return true;
    }

    // check for rook or queen
    while (row > 0) {
      row--;
      if (this.fields[row][col] == Piece.QUEEN[antiColor] || this.fields[row][col] == Piece.ROOK[antiColor])
        return true;
      if (this.fields[row][col] != Piece.NO_PIECE)
        break;
    }

    row = kingField.row;
    while (row < 7) {
      row++;
      if (this.fields[row][col] == Piece.QUEEN[antiColor] || this.fields[row][col] == Piece.ROOK[antiColor])
        return true;
      if (this.fields[row][col] != Piece.NO_PIECE)
        break;
    }

    row = kingField.row;
    while (col > 0) {
      col--;
      if (this.fields[row][col] == Piece.QUEEN[antiColor] || this.fields[row][col] == Piece.ROOK[antiColor])
        return true;
      if (this.fields[row][col] != Piece.NO_PIECE)
        break;
    }

    col = kingField.col;
    while (col < 7) {
      col++;
      if (this.fields[row][col] == Piece.QUEEN[antiColor] || this.fields[row][col] == Piece.ROOK[antiColor])
        return true;
      if (this.fields[row][col] != Piece.NO_PIECE)
        break;
    }

    // check for bishop or queen
    col = kingField.col;
    while (col > 0 && row > 0) {
      col--;
      row--;
      if (this.fields[row][col] == Piece.QUEEN[antiColor] || this.fields[row][col] == Piece.ROOK[antiColor])
        return true;
      if (this.fields[row][col] != Piece.NO_PIECE)
        break;
    }

    col = kingField.col;
    row = kingField.row;
    while (col > 0 && row < 7) {
      col--;
      row++;
      if (this.fields[row][col] == Piece.QUEEN[antiColor] || this.fields[row][col] == Piece.ROOK[antiColor])
        return true;
      if (this.fields[row][col] != Piece.NO_PIECE)
        break;
    }

    col = kingField.col;
    row = kingField.row;
    while (col < 7 && row > 0) {
      col++;
      row--;
      if (this.fields[row][col] == Piece.QUEEN[antiColor] || this.fields[row][col] == Piece.ROOK[antiColor])
        return true;
      if (this.fields[row][col] != Piece.NO_PIECE)
        break;
    }

    col = kingField.col;
    row = kingField.row;
    while (col < 7 && row < 7) {
      col++;
      row++;
      if (this.fields[row][col] == Piece.QUEEN[antiColor] || this.fields[row][col] == Piece.ROOK[antiColor])
        return true;
      if (this.fields[row][col] != Piece.NO_PIECE)
        break;
    }

    // check for king
    for (int i=0; i<8; ++i) {
      if (this.getPiece(kingField, Piece.KING_MOVES[i]) == Piece.KING[antiColor])
        return true;
    }

    // check for pawn
    if (this.getPiece(kingField, Piece.PAWN_STRIKES[antiColor][0]) == Piece.PAWN[antiColor])
      return true;
    if (this.getPiece(kingField, Piece.PAWN_STRIKES[antiColor][1]) == Piece.PAWN[antiColor])
      return true;

    // no ckeck, return false.
    return false;
  }

  /**
   * returns the field of the king
   */
  public Field getKingField(short color) {
    for (byte row=0; row<8; row++) {
      for (byte col=0; col<8; col++) {
        if (this.fields[row][col] == Piece.KING[color]) {
          return new Field(row, col);
        }
      }
    }
    return null;
  }

  /**
   * returns a piece on the field. The location is calculated from a given basis
   * and an offset. If the calculated point is outside the board, Piece.OUTSIDE
   * will be returned.
   */
  public byte getPiece(Field basis, byte rowOffset, byte colOffset) {
    byte row = (byte) (basis.row + rowOffset);
    byte col = (byte) (basis.col + colOffset);
    if (col < 0 || col > 7 || row < 0 || row > 7)
      return Piece.OUTSIDE;
    return this.fields[row] [col];
  }

  public byte getPiece(Field basis, byte[] offset) {
    return this.getPiece(basis, offset[0], offset[1]);
  }

  public byte getPiece(byte row, byte col) {
    return this.fields[row] [col];
  }
}