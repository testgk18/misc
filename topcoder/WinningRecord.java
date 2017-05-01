// Passed System Test
// BEGIN CUT HERE
// PROBLEM STATEMENT
// 
// If a baseball team loses 15 games in a row, and then wins its next 5, there
// are a number of ways this could be reported. A fan of the team could brag
// that they have won all 5 of their last 5 games. However, it could also be
// truthfully said that they have won 5 out of their last 10 games, or only 25%
// of their last 20 games.
// 
// 
// Given a String results with the results of the last several games a team has
// played, determine how many of their most recent games to consider to find the
// team's worst and best percentages of winning. Return a int[] with 2
// elements. The first element should be how many of the team's most recent
// games to consider to give their best winning record (5, in the above
// example). The second element should be how many of the team's most recent
// games to consider to give their worst winning record (20, in the above
// example).
// 
// 
// The String results will contain only the characters 'W' and 'L', where 'W'
// indicates a win, and 'L' indicates a loss. The left-most character represents
// the most recent game, and the right-most character represents the least
// recent
// game. The string for the example above would be
// "WWWWWLLLLLLLLLLLLLLL".
// 
// 
// Never consider fewer than 3 games. When considering 7 games (for example),
// they must be the 7 most recent games, not any 7 from the list. If different
// numbers of games give the same winning record, choose the larger number of
// games.
// 
// 
// DEFINITION
// Class:WinningRecord
// Method:getBestAndWorst
// Parameters:String
// Returns:int[]
// Method signature:int[] getBestAndWorst(String games)
// 
// 
// CONSTRAINTS
// -results will contain between 3 and 50 characters, inclusive.
// -Each character of results will be either 'W' or 'L'.
// 
// 
// EXAMPLES
// 
// 0)
// "WWWWWLLLLLLLLLLLLLLL"
// 
// Returns: { 5, 20 }
// 
// This is the example from the problem statement.
// 
// 1)
// "WWWWWW"
// 
// Returns: { 6, 6 }
// 
// No matter how many games you consider, the team's winning record is 100% and
// the losing record is 0%.
// 
// 2)
// "LWLWLWLWLWLWLWLWLWLWLWLWLWLWLWLWLWLWLWLWLWLWLWLWLW"
// 
// Returns: { 50, 3 }
// 
// 3)
// "WLWLWLLWWLWLWWWWWWWLWLLLLLLLLLLLLWWLWLLWWWLLLWLWLW"
// 
// Returns: { 19, 33 }
// 
// 4)
// "LWWLWWLWWLLLW"
// 
// Returns: { 9, 12 }
// 
// END CUT HERE


public class WinningRecord {
    public int[] getBestAndWorst(String games) {
        int[] ret = new int[2];
        double[] perc = new double[] { 101, -1 };
        for (int i = 3; i <= games.length(); ++i) {
            String s = games.substring(0, i);
            String t = s.replaceAll("W", "");
            System.out.println(i + ",," + s.length());
            double d = (double) t.length() / (double) i;
            if (d <= perc[0]) {
                perc[0] = d;
                ret[0] = i;
            }
            if (d >= perc[1]) {
                perc[1] = d;
                ret[1] = i;
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        String games = "LWWLLWWLLWLWLL";
        WinningRecord temp = new WinningRecord();
        System.out.println(temp.getBestAndWorst(games));
    }
}