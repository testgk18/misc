// passed system test
/*
 * Problem Statement
     
 *** IMPORTANT NOTE: You may only submit a solution to a given problem one time. If you submit more than once for a given problem, only your first submission will count. Please do not submit more than one time for a given problem.***  Your company is working on a new transmission for a high-performance vehicle. The transmission will have two sets of gears, a front set and a back set. The numbers of teeth on each of the front gears has already been set, but now you need to figure out how many teeth to put on all of the back gears. It has already been decided that there will be cnt back gears.  If the front set of gears contains N gears, then there are a total of N*cnt gear ratios. Each gear ratio is determined by the ratio of the number of teeth on one of the front gears divided by the number of teeth on one of the back gears. Your task is to determine how many teeth to put on each of the cnt back gears in such a way that the largest gear has max teeth, and the smallest gear has min teeth. Furthermore, if all of the possible gear ratios from your assignment are sorted, you want the maximum difference between two adjacent ratios in the sorted list to be as small as possible. You are to return the largest difference between adjacent ratios in this sorted list.
 Definition
     
 Class:
 Transmission
 Method:
 gears
 Parameters:
 int[], int, int, int
 Returns:
 double
 Method signature:
 double gears(int[] front, int min, int max, int cnt)
 (be sure your method is public)
     

 Notes
 -
 Each gear must have an integral number of teeth.
 Constraints
 -
 cnt will be between 3 and 5, inclusive.
 -
 min will be between 5 and 100, inclusive.
 -
 max will be between min+cnt-1 and 100, inclusive.
 -
 front will contain between 1 and 5 elements, inclusive.
 -
 Each element of front will be between 5 and 100, inclusive.
 Examples
 0)

     
 {10,50,10}
 5
 20
 3
 Returns: 3.75
 The best we can do here is to make the back 3 gears have 5, 8 and 20 teeth. This gives the following list of ratios: {0.50, 1.25, 2.00, 2.50, 6.25, 10.00} The biggest gap is between 10 and 6.25, so we return 3.75.
 1)

     
 {10,80}
 10
 50
 5
 Returns: 1.7333333333333334

 2)

     
 {5,9,30,100}
 10
 100
 5
 Returns: 1.904761904761905

 
 This problem statement is the exclusive and proprietary property of TopCoder, Inc. Any unauthorized use or reproduction of this information without the prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003, TopCoder, Inc. All rights reserved.
 */

import java.util.Arrays;
import java.util.Vector;

/*
 * Created on 16.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author zoppke
 * 
 * TODO
 */
public class Transmission {

    //int count;
    double bestDist = Double.MAX_VALUE;

    int[] teeth;

    double[] ratios;

    int[] fronts;

    public double gears(int[] front, int min, int max, int cnt) {
        //Arrays.sort(front);
        //for (int i=1;i<front.length;++i) {
        //  if (front[i]==front[])
        // }
        //Vector v = new Vector(Arrays.asList(front))
        fronts = front;
        teeth = new int[cnt];
        ratios = new double[cnt * front.length];
        teeth[0] = min;
        teeth[1] = max;

        //count=cnt;
        set(2);
        return bestDist;
    }

    void set(int index) {
        if (index == teeth.length) {
            evaluate();
            return;
        }
        for (int i = teeth[0]; i <= teeth[1]; ++i) {
            teeth[index] = i;
            set(index + 1);
        }
    }

    void evaluate() {
        double max = 0;
        int c = 0;
        for (int i = 0; i < fronts.length; ++i) {
            for (int j = 0; j < teeth.length; ++j) {
                ratios[c++] = ((double) fronts[i]) / ((double) teeth[j]);
            }
        }
        Arrays.sort(ratios);

        for (int i = 1; i < ratios.length; ++i) {
            double d = ratios[i] - ratios[i - 1];
            if (d > max)
                max = d;
        }
        if (max < bestDist)
            bestDist = max;
    }

    public static void main(String[] args) {
        Transmission tr = new Transmission();
        int[] fr = { 5, 9, 30, 100 };
        int min = 10;
        int max = 100;
        int cnt = 5;

        System.out.println(tr.gears(fr, min, max, cnt));
    }
}