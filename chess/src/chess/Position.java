package chess;

import java.util.Vector;

/**
 * Title:        chess
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author till zoppke
 * @version 1.0
 */

public class Position {

  /////////////////////////////// constants ///////////////////////////////////

  // string representing the start position
  public static final String START_POSITION
    = "RNBQKBNRPPPPPPPP................................pppppppprnbqkbnr";

  // encoding colors
  public static final short WHITE = 0;
  public static final short BLACK = 0;

  // encoding castling flags
  public static final short[] CASTLE_LONG = new short[] {0, 2};
  public static final short[] CASTLE_SHORT = new short[] {1, 3};

  // encoding castling moves
  public static final Move[] CASTLE_KING_MOVES = new Move[] {
    new Move("e1c1"), new Move("e1g1"), new Move("e8c8"), new Move("e8g8")
  };
  public static final Move[] CASTLE_ROOK_MOVES = new Move[] {
    new Move("a1d1"), new Move("h1f1"), new Move("a8d8"), new Move("h8f8")
  };

  // encoding rook start fields
  public static final Field[] ROOK_START_FIELD = new Field[] {
    new Field("a1"), new Field("h1"), new Field("a8"), new Field("h8")
  };

  ///////////////////////////// fields ////////////////////////////////////////

  private Board board = new Board();
  private short toMove;
  private boolean[] castleFlags = new boolean[4];
  private short enPassant;

  ////////////////////////////// constructor //////////////////////////////////

  /**
   * Creates a new position whithout initializing any field.
   */
  public Position() {
  }

  ////////////////////////////// init methods ////////////////////////////////////

  /**
   * initializes an empty position
   */
  public void init() {

    // white moves
    this.toMove = WHITE;

    // all castlings are possible
    for (int i=0; i<4; i++) {
      this.castleFlags[i] = true;
    }

    // no en passant possible
    this.enPassant = -1;

    // init board
    this.board.init();
  }

  /**
   * initializes a starting position
   */
  public void reset() {

    // white moves
    this.toMove = WHITE;

    // all castlings are possible
    for (int i=0; i<4; i++) {
      this.castleFlags[i] = true;
    }

    // no en passant possible
    this.enPassant = -1;

    // reset board
    this.board.setBoard(START_POSITION);
  }

  /////////////////////////////// other methods ///////////////////////////////

  /**
   * complete String representation
   */
  public String toString() {

    // create Stringbuffer
    StringBuffer sb = new StringBuffer();

    // append board
    sb.append(this.board.toString());

    // castlings
    sb.append("castlings: ");
    for (int i=0; i<4; i++) {
      sb.append(this.castleFlags[i] ? 'y' : 'n');
    }
    sb.append('\n');

    // toMove
    sb.append("to move: ");
    sb.append(this.toMove == WHITE ? "White" : "Black");
    sb.append('\n');

    // enPassant
    sb.append("en passant: ");
    sb.append(this.enPassant);

    // return as a string
    return sb.toString();
  }

  public void move(Move move) {

    // move piece
    this.board.move(move);
    short nextToMove = (short) (1 - toMove);

    // check, whether castling flags must be changed.
    if (this.castleFlags[CASTLE_LONG[toMove]] || this.castleFlags[CASTLE_SHORT[toMove]]) {

      // check for a white king's move
      if (this.board.getPiece(move.destField) == Piece.KING[toMove]) {

        // reset castling flags to false
        this.castleFlags[CASTLE_LONG[toMove]] = false;
        this.castleFlags[CASTLE_SHORT[toMove]] = false;

        // move rook in case of castling
        if (move.equals(CASTLE_KING_MOVES[CASTLE_LONG[toMove]]))
          this.board.move(CASTLE_ROOK_MOVES[CASTLE_LONG[toMove]]);
        if (move.equals(CASTLE_KING_MOVES[CASTLE_SHORT[toMove]]))
          this.board.move(CASTLE_ROOK_MOVES[CASTLE_SHORT[toMove]]);
      }

      // check for corner moves, disable castling in case
      if (move.startField.equals(ROOK_START_FIELD[CASTLE_LONG[toMove]]))
        this.castleFlags[CASTLE_LONG[toMove]] = false;
      if (move.startField.equals(ROOK_START_FIELD[CASTLE_SHORT[toMove]]))
        this.castleFlags[CASTLE_SHORT[toMove]] = false;
      if (move.destField.equals(ROOK_START_FIELD[CASTLE_LONG[nextToMove]]))
        this.castleFlags[CASTLE_LONG[nextToMove]] = false;
      if (move.destField.equals(ROOK_START_FIELD[CASTLE_SHORT[nextToMove]]))
        this.castleFlags[CASTLE_SHORT[nextToMove]] = false;
    }

    // change toMove flag
    this.toMove = nextToMove;
  }

