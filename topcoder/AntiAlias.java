// passed system test
/*
 * Problem Statement      Anti-aliasing is an important technique in most
 * graphics work. In this problem, we will be considering anti-aliasing only in
 * the context of filling rectangles. It is simple to fill a rectangle if its
 * edges are along the boundaries between pixels. However, what if we want to
 * make a rectangle whose edges fall in the middle of pixels? In this case, we
 * want to change the color of only a portion of a pixel, which isn't possible.
 * To compromise, we start by imagining that we could change the color of a
 * portion of a pixel, and do so. Now, we have a single pixel with multiple
 * colors in it. To get back to a pixel with a single color, we take an average
 * of the pixel's colors, weighted by the areas of the pixel those colors cover.
 * For example, let's say we start with a white background and want to draw a
 * black rectangle from (0.45,0.45) to (1.21,1.21). This rectangle covers
 * portions of pixels (0,0), (0,1), (1,0) and (1,1). If we work out the math, we
 * find that it covers 0.3025 of (0,0), 0.1155 of (0,1) and (1,0), and 0.0441 of
 * (1,1). If we let black have a value of 0, and white have a value of 50, then
 * the weighted averages give us values of 34.875, 44.225, 44.225, and 47.795
 * for (0,0), (0,1), (1,0) and (1,1), respectively. We then round them to the
 * nearest integer (you needn't worry about rounding .5, due to the constraints)
 * to get 35, 44, 44, and 48. You will be given a String[], bitmap, where
 * character bitmap[y][x] represents the color of pixel (x,y). 'A' represents a
 * pixel of color 0, and in general a character ch has a color value of ch-'A'.
 * You will also be given a String, rect, formatted as "X1 Y1 X2 Y2 COLOR".
 * (X1,Y1) and (X2,Y2) will each represent valid coordinates within the bitmap,
 * and COLOR will be a single character with the same meaning as in bitmap. You
 * are to return a String[] in the same format as bitmap, which represents the
 * image with the rectangle drawn on it. Definition      Class: AntiAlias
 * Method: fillRect Parameters: String[], String Returns: String[] Method
 * signature: String[] fillRect(String[] bitmap, String rect) (be sure your
 * method is public)     
 * 
 * Constraints - bitmap will contain between 1 and 50 elements, inclusive. -
 * Each element of bitmap will contain the same number of characters, between 1
 * and 50, inclusive. - Each character in bitmap will have an ASCII value
 * between 'A' and ('A'+50), inclusive. - rect will be formatted as "X1 Y1 X2 Y2
 * COLOR". - rect will contain between 9 and 50 characters, inclusive. - X1 and
 * X2 will be between 0 and the number of characters in each element of bitmap,
 * inclusive. - X2 will be greater than X1. - Y1 and Y2 will be between 0 and
 * the number of elements in bitmap, inclusive. - Y2 will be greater than Y1. -
 * COLOR will be a single character with an ASCII value between 'A' and
 * ('A'+50), inclusive. - X1, Y1, X2, and Y2 will all be formatted as one or
 * more digits, optionally followed by a decimal point and one or more digits.
 * There may be leading or trailing zeros. - The value of each pixel, prior to
 * rounding, will not be within 1e-6 of x+0.5 for any integer x. Examples 0)
 * 
 *      {"sssss", "sssss", "sssss", "sssss", "sssss"} "0.45 0.45 1.21 1.21 A"
 * Returns: { "dmsss", "mqsss", "sssss", "sssss", "sssss" } This is the example
 * from the statement. 's' corresponds to color 50 (white), while 'd', 'm' and
 * 'q' correspond to 35, 44, and 48, respectively. We start by putting a
 * rectangle in, as shown below. Then we take the average colors of the pixels
 * to get this: 1)
 * 
 *      {"sssss", "sssss", "sssss", "sssss", "sssss"} "0.451 0.451 4.201 4.201
 * A" Returns: { "dXXXm", "XAAAi", "XAAAi", "XAAAi", "miiiq" }
 * 
 * 2)
 * 
 *      {"ABCDEF", "GHIJKL", "MNOPQR", "STUVWX", "ZYabcd", "efghij"} "0.901
 * 0.901 5.101 5.101 s" Returns: { "AGHIJF", "KssssP", "QssssU", "Vssss[",
 * "\\ssssf", "eghijj" } The double backslash in the return represents a single
 * backslash (it is escaped). This problem statement is the exclusive and
 * proprietary property of TopCoder, Inc. Any unauthorized use or reproduction
 * of this information without the prior written consent of TopCoder, Inc. is
 * strictly prohibited. (c)2003, TopCoder, Inc. All rights reserved.
 */

import java.util.StringTokenizer;

public class AntiAlias {

    public String[] fillRect(String[] bitmap, String rect) {

        double[][] bi = new double[bitmap.length][bitmap[0].length()];
        for (int i = 0; i < bitmap.length; ++i) {
            char[] c = bitmap[i].toCharArray();
            for (int j = 0; j < c.length; ++j) {
                bi[i][j] = c[j];
            }
        }
        StringTokenizer tt = new StringTokenizer(rect);
        double x1 = Double.parseDouble(tt.nextToken());
        double y1 = Double.parseDouble(tt.nextToken());
        double x2 = Double.parseDouble(tt.nextToken());
        double y2 = Double.parseDouble(tt.nextToken());
        double color = tt.nextToken().charAt(0);
        System.out.println(color);

        for (int i = (int) Math.floor(x1); i < Math.ceil(x2); ++i) {
            for (int j = (int) Math.floor(y1); j < Math.ceil(y2); ++j) {
                bi[j][i] += (color - bi[j][i])
                        * (Math.min(i + 1, x2) - Math.max(i, x1))
                        * (Math.min(j + 1, y2) - Math.max(j, y1));
            }
        }
        for (int i = 0; i < bitmap.length; ++i) {
            bitmap[i] = "";
            for (int j = 0; j < bi[0].length; ++j) {
                bitmap[i] += (char) Math.round(bi[i][j]);
            }
        }
        return bitmap;
    }

    public static void main(String[] args) {
        AntiAlias inst = new AntiAlias();
        String[] map = new String[] { "sssss", "sssss", "sssss", "sssss" };
        String rect = "0.45 0.45 1.21 1.21 A";
        String[] result = inst.fillRect(map, rect);
        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[2]);
        System.out.println(result[3]);
        //System.out.println(result[4]);
    }
}