// Passed system test
/*
 * Problem Statement      Your function will be fed a String[] screen containing
 * the output of a radar device. screen will contain exactly one 'Y' and one or
 * more 'T's. The remaining characters are all periods ('.'). 'Y' designates
 * your position on the radar. 'T' designates an enemy position. You will return
 * a int[] containing the relative distances from your position to each enemy in
 * screen. The relative distance is the absolute difference in rows added to the
 * absolute difference in columns. Here absolute difference means take the
 * absolute value of the difference. For example, if screen = {"....T...",
 * "Y.......", ".......T"} then the higher 'T' has relative distance 5 (1 row, 4
 * columns) and the lower 'T' has relative distance 8 (1 row, 7 columns).
 * Enemies that occur earlier in screen should occur earlier in the return
 * value. If two enemies are in the same element of screen, the enemy that
 * occurs earlier in that element should occur earlier. Definition      Class:
 * RelativeCoordinates Method: getCoords Parameters: String[] Returns: int[]
 * Method signature: int[] getCoords(String[] screen) (be sure your method is
 * public)     
 * 
 * Constraints - screen must contain between 1 and 50 elements inclusive. - Each
 * element of screen must contain between 1 and 50 characters inclusive. - Each
 * element of screen must contain the same number of characters. - Each
 * character in screen will be (quotes for clarity) '.', 'T', or 'Y'. - screen
 * will contain exactly one 'Y'. - screen will contain between 1 and 100 'T'
 * characters inclusive. Examples 0)
 * 
 *      {"....T...", "Y.......", ".......T"} Returns: { 5, 8 } From above. 1)
 * 
 *      {"YTTTTTTTTTT"} Returns: { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }
 * 
 * 2)
 * 
 *      {"T" ,"T" ,"T" ,"T" ,"T" ,"T" ,"Y"} Returns: { 6, 5, 4, 3, 2, 1 }
 * 
 * 3)
 * 
 *      {"Y................................"
 * ,"................................." ,"................................."
 * ,"................................." ,"................................."
 * ,"................................." ,"................................."
 * ,"................................T"} Returns: { 39 }
 * 
 * 4)
 * 
 *      {"TY"} Returns: { 1 }
 * 
 * This problem statement is the exclusive and proprietary property of TopCoder,
 * Inc. Any unauthorized use or reproduction of this information without the
 * prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003,
 * TopCoder, Inc. All rights reserved.
 */

import java.util.Vector;

public class RelativeCoordinates {

    public int[] getCoords(String[] screen) {
        int x = 0, y = 0;
        char[][] c = new char[screen.length][screen[0].length()];
        for (int i = 0; i < screen.length; ++i) {
            c[i] = screen[i].toCharArray();
            if (screen[i].indexOf('Y') >= 0) {
                y = i;
                x = screen[i].indexOf('Y');
            }
        }
        Vector v = new Vector();
        for (int i = 0; i < screen.length; ++i) {
            for (int j = 0; j < screen[0].length(); ++j) {
                if (c[i][j] == 'T') {
                    v.add(new Integer(Math.abs(i - y) + Math.abs(j - x)));
                }
            }
        }
        int[] r = new int[v.size()];
        for (int i = 0; i < r.length; ++i) {
            r[i] = ((Integer) v.get(i)).intValue();
        }
        return r;
    }

    public static void main(String[] args) {
        RelativeCoordinates coo = new RelativeCoordinates();
        coo.getCoords(new String[] { "....T...", "Y.......", ".......T" });
    }
}