  public Move[] generateMoves() {

    byte antiColor = (byte) (1 - this.toMove);
    Vector moves = new Vector();

    // recurse on 64 fields
    for (byte row=0; row<8; row++) {
      for (byte col=0; col<8; col++) {
        short piece = this.board.getPiece(row, col);
        Field field = new Field(row, col);

        // case king
        if (piece == Piece.KING[this.toMove]) {
          for (int i=0; i<8; i++) {
            short p = this.board.getPiece(field, Piece.KING_MOVES[i]);
            if (Piece.COLOR[p] != this.toMove && p != Piece.OUTSIDE) {
              moves.add(new Move(field, Piece.KING_MOVES[i]));
            }
          }
        }

        // case knight
        else if (piece == Piece.KNIGHT[this.toMove]) {
          for (int i=0; i<8; i++) {
            short p = this.board.getPiece(field, Piece.KNIGHT_MOVES[i]);
            if (Piece.COLOR[p] != this.toMove && p != Piece.OUTSIDE) {
              moves.add(new Move(field, Piece.KNIGHT_MOVES[i]));
            }
          }
        }

        // case pawn
        else if (piece == Piece.PAWN[this.toMove]) {

          // move forward if there is an empty field
          if (this.board.getPiece(field, Piece.PAWN_ONE_MOVES[this.toMove]) == Piece.NO_PIECE) {

            // check for improving pawn at seventh row
            if (row == Piece.SEVENTH_ROW[this.toMove]) {
              moves.add(new Move(field, Piece.PAWN_ONE_MOVES[this.toMove], Piece.QUEEN[this.toMove]));
              moves.add(new Move(field, Piece.PAWN_ONE_MOVES[this.toMove], Piece.ROOK[this.toMove]));
              moves.add(new Move(field, Piece.PAWN_ONE_MOVES[this.toMove], Piece.BISHOP[this.toMove]));
              moves.add(new Move(field, Piece.PAWN_ONE_MOVES[this.toMove], Piece.KNIGHT[this.toMove]));
            }
            else {

              // move one forward
              moves.add(new Move(field, Piece.PAWN_ONE_MOVES[this.toMove]));

              // check for moving two forward from the second row
              if (row == Piece.SECOND_ROW[this.toMove]
                  && this.board.getPiece(field, Piece.PAWN_TWO_MOVES[this.toMove]) == Piece.NO_PIECE) {
                moves.add(new Move(field, Piece.PAWN_TWO_MOVES[this.toMove]));
              }
            }
          }

          // strike other piece
          // note: en passant striking is checked outside the for-loops.
          for (int i=0; i<2; i++) {
            if (Piece.COLOR[this.board.getPiece(field, Piece.PAWN_STRIKES[this.toMove][i])] == antiColor) {

              // check for improving pawn at seventh row
              if (row == Piece.SEVENTH_ROW[this.toMove]) {
                moves.add(new Move(field, Piece.PAWN_STRIKES[this.toMove][i], Piece.QUEEN[this.toMove]));
                moves.add(new Move(field, Piece.PAWN_STRIKES[this.toMove][i], Piece.ROOK[this.toMove]));
                moves.add(new Move(field, Piece.PAWN_STRIKES[this.toMove][i], Piece.BISHOP[this.toMove]));
                moves.add(new Move(field, Piece.PAWN_STRIKES[this.toMove][i], Piece.KNIGHT[this.toMove]));
              }
              else {

                // common striking
                moves.add(new Move(field, Piece.PAWN_STRIKES[this.toMove][i]));
              }
            }
          }
        } // end of pawn moving

        // case rook or queen
        else if (piece == Piece.QUEEN[this.toMove] || piece == Piece.ROOK[this.toMove]) {
          this.addMoves(moves, field, (byte) 1, (byte) 0);
          this.addMoves(moves, field, (byte) -1, (byte) 0);
          this.addMoves(moves, field, (byte) 0, (byte) 1);
          this.addMoves(moves, field, (byte) 0, (byte) -1);
        }

        // case bishop or queen
        else if (piece == Piece.QUEEN[this.toMove] || piece == Piece.BISHOP[this.toMove]) {
          this.addMoves(moves, field, (byte) 1, (byte) 1);
          this.addMoves(moves, field, (byte) -1, (byte) 1);
          this.addMoves(moves, field, (byte) 1, (byte) -1);
          this.addMoves(moves, field, (byte) -1, (byte) -1);
        }
      }
    }

    // convert vector to array
    Move[] retour = new Move[moves.size()];
    for (int i=0; i<moves.size(); ++i) {
      retour[i] = (Move) moves.get(i);
    }
    return retour;
  }

  private void addMoves(Vector moves, Field startField, byte rowStep, byte colStep) {
    byte row = startField.row;
    byte col = startField.col;

    while (true) {
      row = (byte) (row + rowStep);
      col = (byte) (col + colStep);

      // check, if outside board;
      if (row < 0 || row > 7 || col < 0 || col > 7) {
        return;
      }

      short piece = this.board.getPiece(row, col);
      short color = Piece.COLOR[piece];

      // case destination field is empty: add move to vector
      if (color == Piece.NO_COLOR) {
        moves.add(new Move (startField, new Field(row, col)));
      }

      // case piece at destination field has same color: done
      else if (color == this.toMove) {
        return;
      }

      // case piece at destination field has other color: add move to vector and done
      else if (color == this.getAntiColor()) {
        moves.add(new Move(startField, new Field(row, col)));
        return;
      }
    }
  }

  public boolean isLegalMove(Move move) {
    Move[] moves = this.generateMoves();
    for (int i=0; i<moves.length; ++i) {
      if (move.equals(moves[i])) {
        return true;
      }
    }
    return false;
  }

  /**
   * main method for testing
   */
  public static void main (String args[]) {
    Position p = new Position();
    p.reset();
    System.out.println(p.toString());
  }

  private short getAntiColor() {
    return (short) (1 - this.toMove);
  }
}