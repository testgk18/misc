// Passed system test
// BEGIN CUT HERE
// PROBLEM STATEMENT
// THIS PROBLEM WAS TAKEN FROM THE SEMIFINALS OF THE TOPCODER INVITATIONAL
// TOURNAMENT
// 
// PROBLEM STATEMENT
// In a game of chess, the king is always looking out for his safety, trying to
// stay out of the path of the opponent's attacking pieces. Given an x by y
// rectangular chess board, with z opponent pieces placed on the board,
// determine
// how many safe squares are left on the board, where the king may be placed.
// Safe squares are squares that are not in the path of attack of one or more
// opponent piece(s).
// 
// Discription of piece movement:
// - A queen's path of attack is any square contained in a horizontal, vertical,
// or diagonal line crossing the square on which it stands.
// - A rook's path of attack is any square contained in a horizontal or vertical
// line crossing the square on which it stands.
// - A bishop's path of attack is any square contained in a diagonal line
// crossing
// the square on which it stands.
// - A knight's path of attack is the last square of an L shaped movement
// (described below) starting from the square it is standing on.
// - A pawns path of attack is the first diagonal square in *all four
// directions*
// starting from the square it is standing on.
// 
// The following must be true for a valid L shaped movement:
// 1. Start from the square the knight is standing on.
// 2. If the knight moves m square(s) horizontally then it must move n square(s)
// vertically
// 3. If the knight moves m square(s) vertically then it must move n square(s)
// horizontally
// 4. If m equals 1 then n must equal 2
// 5. If n equals 1 then m must equal 2
// 
// The input will be a String[] representing the squares on a rectangular chess
// board. Each square is represented by a character having one of the following
// values:
// 'U' will represent an unoccupied square (there are no pieces located in that
// square)
//  'Q' will represent a queen in that square
//  'R' will represent a rook in that square
//  'B' will represent a bishop in that square
//  'K' will represent a knight in that square
//  'P' will represent a pawn in that square
// 
// The following diagrams are meant to show how the pieces can move. The pieces
// are represented as above, 'X' represents a square in the path of attack of
// the
// piece and '+' represents a safe square.
// 
//     Queen Rook Bishop Knight Pawn
// X + + X + + X + + + X + + + X + + + + + X + + + + + + + + + + + + + +
// + X + X + X + + + + X + + + + X + + + X + + + X + X + + + + + + + + +
// + + X X X + + + + + X + + + + + X + X + + + X + + + X + + + X + X + +
// X X X Q X X X X X X R X X X + + + B + + + + + + K + + + + + + P + + +
// + + X X X + + + + + X + + + + + X + X + + + X + + + X + + + X + X + +
// + X + X + X + + + + X + + + + X + + + X + + + X + X + + + + + + + + +
// X + + X + + X + + + X + + + X + + + + + X + + + + + + + + + + + + + +
// 
// Implement a class ChessCover which contains a method getSafe. getSafe should
// return the number of safe squares on the board.
// 
// DEFINITION
// Class name: ChessCover
// Method name: getSafe
// Parameters: String[]
// Returns: int
// Method signature (be sure your method is public): int getSafe (String[]
// boardLayout);
// 
// NOTES
// - The king is not on the board. The purpose is to count the number of
// available safe squares on which the king can placed.
// - The board may be full, i.e. every square on the board can have a piece on
// it
// (z <= x*y).
// - There are five types of pieces: queen, rook, bishop, knight, and pawn.
// - The same type of piece may be placed on the board multiple times (i.e.
// there
// may be 5 queens on the board etc.).
// - The square on which a piece sits is not a safe square.
// - A Piece can block another piece's path of attack.
// - A knight is the only piece that can jump over another piece(s).
// - A pawn's path of attack differs from regular chess rules. The pawn path of
// attack is the first diagonal square in *all four directions* starting from
// the
// square it is standing on.
// - The board is not necessarily square. The number of rows could be the
// different than the number of columns.
// 
// TopCoder will ensure the validity of the inputs. Inputs are valid if all of
// the following criteria are met:
// - Each element of boardLayout will have a length between 1 and 10
// (inclusive).
// - Each element of boardLayout will be of equal length.
// - Each element of boardLayout will consist of characters from the following
// list: 'U', 'Q', 'R', 'B', 'K', 'P'.
// - boardLayout will have between 1 and 10 elements, inclusive.
// 
// EXAMPLES
// U U -> + + -> 4 safe squares
// U U + +
// 
// U U U U U X X X X X
// U Q U Q U -> X Q X Q X -> 0 safe squares
// U U U U U X X X X X
// 
// U U U X + X
// U P U -> + P + -> 4 safe squares
// U U U X + X
// 
// U U U U X + X +
// U U U U X X + +
// Q U U U -> Q X X X -> 6 safe squares
// U U U U X X + +
// 
// U U U U U Q X X X X X Q
// U U U U U U + X X X X X
// B U R U U U -> B X R X X X -> 6 safe squares
// U U K U U U + X K + + X
// U U U U U U X + X + X X
// 
// U B U K U U U B U U -> + B + K + X X B X X -> 4 safe squares
// U U U U B U U Q U R X X X + B X X Q X R
// 
// 
// DEFINITION
// Class:ChessCover
// Method:getSafe
// Parameters:String[]
// Returns:int
// Method signature:int getSafe(String[] param0)
// 
// END CUT HERE

