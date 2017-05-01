package chess;

/**
 * Title:        chess
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author till zoppke
 * @version 1.0
 */

public class Piece {

  ///////////////////////////// constants ////////////////////////////////

  // values for pieces
  public static final byte OUTSIDE = 13;
  public static final byte NO_PIECE = 0;
  public static final byte[] KING = new byte[] {1, 7};
  public static final byte[] QUEEN = new byte[] {2, 8};
  public static final byte[] ROOK = new byte[] {3, 9};
  public static final byte[] BISHOP = new byte[] {4, 10};
  public static final byte[] KNIGHT = new byte[] {5, 11};
  public static final byte[] PAWN = new byte[] {6, 12};

  // values for toMove
  public static final byte NO_COLOR = -1;
  public static final byte WHITE = 0;
  public static final byte BLACK = 1;

  // string representation for pieces
  public static final char[] PIECE_CHARS = new char[] {
    '.', 'K', 'Q', 'R', 'B', 'N', 'P', 'k', 'q', 'r', 'b', 'n', 'p'
  };

  public static final byte[][] KNIGHT_MOVES = new byte[][] {
    {-2, -1}, {-1, -2}, {1, 2}, {2, 1}, {-1, 2}, {-2, 1}, {1, -2}, {2, -1}
  };

  public static final byte[][] KING_MOVES = new byte[][] {
    {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
  };

  public static final byte[][][] PAWN_STRIKES = new byte[][][] {

    // white strikes
    { {1, -1}, {1, 1} },

    // black strikes
    { {-1, -1}, {-1, 1} }
  };

  public static final byte[][] PAWN_ONE_MOVES = new byte[][] {
    {1, 0}, {-1, 0}
  };

  public static final byte[][] PAWN_TWO_MOVES = new byte[][] {
    {2, 0}, {-2, 0}
  };

  public static final byte[] SECOND_ROW = new byte[] {1, 6};
  public static final byte[] SEVENTH_ROW = new byte[] {6, 1};

  // rows, where a pawn must be located to strike en passant
  public static final byte[] EN_PASSANT_ROWS = new byte[] {4, 3};

  public static final byte[] COLOR = {
    NO_COLOR, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE,
    BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, NO_COLOR
  };

  ///////////////////////////// fields //////////////////////////////////////

  public byte byteValue;

  public Piece() {}

  public static byte charToByte(char c) {
    switch (c) {
      case '.': return NO_PIECE;
      case 'K': return KING[WHITE];
      case 'Q': return QUEEN[WHITE];
      case 'R': return ROOK[WHITE];
      case 'B': return BISHOP[WHITE];
      case 'N': return KNIGHT[WHITE];
      case 'P': return PAWN[WHITE];
      case 'k': return KING[BLACK];
      case 'q': return QUEEN[BLACK];
      case 'r': return ROOK[BLACK];
      case 'b': return BISHOP[BLACK];
      case 'n': return KNIGHT[BLACK];
      case 'p': return PAWN[BLACK];
      default: return NO_PIECE;
    }
  }
}