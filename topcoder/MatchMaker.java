// Passed System test
// BEGIN CUT HERE
// PROBLEM STATEMENT
// THIS PROBLEM WAS TAKEN FROM THE SEMIFINALS OF THE TOPCODER INVITATIONAL
// TOURNAMENT
// 
// DEFINITION
// Class Name: MatchMaker
// Method Name: getBestMatches
// Paramaters: String[], String, int
// Returns: String[]
// Method signature (be sure your method is public): String[]
// getBestMatches(String[] members, String currentUser, int sf);
// 
// PROBLEM STATEMENT
// A new online match making company needs some software to help find the
// "perfect
// couples". People who sign up answer a series of multiple-choice questions.
// Then, when a member makes a "Get Best Mates" request, the software returns a
// list of users whose gender matches the requested gender and whose answers to
// the questions were equal to or greater than a similarity factor when compared
// to the user's answers.
// 
// Implement a class MatchMaker, which contains a method getBestMatches. The
// method takes as parameters a String[] members, String currentUser, and an int
// sf:
// - members contains information about all the members. Elements of members are
// of the form "NAME G D X X X X X X X X X X"
//    * NAME represents the member's name
//    * G represents the gender of the current user.
//    * D represents the requested gender of the potential mate.
// * Each X indicates the member's answer to one of the multiple-choice
// questions. The first X is the answer to the first question, the second is the
// answer to the second question, et cetera.
// - currentUser is the name of the user who made the "Get Best Mates" request.
// - sf is an integer representing the similarity factor.
// 
// The method returns a String[] consisting of members' names who have at least
// sf
// identical answers to currentUser and are of the requested gender. The names
// should be returned in order from most identical answers to least. If two
// members have the same number of identical answers as the currentUser, the
// names
// should be returned in the same relative order they were inputted.
// 
// TopCoder will ensure the validity of the inputs. Inputs are valid if all of
// the following criteria are met:
// - members will have between 1 and 50 elements, inclusive.
// - Each element of members will have a length between 7 and 44, inclusive.
// - NAME will have a length between 1 and 20, inclusive, and only contain
// uppercase letters A-Z.
// - G can be either an uppercase M or an uppercase F.
// - D can be either an uppercase M or an uppercase F.
// - Each X is a capital letter (A-D).
// - The number of Xs in each element of the members is equal. The number of Xs
// will be between 1 and 10, inclusive.
// - No two elements will have the same NAME.
// - Names are case sensitive.
// - currentUser consists of between 1 and 20, inclusive, uppercase letters,
// A-Z,
// and must be a member.
// - sf is an int between 1 and 10, inclusive.
// - sf must be less than or equal to the number of answers (Xs) of the members.
// 
// NOTES
// The currentUser should not be included in the returned list of potential
// mates.
// 
// 
// EXAMPLES
// 
// For the following examples, assume members =
// {"BETTY F M A A C C",
//  "TOM M F A D C A",
//  "SUE F M D D D D",
//  "ELLEN F M A A C A",
//  "JOE M F A A C A",
//  "ED M F A D D A",
//  "SALLY F M C D A B",
//  "MARGE F M A A C C"}
// 
// If currentUser="BETTY" and sf=2, BETTY and TOM have two identical answers and
// BETTY and JOE have three identical answers, so the method should return
// {"JOE","TOM"}.
// 
// If currentUser="JOE" and sf=1, the method should return
// {"ELLEN","BETTY","MARGE"}.
// 
// If currentUser="MARGE" and sf=4, the method should return [].
// 
// 
// DEFINITION
// Class:MatchMaker
// Method:getBestMatches
// Parameters:String[], String, int
// Returns:String[]
// Method signature:String[] getBestMatches(String[] param0, String param1, int
// param2)
// 
// END CUT HERE

import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.Vector;

public class MatchMaker {

    public String[] getBestMatches(String[] members, String currentUser, int sf) {

        // init members
        Member[] ms = new Member[members.length];
        Member myMember = null;
        for (int i = 0; i < members.length; ++i) {
            ms[i] = new Member(members[i]);
            if (ms[i].name.equals(currentUser)) {
                myMember = ms[i];
            }
        }

        // init buckets
        Vector[] buckets = new Vector[11];
        for (int i=0;i<buckets.length;++i) {
            buckets[i]=new Vector();
        }
        
        // sort members to buckets
        for (int i = 0; i < members.length; ++i) {
            int m = myMember.match(ms[i]);
            buckets[m].add(ms[i].name);
        }
        // collect buckets
        Vector r = new Vector();
        for (int i = 10; i >= sf; --i) {
            r.addAll(buckets[i]);
        }
        // convert to array
        String[] retour = new String[r.size()];
        r.toArray(retour);
        return retour;
    }

    class Member {
        String name;

        char gender, wgender;

        char[] answers;

        Member(String s) {
            StringTokenizer tt = new StringTokenizer(s);
            answers = new char[tt.countTokens() - 3];
            name = tt.nextToken();
            gender = tt.nextToken().charAt(0);
            wgender = tt.nextToken().charAt(0);

            for (int i = 0; i < answers.length; ++i) {
                answers[i] = tt.nextToken().charAt(0);
            }
        }

        int match(Member m) {
            if (m.name.equals(name)) {
                return 0;
            }
            if (m.gender == wgender ) {
                int r = 0;
                for (int i = 0; i < answers.length; ++i) {
                    if (answers[i] == m.answers[i]) {
                        r++;
                    }
                }
                return r;
            }
            return 0;
        }
    }

    public static void main(String[] args) {
        MatchMaker mm = new MatchMaker();
        String[] members = new String[] { "BETTY F M A A C C",
                "TOM M F A D C A", "SUE F M D D D D", "ELLEN F M A A C A",
                "JOE M F A A C A", "ED M F A D D A", "SALLY F M C D A B",
                "MARGE F M A A C C" };
        String[] ret = mm.getBestMatches(members, "MARGE", 4);
        for (int i = 0; i < ret.length; ++i) {
            System.out.println(ret[i]);
        }
    }
}