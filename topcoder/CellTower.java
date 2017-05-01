// passed system test
/*
 * Problem Statement      There are often a number of cell towers that are
 * within range of a cell phone. Your task is to find the best cell tower for a
 * cell phone at a particular location. You will be given a String[], towers,
 * each of whose elements is formatted as (quotes for clarity) "( <x>, <y>)",
 * where <x> and <y> are decimal numbers. You will also be given two ints, x and
 * y, representing the location of the cell phone. Your method should return the
 * index of the best cell tower, where best is defined as follows. First, you
 * should find the distance to the closest cell tower, and then you should find
 * all of the towers that are at most 2 units farther out than that one. Of
 * these, you should return the index (starting from 0) of the one with the
 * lowest index. The idea behind this is that the towers have already been
 * ranked, with lower indexed towers being better in some way, so if one of the
 * lower indexed towers is almost the closest, it would be preferable to use it
 * over the true closest. Definition      Class: CellTower Method: best
 * Parameters: String[], int, int Returns: int Method signature: int
 * best(String[] towers, int x, int y) (be sure your method is public)     
 * 
 * Constraints - towers will contain between 1 and 50 elements, inclusive. -
 * Each element of towers will be formatted as "( <x>, <y>)". - Each element of
 * towers will have between 5 and 50 characters, inclusive. - Each <x> and <y>
 * in towers will represent a decimal number between -1000.0 and 1000.0,
 * inclusive (possibly with extra leading 0's and extra trailing 0's after the
 * decimal point). - Each decimal number will be formatted as an optional minus
 * sign, followed by a sequence of one or more digits, which may contain a
 * decimal point. However, if there is a decimal point, there will be at least
 * one digit both before and after the decimal point. - x and y will be between
 * -1000 and 1000, inclusive. - To avoid rounding errors there will be no towers
 * that are within 1e-3 units of 2 units farther than the closest tower.
 * Examples 0)
 * 
 *      {"(5.3,4.9)","(1.2,3.7)"} 3 3 Returns: 0 The first tower is about 2.98
 * away from (3,3). The second tower is about 1.93 away from (3,3). So, the
 * closest tower is 1.93 away. Since the first tower has a lower index, and its
 * distance is less than 2 more than the closest distance, it is the best, and
 * we return its index, 0. 1)
 * 
 *      {"(-43.54,632.5331)","(43.53,632.5332)","(-652.23000,00.000)"} 30 532
 * Returns: 1 The second tower is the closest one here, and is more than 2 units
 * closer than any other tower. 2)
 * 
 *     
 * {"(432,126)","(429,131)","(440,121)","(433,129)","(435,123)","(436,119)","(450,120)"}
 * 437 121 Returns: 2
 * 
 * This problem statement is the exclusive and proprietary property of TopCoder,
 * Inc. Any unauthorized use or reproduction of this information without the
 * prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003,
 * TopCoder, Inc. All rights reserved.
 */

import java.awt.geom.Point2D;
import java.util.StringTokenizer;

public class CellTower {

    public int best(String[] towers, int x, int y) {

        float[] distances = new float[towers.length];
        int bestindex = 0;
        float bestvalue = computedistance(toPoint(towers[0]), x, y);
        distances[0] = bestvalue;

        for (int i = 1; i < towers.length; ++i) {
            distances[i] = computedistance(toPoint(towers[i]), x, y);
            if (distances[i] < bestvalue) {
                bestvalue = distances[i];
                bestindex = i;
            }
        }
        bestvalue = (float) Math.sqrt(bestvalue);
        int retour = bestindex;

        for (int i = bestindex - 1; i >= 0; --i) {
            float f = (float) Math.sqrt(distances[i]);
            if (f < bestvalue + 2) {
                retour = i;
            }
        }
        return retour;
    }

    private float computedistance(Point2D.Float p, int x, int y) {
        return (p.x - x) * (p.x - x) + (p.y - y) * (p.y - y);
    }

    private Point2D.Float toPoint(String tower) {
        Point2D.Float retour = new Point2D.Float();
        boolean b = false;
        StringTokenizer tokenizer = new StringTokenizer(tower, "(),");
        while (tokenizer.hasMoreTokens()) {
            String s = tokenizer.nextToken();
            if (s.length() > 0) {
                if (b) {
                    retour.y = Float.parseFloat(s);
                } else {
                    retour.x = Float.parseFloat(s);
                }
                b = true;
            }
        }
        return retour;
    }
}