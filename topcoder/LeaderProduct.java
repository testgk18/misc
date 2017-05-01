// passed system test
/*
 * Problem Statement      ** IMPORTANT NOTE: You may only submit a solution to a
 * given problem one time. If you submit more than once for a given problem,
 * only your first submission will count. Please do not submit more than one
 * time for a given problem.***
 * 
 * In a sales business, there is often some leader product that drives sales. In
 * such a business, the total sales from one month to the next is often directly
 * linked to the price of the leader product. The lower the price of the leader
 * product, the higher the sales for the month. In this problem your task is to
 * write a program to detect products with this property. You will be given a
 * String[], prices, each element of which represents the prices of a number of
 * products for a given month. Each element of prices will contain N
 * single-space delimited integers, where N is the number of products. The ith
 * integer in each element of prices represents the price of the ith product for
 * one month. You will also be given a int[], sales, each element of which
 * represents the total sales for a month, where elements of sales and prices
 * with corresponding indices represent the same month. You should find all
 * products such that for every pair of months, if the price is lower one month
 * than the other, the sales are higher in the month when the price is lower.
 * You are to return a int[] sorted in ascending order, each element of which
 * represents the index (starting from 0) of such a product. Definition     
 * Class: LeaderProduct Method: getLeader Parameters: String[], int[] Returns:
 * int[] Method signature: int[] getLeader(String[] prices, int[] sales) (be
 * sure your method is public)     
 * 
 * Constraints - sales will contain between 2 and 50 elements, inclusive. -
 * sales and prices will each contain the same number of elements. - Each
 * element of prices will be formatted as a single-space delimited list of
 * positive integers without leading zeros. - Each element of prices will
 * contain the same number of integers. - Each element of sales will be between
 * 0 and 1,000,000,000. - Each integer in prices will be between 1 and
 * 1,000,000,000 (without the commas). Examples 0)
 * 
 *      {"5 10 76 48","4 9 49 50","4 5 67 61"} {184,305,1945} Returns: { 0, 1 }
 * Product 0 is more expensive in month 0 than in months 1 and 2, and there are
 * fewer sales in month 0 than in month 1 or month 2. Since the price of product
 * 0 is the same in months 1 and 2, it doesn't matter which of them has the
 * higher sales, so far as product 0 is concerned. The price of product 1
 * decreases from each month to the next, and the sales increase. 1)
 * 
 *      {"1 2 3 4","1 2 3 4","1 2 3 4","1 2 3 4"} {46,354,38,53} Returns: { 0,
 * 1, 2, 3 } The prices of the products are all constant. Therefore, it is
 * trivially true that "for every pair of months, if the price is lower one
 * month than the other, the sales are higher in the month when the price is
 * lower." 2)
 * 
 *      {"79 144 75 94 29 78 100 67","45 136 59 93 52 44 86 64","92 959 34 60 61
 * 80 39 67", "3 138 85 2 77 46 28 61","63 138 75 66 5 30 5 30","97 258 80 48
 * 100 4 3 30", "93 138 40 45 25 39 63 83","60 977 67 38 73 23 27 53","71 138 58
 * 100 38 75 46 75", "16 220 40 47 78 83 29 33","74 138 24 82 42 62 94 5","32
 * 137 12 40 12 80 2 68", "94 137 90 77 51 40 93 10","60 138 92 95 21 65 8
 * 95","79 137 3 37 85 5 83 59", "34 996 16 26 59 37 16 94","58 138 79 48 22 64
 * 92 63","74 143 40 55 54 11 54 93", "4 970 94 98 38 9 15 54","52 137 11 91 50
 * 17 57 43","23 138 90 64 45 81 69 42"}
 * {44,98,24,58,90,30,59,9,63,43,72,95,97,75,96,1,61,48,11,91,73} Returns: { 1 }
 * 
 * 
 * This problem statement is the exclusive and proprietary property of TopCoder,
 * Inc. Any unauthorized use or reproduction of this information without the
 * prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003,
 * TopCoder, Inc. All rights reserved.
 */

import java.util.StringTokenizer;
import java.util.Vector;

public class LeaderProduct {

    public int[] getLeader(String[] prices, int[] sales) {
        // parse products
        int[][] prods = new int[prices.length][];
        for (int i = 0; i < prices.length; ++i) {
            StringTokenizer tt = new StringTokenizer(prices[i]);
            prods[i] = new int[tt.countTokens()];
            int c = 0;
            while (tt.hasMoreTokens()) {
                prods[i][c++] = Integer.parseInt(tt.nextToken());
            }
        }

        // determine leader products
        Vector ret = new Vector();
        for (int i = 0; i < prods[0].length; ++i) {
            boolean bo = true;
            for (int j = 0; j < prods.length; ++j) {
                for (int k = j + 1; k < prods.length; ++k) {
                    int a = prods[j][i] - prods[k][i];
                    int b = sales[j] - sales[k];
                    if ((a > 0 && b >= 0) || (a < 0 && b <= 0)) {
                        bo = false;
                        break;
                    }
                }
            }
            if (bo) {
                ret.add(new Integer(i));
            }
        }
        int[] ro = new int[ret.size()];
        for (int i = 0; i < ret.size(); ++i) {
            ro[i] = ((Integer) ret.get(i)).intValue();
        }
        return ro;
    }

    public static void main(String[] args) {
        LeaderProduct le = new LeaderProduct();
        String[] prices = { "5 10 76 48", "4 9 49 50", "4 5 67 61" };
        int[] sales = { 184, 305, 1945 };
        int[] result = le.getLeader(prices, sales);
        for (int i = 0; i < result.length; ++i) {
            System.out.print(result[i] + ", ");
        }
        System.out.println();
    }
}