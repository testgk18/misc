// passed system test
/*
 * Problem Statement      Given a String text return the longest prefix which
 * does not have any element of remove as a substring. A substring is a
 * contiguous portion of a string, which may be the entire string. A prefix is a
 * substring taken from the beginning of the string. For example, "wow" is a
 * substring of "awowb", but not a prefix. "awo" is both a substring and prefix
 * of "awowb". Definition      Class: PrefixChoice Method: longestPrefix
 * Parameters: String, String[] Returns: String Method signature: String
 * longestPrefix(String text, String[] remove) (be sure your method is public)
 *     
 * 
 * Constraints - remove will contain between 1 and 50 elements inclusive. - Each
 * element of remove will contain between 2 and 50 characters inclusive. - Each
 * character in each element of remove will be a lowercase letter ('a'-'z'). -
 * text will contain between 1 and 50 characters inclusive. - Each character in
 * text will be a lowercase letter ('a'-'z'). Examples 0)
 * 
 *      "awowb" {"wow"} Returns: "awo" The longest prefix is "awo" since
 * anything longer will contain "wow". 1)
 * 
 *      "awowb" {"awowb"} Returns: "awow"
 * 
 * 2)
 * 
 *      "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwx"
 * {"abce","abcdefh","abcdefghijklmnopr"} Returns:
 * "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwx"
 * 
 * 3)
 * 
 *      "zzzzzzzzzz" {"zz","zzz","zzzz"} Returns: "z"
 * 
 * 4)
 * 
 *      "topcodertopcodertopcodertopcoder" {"rtopcodertop"} Returns:
 * "topcodertopcoderto"
 * 
 * This problem statement is the exclusive and proprietary property of TopCoder,
 * Inc. Any unauthorized use or reproduction of this information without the
 * prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003,
 * TopCoder, Inc. All rights reserved.
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class PrefixChoice {

    public String longestPrefix(String text, String[] remove) {

        Vector v = new Vector(Arrays.asList(remove));
        for (int i = 0; i <= text.length(); ++i) {
            String s = text.substring(0, i);
            for (int j = 0; j < remove.length; ++j) {
                if (s.indexOf(remove[j]) >= 0) {
                    return text.substring(0, i - 1);
                }
            }
        }
        return text;
    }

    public static void main(String[] args) {
        PrefixChoice inst = new PrefixChoice();
    }
}