import java.awt.Point;
import java.util.StringTokenizer;

public class ChessCover {

    Point[] knightmoves = new Point[] { new Point(1, 2), new Point(2, 1),
            new Point(-1, -2), new Point(-2, -1), new Point(2, -1),
            new Point(1, -2), new Point(-2, 1), new Point(-1, 2) };

    Point[] pawnmoves = new Point[] { new Point(1, 1), new Point(1, -1),
            new Point(-1, 1), new Point(-1, -1) };

    Point[] rookDirs = new Point[] { new Point(0, 1), new Point(1, 0),
            new Point(-1, 0), new Point(0, -1) };

    Point[] bishDirs = new Point[] { new Point(1, 1), new Point(1, -1),
            new Point(-1, -1), new Point(-1, 1) };

    Point[] qDirs = new Point[] { new Point(0, 1), new Point(1, 0),
            new Point(-1, 0), new Point(0, -1), new Point(1, 1),
            new Point(1, -1), new Point(-1, -1), new Point(-1, 1) };

    char[][] board;

    public int getSafe(String[] param) {

        // init board
        board = new char[param.length][];
        for (int i = 0; i < param.length; ++i) {
            board[i] = param[i].toCharArray();
        }

        // iterate on board
        for (int x = 0; x < board.length; ++x) {
            for (int y = 0; y < board[x].length; ++y) {
                switch (board[x][y]) {
                case 'B':
                    for (int k = 0; k < bishDirs.length; ++k) {
                        Point dir = bishDirs[k];
                        Point f = new Point(x + dir.x, y + dir.y);
                        go(f, dir);
                    }
                    break;
                case 'R':
                    for (int k = 0; k < rookDirs.length; ++k) {
                        Point dir = rookDirs[k];
                        Point f = new Point(x + dir.x, y + dir.y);
                        go(f, dir);
                    }
                    break;
                case 'Q':
                    for (int k = 0; k < qDirs.length; ++k) {
                        Point dir = qDirs[k];
                        Point f = new Point(x + dir.x, y + dir.y);
                        go(f, dir);
                    }
                    break;
                case 'K':
                    for (int k = 0; k < knightmoves.length; ++k) {
                        Point move = knightmoves[k];
                        Point f = new Point(x + move.x, y + move.y);
                        if (isFree(f)) {
                            board[f.x][f.y] = 'X';
                        }
                    }
                    break;
                case 'P':
                    for (int k = 0; k < pawnmoves.length; ++k) {
                        Point move = pawnmoves[k];
                        Point f = new Point(x + move.x, y + move.y);
                        if (isFree(f)) {
                            board[f.x][f.y] = 'X';
                        }
                    }
                    break;
                default:
                    break;
                }
            }
        }
        return count();
    }

    boolean isFree(Point p) {
        if (p.x < 0 || p.x >= board.length || p.y < 0 || p.y >= board[0].length) {
            return false;
        }
        char c = board[p.x][p.y];
        return c == 'U' || c == 'X';
    }

    void go(Point f, Point dir) {
        if (isFree(f)) {
            board[f.x][f.y] = 'X';
            Point newF = new Point(f.x + dir.x, f.y + dir.y);
            go(newF, dir);
        }
    }

    int count() {
        int c = 0;
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] == 'U') {
                    c++;
                }
            }
        }
        return c;
    }

    public static void main(String[] args) {
        ChessCover cov = new ChessCover();
        String[] b = new String[] { "UUPUPUUUUB", "UUUKUUUPBU", "UPPUPUUUUU",
                "UPUQQUPUUU", "UPUQQUUUUU", "UPUUUQUPUU", "QUQUPUPRPU",
                "UUUUUUUUBB", "URRRRUPRPP", "UUUUUUUBUU" };
        System.out.println(cov.getSafe(b));
        for (int i=0;i<cov.board.length;++i) {
            System.out.println(cov.board[i]);
        }
    }
}