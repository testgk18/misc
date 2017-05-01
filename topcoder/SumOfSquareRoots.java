// Passed System test
// BEGIN CUT HERE
// PROBLEM STATEMENT
// 
// The expression "sqrt(12) + sqrt(48)" can be simplified as follows:
// 
// 
//     sqrt(12) + sqrt(48) = sqrt(4*3) + sqrt(16*3)
//                         = 2*sqrt(3) + 4*sqrt(3)
//                         = 6*sqrt(3)
//                         = sqrt(36*3)
//                         = sqrt(108)
// 
// 
// Given a list of integers, A,
// return a second list of integers, B,
// such that the sum of the square roots of the elements in B
// equals the sum of the square roots of the elements in A.
// B should contain as few elements as possible.
// The list with the fewest elements is guaranteed to be unique.
// The elements in your returned list B should be sorted
// from smallest to largest.
// 
// 
// A will be given as a int[]. Return B as a int[] also.
// 
// 
// For example, given the integers { 9, 16, 25 }, the sum of the
// square roots is 3 + 4 + 5, which is 12.
// The sum of the square roots of the list { 121, 1 } is also 12,
// but there is an even shorter list: { 144 }, which is the correct answer.
// 
// 
// 
// DEFINITION
// Class:SumOfSquareRoots
// Method:shortestList
// Parameters:int[]
// Returns:int[]
// Method signature:int[] shortestList(int[] A)
// 
// 
// CONSTRAINTS
// -A will contain between 1 and 50 elements, inclusive.
// -Each element of A will be between 1 and 1000, inclusive.
// 
// 
// EXAMPLES
// 
// 0)
// {12, 48}
// 
// Returns: { 108 }
// 
// This is the first example in the problem statement.
// 
// 1)
// {9, 16, 25}
// 
// Returns: { 144 }
// 
// This is the second example in the problem statement.
// 
// 2)
// {4, 3}
// 
// Returns: { 3, 4 }
// 
// The square root of 4 plus the square root of 3 is ~3.7320508.
// There is no way to express this as the square root of a single integer,
// so the correct answer is { 3, 4 }.
// 
// 3)
// {1, 1, 1}
// 
// 
// Returns: { 9 }
// 
// 4)
// {5, 3, 5}
// 
// Returns: { 3, 20 }
// 
// 5)
// {1, 3, 5, 12, 20}
// 
// Returns: { 1, 27, 45 }
// 
// 6)
// {1, 2, 4, 8, 16, 32, 64, 128, 256, 512 }
// 
// Returns: { 961, 1922 }
// 
// 7)
// { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
//   11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
//   21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
//   31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
//   41, 42, 43, 44, 45, 46, 47, 48, 49, 50 }
// 
// Returns: { 13, 14, 15, 17, 19, 21, 22, 23, 26, 29, 30, 31, 33,
// 34, 35, 37, 38, 39, 41, 42, 43, 46, 47, 54, 63, 90, 99, 180,
// 300, 450, 784 }
// 
// END CUT HERE

import java.util.*;

public class SumOfSquareRoots {
    public int[] shortestList(int[] A) {
        int[] fac = new int[A.length];
        for (int i = 0; i < A.length; ++i) {
            fac[i] = 1;
            int c = 2;
            while (A[i] >= c*c) {
                if (A[i] % (c * c) == 0) {
                    A[i] /= (c * c);
                    fac[i] *= c;
                } else {
                    c++;
                } 
            }
            System.out.println(fac[i] + ", " + A[i]);
        }

        for (int i = 0; i < A.length; ++i) {
            for (int j = i + 1; j < A.length; ++j) {
                if (fac[i] != 0 && A[i] == A[j]) {
                    fac[i] += fac[j];
                    fac[j] = 0;
                }
            }
        }
        for (int i = 0; i < A.length; ++i) {
            A[i] *= (fac[i] * fac[i]);
        }
        Arrays.sort(A);
        int c = 0;
        while (c < A.length && A[c] == 0) {
            ++c;
        }
        int[] ret = new int[A.length - c];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = A[i + c];
        }
        return ret;

    }

    public static void main(String[] args) {
        int[] A = new int[] {313, 934, 939, 655, 11};
        SumOfSquareRoots temp = new SumOfSquareRoots();
        int[] result = temp.shortestList(A);
        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[2]);
    }
}