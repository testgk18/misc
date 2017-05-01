// passed system test
// BEGIN CUT HERE
// PROBLEM STATEMENT
// In diving competitions each dive is assigned a numeric rating
// by 5 different judges. Each dive also has a "degree of difficulty."
// The score for the dive is obtained by
// throwing out a highest and a lowest rating, and then multiplying the degree
// of
// difficulty times the sum of the remaining three ratings.
// 
// Each rating must be a decimal number between 0.0 and 10.0 inclusive,
// having exactly one digit to the right of its decimal point.
// Furthermore, the digit to the right of the decimal point must be a 0 or a 5.
// The degree
// of difficulty must be between 1.0 and 4.0 inclusive and have exactly one
// digit
// (possibly 0) to the right of its decimal point. Exactly 3 characters are
// used to express the degree of difficulty and each rating, with the exception
// that a rating
// of 10.0 uses 4 characters.
// 
// We know the score that our diver needs on her final dive to win a medal.
// We know the degree of difficulty of her final dive. But when the ratings
// appear on the scoreboard, one of the ratings appears as "?.?" due to an
// electronic
// malfunction. What is the smallest legal rating from that judge that will
// result
// in a medal?
// 
// Create a class Diving that contains a method needed that is given a
// String difficulty, the degree of difficulty, a String need that is the score
// needed for a medal expressed with exactly 2 digits to the right of its
// decimal
// point,
// and a String ratings
// containing the 5 ratings given to our diver except that one of them appears
// as
// "?.?". ratings will have no leading or trailing spaces and will have exactly
// one space between its 5 parts.
// The method returns the smallest legal rating that would result in a medal for
// our diver, expressed as a String as described above. If no legal rating would
// give her a medal, return "-1.0".
// 
// 
// 
// DEFINITION
// Class:Diving
// Method:needed
// Parameters:String, String, String
// Returns:String
// Method signature:String needed(String difficulty, String need, String
// ratings)
// 
// 
// CONSTRAINTS
// -difficulty will be a legally expressed degree of difficulty.
// -need will be a value between 0 and 120 inclusive and will contain a decimal
// point followed by exactly two digits.
// -need will contain no more than 6 characters (but may have leading zeroes).
// -ratings will consist of 4 legally expressed ratings and "?.?", separated by
// 4
// single spaces (' ').
// 
// 
// EXAMPLES
// 
// 0)
// "3.2"
// "50.32"
// "5.5 7.5 10.0 ?.? 4.5"
// 
// Returns: "0.0"
// 
// 
// 
//    Even if the unknown rating is thrown out as the low rating, the resulting
//    score is 3.2*(5.5+7.5+4.5) which exceeds the needed score of 50.32.
// 
// 
// 
// 1)
// "3.2"
// "50.32"
// "5.5 5.5 10.0 ?.? 4.5"
// 
// Returns: "5.0"
// 
// 
// 
//    If the unknown rating is no help, then our score would be 3.2*(5.5 + 5.5 +
// 4.5)
//    = 49.60 which is too low. If the unknown rating is 5.0, then it will replace
//    the 4.5 yielding a score of 3.2*(5.5+5.5+5.0) = 51.2 which is enough for a
//    medal.
//   
// 
// 2)
// "4.0"
// "120.00"
// "9.5 10.0 ?.? 10.0 10.0"
// 
// Returns: "10.0"
// 
// 
//    We need to be able to count 3 ratings of 10.0 to achieve 120.00. One of the
// existing
//    10.0's will be thrown out as the high rating, so we need a perfect 10.0 to
// get
//    a medal.
// 
// 3)
// "4.0"
// "120.00"
// "9.5 10.0 ?.? 9.5 10.0"
// 
// Returns: "-1.0"
// 
// 
// 
//    Even a 10.0 will leave us short of the needed score of 120.
// 
// 4)
// "2.3"
// ".01"
// "0.0 0.0 ?.? 0.0 0.5"
// 
// Returns: "0.5"
// 
// 5)
// "3.3"
// "47.85"
// "0.5 5.5 3.5 7.0 ?.?"
// 
// Returns: "5.5"
// 
// END CUT HERE

import java.util.*;

public class Diving {

    public String needed(String difficulty, String need, String ratings) {

        double[] rates;
        //ratings.replaceAll("\\O77\\O56\\O77", "");
        //ratings.replaceAll("  ", " ");
        String[] ss = ratings.split(" ");
        rates = new double[5];
        int c=0;
        for (int i = 0; i < rates.length; ++i) {
            if (!ss[i].equals("?.?"))
            rates[c++] = Double.parseDouble(ss[i]);
        }
        double niet = Double.parseDouble(need);
        double diff = Double.parseDouble(difficulty);

        for (int i = 0; i <= 100; i += 5) {
            rates[4] = (double) (i / 10.0);
            double[] te = new double[5];
            System.arraycopy(rates, 0, te, 0, 5);
            Arrays.sort(te);
            //System.out.println(te[0]);
            //System.out.println(te[1]);
            //System.out.println(te[2]);
            //System.out.println(te[3]);
            //System.out.println(te[4]);
            double res = (te[1] + te[2] + te[3]) * diff;
            //System.out.println("i= "+i+": "+res);
            if (res + 0.00000001>= niet) {
                return "" + rates[4];
            }
        }
        return "-1.0";
    }

    public static void main(String[] args) {
        Diving temp = new Diving();
        String difficulty = "3.3";
        String need = "47.85";
        String ratings = "0.5 5.5 3.5 7.0 ?.?";
        System.out.println("result:"+ temp.needed(difficulty, need, ratings));
    }
}