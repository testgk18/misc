// passed system test
// BEGIN CUT HERE
// PROBLEM STATEMENT
// An entry in a Thesaurus is a list of words that are all synonyms. Each entry
// contains no duplicates within it. It is possible
// that two entries might have some common words, but the editors (who are
// somewhat
// cheap) have decided that if any two entries have 2 or more words in common
// then
// they should be combined into a single entry.
// 
// This editing process may produce new entries which can be combined. The
// final Thesaurus must contain no pair of entries that have 2 or more words in
// common.
// Of course, each entry must contain no duplicates.
//  
// Create a class Thesaurus that contains a method edit that is given a String[]
// entry, the entries in the original Thesaurus. The method returns the edited
// Thesaurus as a String[]. Each element of entry has no leading or trailing
// spaces and has its words separated by a single space. Each element of the
// return should also have no leading or trailing spaces and have its words
// separated by a single space. In addition, the words within each element of
// the
// return must be in alphabetical order, and the elements in the return must
// appear in alphabetical order.
// 
// 
// 
// DEFINITION
// Class:Thesaurus
// Method:edit
// Parameters:String[]
// Returns:String[]
// Method signature:String[] edit(String[] entry)
// 
// 
// CONSTRAINTS
// -entry will contain between 1 and 50 elements inclusive.
// -Each element of entry will contain between 1 and 50 characters inclusive.
// -Each element of entry will consist of 1 or more "words" separated by single
// spaces.
// -Each element of entry will contain no leading or trailing spaces.
// -Each "word" will consist of 1 or more lowercase letters 'a'-'z'
// -No element of entry will contain two identical words.
// 
// 
// EXAMPLES
// 
// 0)
// {"ape monkey wrench", "wrench twist strain"}
// 
// Returns: { "ape monkey wrench", "strain twist wrench" }
// 
//  
// 
//    These two entries have only one common word so they cannot be combined.
//    After rearranging the words within each entry to put the words into
// alphabetical order,
//    the first entry is first alphabetically.
// 
// 
// 
// 1)
// {"ape monkey wrench", "wrench twist strain", "monkey twist frugue"}
// 
// Returns: { "ape monkey wrench", "frugue monkey twist", "strain twist wrench"
// }
// 
// 
// 
//    No entries could be combined, but two had to be arranged, and the order
//    was changed.
// 
// 2)
// {"ape monkey wrench", "wrench twist strain", "monkey twist frugue strain"}
// 
// Returns: { "ape frugue monkey strain twist wrench" }
// 
// 
// 
//    The first two entries could not be combined, but the last two could.
//    After they were combined, the first entry shared both "wrench" and
//    "monkey" with the new combined entry, so we ended up with just one entry.
// 
// 
// 3)
// {"point run score","point dot","cut run tear score","cut valley","cute
// pretty"}
// 
// Returns: { "cut point run score tear", "cut valley", "cute pretty", "dot
// point" }
// 
// END CUT HERE

import java.util.*;

public class Thesaurus {
    public String[] edit(String[] entry) {
        Vector thes = new Vector();
        for (int i = 0; i < entry.length; ++i) {
            String[] s = entry[i].split(" ");
            HashSet m = new HashSet();
            for (int j = 0; j < s.length; ++j) {
                m.add(s[j]);
            }
            thes.add(m);
        }
        //boolean done=false;
        int i = 0;
        while (i < thes.size()) {
            HashSet hs = (HashSet) thes.get(i);
            int j = i + 1;
            boolean done = false;
            while (j < thes.size() && !done) {
                HashSet hs2 = (HashSet) thes.get(j);
                int dl = 0;
                Iterator it = hs.iterator();
                while (it.hasNext()) {
                    String s = (String) it.next();
                    if (hs2.contains(s)) {
                        dl++;
                    }
                }
                if (dl >= 2) {
                    hs.addAll(hs2);
                    thes.remove(hs2);
                    done = true;
                    i = -1;
                    j = 0;
                } else {
                    j++;
                }
            }
            i++;
        }
        String[] retour = new String[thes.size()];
        for (int ii = 0; ii < thes.size(); ++ii) {
            HashSet hs = (HashSet) thes.get(ii);
            Iterator it = hs.iterator();
            String[] tt = new String[hs.size()];
            int cc = 0;
            while (it.hasNext()) {
                tt[cc++] = (String) it.next();
            }
            Arrays.sort(tt);
            retour[ii] = tt[0];
            for (int jj = 1; jj < tt.length; ++jj) {
                retour[ii] = retour[ii] + " " + tt[jj];
            }
        }
        Arrays.sort(retour);
        return retour;

    }

    public static void main(String[] args) {
        Thesaurus temp = new Thesaurus();
        String[] entry = { "ape monkey wrench", "wrench twist strain",
                "monkey twist frugue strain", "kl" };
        String[] ss = temp.edit(entry);
        System.out.println(ss[0]);
        System.out.println(ss[1]);
        System.out.println(ss[2]);
    }
}