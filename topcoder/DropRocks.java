// passed system test
/*
 * Problem Statement      In this problem we are going to model the ripples
 * formed when you drop rocks in the water. When a rock is dropped in the water
 * large ripples form around the rock that gradually decrease as they move away.
 * Let's say you drop a 4 pound rock in the water at time 0 at location (5,3).
 * At time 0 there will be a level 4 ripple where the rock was dropped. At time
 * 1 there will be a level 3 ripple surrounding the rock. At time 2 there will
 * be a level 2 ripple surrounding where the level 3 ripple was. This goes on
 * until time 4 where there will no longer be a ripple: Input format: Weight
 * Time X Y rocks = {"4 0 5 3"} Time 0 Time 1 Time 2 Time 3 Time 4 0123456789
 * 0123456789 0123456789 0123456789 0123456789 0.......... 0..........
 * 0.......... 0..1111111. 0.......... 1.......... 1.......... 1...22222..
 * 1..1.....1. 1.......... 2.......... 2....333... 2...2...2.. 2..1.....1.
 * 2.......... 3.....4.... 3....3.3... 3...2...2.. 3..1.....1. 3..........
 * 4.......... 4....333... 4...2...2.. 4..1.....1. 4.......... 5..........
 * 5.......... 5...22222.. 5..1.....1. 5.......... 6.......... 6..........
 * 6.......... 6..1111111. 6.......... When many ripples occupy the same
 * location at a particular time, the level of the ripple at that location is
 * the sum of all the ripples occupying that location: rocks = {"3 0 3 2","2 1 7
 * 2","3 0 3 4","1 2 8 5"} Time 0 Time 1 Time 2 Time 3 0123456789 0123456789
 * 0123456789 0123456789 0.......... 0.......... 0.11111.... 0..........
 * 1.......... 1..222..... 1.1...1111. 1.......... 2...3...... 2..2.2..2..
 * 2.211121.1. 2.......... 3.......... 3..444..... 3.2...2111. 3..........
 * 4...3...... 4..2.2..... 4.21112.... 4.......... 5.......... 5..222.....
 * 5.1...1..1. 5.......... 6.......... 6.......... 6.11111.... 6.......... Note
 * that the ripples maintain their path even if they collide. Given the weight,
 * times, and locations of the dropped rocks, determine the highest ripple level
 * that will occur before all of the ripples have died out. Disregard locations
 * that have negative coordinates when checking for ripple levels. In both of
 * the examples above, the highest ripple value was 4. Definition      Class:
 * DropRocks Method: maxWave Parameters: String[] Returns: int Method signature:
 * int maxWave(String[] rocks) (be sure your method is public)     
 * 
 * Constraints - rocks will contain between 1 and 50 elements inclusive. - Each
 * element of rocks will be of the form (quotes for clarity): "WEIGHT TIME X Y"
 * where: WEIGHT is an integer with no leading zeros between 1 and 250
 * inclusive. TIME is an integer with no leading zeros between 0 and 1000
 * inclusive. X is an integer with no leading zeros between 0 and 2000
 * inclusive. Y is an integer with no leading zeros between 0 and 2000
 * inclusive. - Each element of rocks will not contain leading or trailing
 * whitespace. Examples 0)
 * 
 *      {"4 0 5 3"} Returns: 4 From above. 1)
 * 
 *      {"3 0 3 2","2 1 7 2","3 0 3 4","1 2 8 5"} Returns: 4 From above. 2)
 * 
 *      {"1 0 0 0","1 1 0 0","1 2 0 0","1 3 0 0","1 4 0 0","1 5 0 0"} Returns: 1
 * 
 * 3)
 * 
 *      {"250 0 0 0","250 0 100 100"} Returns: 400
 * 
 * This problem statement is the exclusive and proprietary property of TopCoder,
 * Inc. Any unauthorized use or reproduction of this information without the
 * prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003,
 * TopCoder, Inc. All rights reserved.
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;

public class DropRocks {

    int SIZE = 2001;

    int OFFSET = 250;

    int maxWave = 0;

    Rock[] rocks;

    int[][] lake = new int[SIZE + 2 * OFFSET][SIZE + 2 * OFFSET];

    public int maxWave(String[] rockies) {

        // init waves
        rocks = new Rock[rockies.length];
        for (int i = 0; i < rockies.length; ++i) {
            rocks[i] = new Rock(rockies[i]);
        }

        // init timeframe
        int mintime = Integer.MAX_VALUE;
        int maxtime = 0;
        for (int i = 0; i < rocks.length; ++i) {
            mintime = Math.min(mintime, rocks[i].time);
            maxtime = Math.max(maxtime, rocks[i].time + rocks[i].weight);
        }

        // simulate
        for (int i = mintime; i <= maxtime; ++i) {
            for (int j = 0; j < rocks.length; ++j) {
                rocks[j].modify(i, 1);
            }
            for (int j = 0; j < rocks.length; ++j) {
                rocks[j].modify(i, -1);
            }
        }
        return maxWave;
    }

    public static void main(String[] args) {
        DropRocks rr = new DropRocks();
        String[] rocks = { "100 100 0 0", "100 100 0 10", "100 90 10 5" };
        System.out.println(rr.maxWave(rocks));
    }

    class Rock {
        int weight, time, x, y;

        Rock(String s) {
            StringTokenizer tt = new StringTokenizer(s);
            weight = Integer.parseInt(tt.nextToken());
            time = Integer.parseInt(tt.nextToken());
            x = Integer.parseInt(tt.nextToken());
            y = Integer.parseInt(tt.nextToken());
        }

        void modify(int t, int upordown) {
            if (t < time) {
                return;
            }
            if (t == time) {
                lake[x][y] += (weight * upordown);
                maxWave = Math.max(maxWave, lake[x][y]);
                return;
            }
            if (t - time >= weight) {
                return;
            }
            int dist = t - time;
            int w = (weight - dist) * upordown;
            int xx, yy;
            for (int i = -dist; i < dist; ++i) {
                xx = x + i;
                yy = y - dist;
                if (xx >= 0 && yy >= 0) {
                    lake[xx][yy] += w;
                    maxWave = Math.max(maxWave, lake[xx][yy]);
                }
                xx = x + i + 1;
                yy = y + dist;
                if (xx >= 0 && yy >= 0) {
                    lake[xx][yy] += w;
                    maxWave = Math.max(maxWave, lake[xx][yy]);
                }
                xx = x - dist;
                yy = y + i + 1;
                if (xx >= 0 && yy >= 0) {
                    lake[xx][yy] += w;
                    maxWave = Math.max(maxWave, lake[xx][yy]);
                }
                xx = x + dist;
                yy = y + i;
                if (xx >= 0 && yy >= 0) {
                    lake[xx][yy] += w;
                    maxWave = Math.max(maxWave, lake[xx][yy]);
                }
            }
            return;
        }
    }
}