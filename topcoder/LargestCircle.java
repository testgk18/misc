// Passed System Test
// BEGIN CUT HERE
// PROBLEM STATEMENT
// 
// NOTE: This problem statement contains images that may not display properly if
// viewed outside of the applet.
// 
// 
// 
// Given a regular square grid, with some number of squares marked, find
// the largest circle you can draw on the grid that does not pass through
// any of the marked squares. The circle must be centered on a grid point
// (the corner of a square) and the radius must be an integer.
// Return the radius of the circle.
// 
// 
// The grid will be given as a String[], with each character
// representing a square. A '.' represents an empty square, and a '#'
// represents a marked square.
// 
// 
// The circle may intersect the corner of a marked square, but may not pass
// through the interior. For example, given the grid:
// 
// 
// 
//   { "############",
//     "###......###",
//     "##.######.##",
//     "#.########.#",
//     "#.##..####.#",
//     "#.##..####.#",
//     "#.########.#",
//     "#.########.#",
//     "#.########.#",
//     "##.######.##",
//     "###......###",
//     "############" }
// 
// 
// two circles can be drawn with radii 1 and 5, as shown below:
// 
// 
// 
// 
// 
// 
// 
// Therefore, your method should return 5.
// 
// 
// Circles may not extend outside of the grid, and must have a radius of
// at least 1. If no such circle exists, return 0.
// 
// 
// DEFINITION
// Class:LargestCircle
// Method:radius
// Parameters:String[]
// Returns:int
// Method signature:int radius(String[] grid)
// 
// 
// CONSTRAINTS
// -grid will contain between 1 and 50 elements, inclusive.
// -Each element of grid will contain between 1 and 50 characters, inclusive.
// -Each element of grid will contain the same number of characters.
// -Each element of grid will contain only the characters '.' and '#'.
// 
// 
// EXAMPLES
// 
// 0)
// { "####",
//   "#..#",
//   "#..#",
//   "####" }
// 
// Returns: 1
// 
// Only one circle fits in this grid -- a circle of radius 1, in the center of
// the grid.
// 
// 1)
// { "############",
//   "###......###",
//   "##.######.##",
//   "#.########.#",
//   "#.##..####.#",
//   "#.##..####.#",
//   "#.########.#",
//   "#.########.#",
//   "#.########.#",
//   "##.######.##",
//   "###......###",
//   "############" }
// 
// Returns: 5
// 
// This is the example from the problem statement.
// 
// 2)
// { ".........." }
// 
// Returns: 0
// 
// The grid must be at least two squares wide and tall in order for any circles
// to fit.
// 
// 3)
// { "#######",
//   "#######",
//   "#######",
//   "#######",
//   "#######" }
// 
// Returns: 0
// 
// 4)
// { "#####.......",
//   "#####.......",
//   "#####.......",
//   "............",
//   "............",
//   "............",
//   "............",
//   "............",
//   "............",
//   "............" }
// 
// Returns: 4
// 
// A circle of radius 4 fits in this grid, as shown here:
// 
// 
// 
// 
// 5)
// { "#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.",
//   "...#...#...#...#...#...#...#...#...#...#...#...#..",
//   "#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.",
//   ".#...#...#...#...#...#...#...#...#...#...#...#...#",
//   "#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.",
//   "...#...#...#...#...#...#...#...#...#...#...#...#..",
//   "#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.",
//   ".#...#...#...#...#...#...#...#...#...#...#...#...#",
//   "#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.",
//   "...#...#...#...#...#...#...#...#...#...#...#...#.#",
//   "#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#.#." }
// 
// 
// Returns: 0
// 
// 6)
// { ".........................#........................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   "..................................................",
//   ".................................................." }
// 
// 
// Returns: 24
// 
// END CUT HERE

import java.awt.Point;
import java.util.Vector;

public class LargestCircle {

    public int radius(String[] grid) {
        Vector pats = new Vector();
        for (int i = 1; i <= 25; ++i) {
            Point[] cache = new Point[50];
            int c = 0;
            for (double y = 0.01; y < i; y += 0.01) {
                double x = Math.sqrt(i * i - y * y);
                Point p = new Point((int) x, (int) y);
                //System.out.println(p);
                if (c == 0 || !cache[c - 1].equals(p)) {
                    cache[c++] = p;
                }
            }
            Point[] patterns = new Point[c * 4];
            for (int j = 0; j < c; ++j) {
                patterns[j * 4] = cache[j];
                patterns[j * 4 + 1] = new Point(cache[j]);
                patterns[j * 4 + 1].x = -patterns[j * 4 + 1].x - 1;

                patterns[j * 4 + 2] = new Point(cache[j]);
                patterns[j * 4 + 2].y = -patterns[j * 4 + 2].y - 1;

                patterns[j * 4 + 3] = new Point(cache[j]);
                patterns[j * 4 + 3].x = -patterns[j * 4 + 3].x - 1;
                patterns[j * 4 + 3].y = -patterns[j * 4 + 3].y - 1;
            }

            for (int k = 0; k < patterns.length; ++k) {
                //System.out.println(patterns[k]);
            }
            //System.out.println("bbbb");

            pats.add(patterns);
        }

        System.out.println("start searching...");

        int max = Math.min(grid[0].length() / 2, grid.length / 2);
        for (int i = max; i >= 1; --i) {
            System.out.println(i);
            for (int x = i; x <= grid[0].length() - i; ++x) {
                for (int y = i; y <= grid.length - i; ++y) {
                    Point[] po = (Point[]) pats.get(i - 1);
                    boolean ok = true;
                    for (int j = 0; j < po.length; ++j) {
                        int a = po[j].x + x;
                        int b = po[j].y + y;
                        if (a >= grid[0].length() || b >= grid.length) {
                            ok = false;
                            break;
                        }
                        char c = grid[b].charAt(a);
                        if (c == '#') {
                            ok = false;
                            break;
                        }
                    }

                    if (ok) {
                        return i;
                    }
                }

            }
        }
        return 0;

    }

    public static void main(String[] args) {
        LargestCircle ll = new LargestCircle();
        int f = ll
                .radius(new String[] { "#####.......", "#####.......",
                        "#####.......", "............", "............",
                        "............", "............", "............",
                        "............", "............" });
        System.out.println("result: " + f);
    }
}