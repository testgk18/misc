// passed system test
/*
 * Problem Statement      You will be given a formula in the following form:
 * aw+bx=cy+dz, where a, b, c and d are positive integers between 1 and 9
 * inclusive. You will also be given positive values for 3 of w, x, y, and z.
 * The remaining value will be -1, denoting a blank. Return what value the blank
 * must have (possibly 0 or negative) in order to satisfy the given formula.
 * Definition      Class: LinearCombinations Method: computeValue Parameters:
 * String, int, int, int, int Returns: double Method signature: double
 * computeValue(String formula, int w, int x, int y, int z) (be sure your method
 * is public)     
 * 
 * Notes - The returned value must be accurate to within a relative or absolute
 * value of 1E-9. Constraints - formula will contain exactly 11 characters. -
 * formula will have the form (quotes for clarity) "aw+bx=cy+dz" where a,b,c,d
 * are positive digits ('1'-'9'). - Exactly three of w, x, y, and z will be
 * between 1 and 10000 inclusive. - Exactly one of w, x, y, and z will be -1.
 * Examples 0)
 * 
 *      "2w+3x=4y+5z" -1 1 2 3 Returns: 10.0 2w+3(1)=4(2)+5(3) so 2w + 3 = 23.
 * 1)
 * 
 *      "1w+1x=1y+1z" 1 1 1 -1 Returns: 1.0 1(1) + 1(1) = 1(1) + 1z so 2 = 1 +
 * z. 2)
 * 
 *      "9w+9x=8y+8z" 9999 9996 3 -1 Returns: 22491.375
 * 
 * 3)
 * 
 *      "9w+8x=1y+9z" 729 4096 1 -1 Returns: 4369.777777777777
 * 
 * This problem statement is the exclusive and proprietary property of TopCoder,
 * Inc. Any unauthorized use or reproduction of this information without the
 * prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003,
 * TopCoder, Inc. All rights reserved.
 */

import java.util.StringTokenizer;

public class LinearCombinations {

    double a, b, c, d;

    public double computeValue(String formula, int w, int x, int y, int z) {

        StringTokenizer tt = new StringTokenizer(formula, "wxyz+=");
        a = (double) Integer.parseInt(tt.nextToken());
        b = (double) Integer.parseInt(tt.nextToken());
        c = (double) Integer.parseInt(tt.nextToken());
        d = (double) Integer.parseInt(tt.nextToken());

        if (w == -1) {
            return (y * c + z * d - x * b) / a;
        }
        if (x == -1) {
            return (y * c + z * d - w * a) / b;
        }
        if (y == -1) {
            return (w * a + x * b - z * d) / c;
        }
        if (z == -1) {
            return (w * a + x * b - y * c) / d;
        }
        System.out.println("err");
        return 0;
    }

    public static void main(String[] args) {
        LinearCombinations inst = new LinearCombinations();
    }
}