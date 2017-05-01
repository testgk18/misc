// passed system test
/*
 * Problem Statement      With all the different cell phone plans being offered
 * these days, it is often difficult to pick the one that provides the best
 * values for a particular usage pattern. In this problem, we will consider cell
 * phone plans that provide a fixed number of minutes each month for a certain
 * fee. If you go over the fixed number of minutes, you must pay an additional
 * fee per minute. Additionally, some plans offer free off hour (night and
 * weekend) calls, while other plans treat off hour calls the same as peak hour
 * calls. If a plan offers free off hour calling, then the calls made during off
 * hours don't use up the fixed number of minutes you get each month. You will
 * be given a String[], plans, each element of which represents a cell phone
 * plan. Each plan will be formatted as " <PRICE> <MINUTES> <COST PER MINUTE>
 * <OFF>" (quotes and angle brackets for clarity only). <PRICE> will be an
 * integer representing the number of cents per month that one must pay for the
 * plan. <MINUTES> will represent the number of free minutes the plan provides,
 * per month. <COST PER MINUTE> will represent the price in cents of each
 * additional minute in a month, beyond the first <MINUTES>. <OFF> will be
 * either 'T' or 'F', representing whether off hour calls are free ('T') or not
 * ('F'). Additionally, you will be given two int[]s. peakMinutes will represent
 * the number of minutes spent talking during peak hours, while offMinutes will
 * represent the number of minutes spent talking during off hours. Corresponding
 * elements of peakMinutes and offMinutes will represent a single month of use.
 * Your task is to return the index (starting from 0) of the cheapest plan over
 * all the months given as input. If there is a tie, return the lowest index
 * among tied plans. Definition      Class: CellPlans Method: cheapest
 * Parameters: String[], int[], int[] Returns: int Method signature: int
 * cheapest(String[] plans, int[] peakMinutes, int[] offMinutes) (be sure your
 * method is public)     
 * 
 * Constraints - plans will contain between 1 and 50 elements, inclusive. - Each
 * element of plans will be formatted as " <PRICE> <MINUTES> <COST PER MINUTE>
 * <OFF>". - <PRICE> will be between 0 and 100000, inclusive, with no extraneous
 * leading zeros. - <MINUTES> will be between 0 and 50000, inclusive, with no
 * extraneous leading zeros. - <COST PER MINUTE> will be between 0 and 1000,
 * inclusive, with no extraneous leading zeros. - <OFF> will be either 'T' or
 * 'F'. - peakMinutes will contain between 1 and 50 elements, inclusive. -
 * offMinutes will contain the same number of elements as peakMinutes. - Each
 * element of offMinutes and peakMinutes will be between 0 and 25000, inclusive.
 * Examples 0)
 * 
 *      {"2999 1000 29 T", "5999 3000 49 F", "999 0 19 F"}
 * {1543,463,754,405,0,30} {100,2053,1003,534,2595,3056} Returns: 0 Plan 0
 * provides 1000 free minutes, and free off hour calling. For the 6 months
 * given, we must pay the base rate (2999) each month for a total of 17994.
 * Additionally, we used an extra 543 minutes during the first month, for which
 * we must pay 543*29 = 15747. 15747 + 17994 = 33741. Plan 1 would end up
 * costing 40208, while plan 2 would end up costing 244178. 1)
 * 
 *      {"10000 0 0 T","10000 0 0 F"} {20000,25000} {25000,20000} Returns: 0
 * These plans always cost the same amount since it doesn't matter whether you
 * get free off hour calling when the calls are already free. Since there is a
 * tie, return the lower index. 2)
 * 
 *      {"100000 0 1000 F","100000 0 10 F"}
 * {25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,
 * 25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,
 * 25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,
 * 25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,
 * 25000,25000,25000,25000,25000,25000}
 * {25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,
 * 25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,
 * 25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,
 * 25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,25000,
 * 25000,25000,25000,25000,25000,25000} Returns: 1 Beware of overflow. This
 * problem statement is the exclusive and proprietary property of TopCoder, Inc.
 * Any unauthorized use or reproduction of this information without the prior
 * written consent of TopCoder, Inc. is strictly prohibited. (c)2003, TopCoder,
 * Inc. All rights reserved.
 */

import java.util.StringTokenizer;

public class CellPlans {

    //int[] res;

    public int cheapest(String[] plans, int[] peakMinutes, int[] offMinutes) {

        //res = new int[plans.length];
        int bestind = 0;
        long bestval = Long.MAX_VALUE;
        for (int i = 0; i < plans.length; ++i) {
            long thisval = 0;
            StringTokenizer tt = new StringTokenizer(plans[i]);
            int price = Integer.parseInt(tt.nextToken());
            int minut = Integer.parseInt(tt.nextToken());
            int costper = Integer.parseInt(tt.nextToken());
            char off = tt.nextToken().charAt(0);

            for (int j = 0; j < peakMinutes.length; ++j) {
                int tot = peakMinutes[j];
                if (off == 'F') {
                    tot += offMinutes[j];
                }
                thisval += (price + Math.max(0, (tot - minut) * costper));
                //System.out.println(thisval);
            }

            if (thisval < bestval) {
                bestval = thisval;
                bestind = i;
            }
        }
        return bestind;
    }

    public static void main(String[] args) {
        CellPlans inst = new CellPlans();
        //inst.cheapest()
    }
}