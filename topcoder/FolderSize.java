// Passed System test
// BEGIN CUT HERE
// PROBLEM STATEMENT
// When files are stored on disk, typically they are stored in "clusters". Each
// cluster has a fixed size, and the amount of space consumed by a file is
// always
// a multiple of the cluster size. Thus, if the cluster size is 100 bytes, a 165
// byte file will actually use 200 bytes of storage, resulting in 35 bytes of
// wasted space.
// 
// We want to determine which areas of our disk storage are wasting the most
// space. You will be given a String[] files, each element of which contains a
// folder number followed by a space, followed by a file size. You will also be
// given an int folderCount indicating the total number of folders on our disk.
// The folders are numbered 0 through folderCount - 1. Finally, you will be
// given an int clusterSize, indicating how large each disk cluster is.
// 
// You are to return a int[], containing exactly folderCount elements, each
// element of which is the total amount of wasted space for that folder. Each
// element of the return value corresponds to the folder with the same index.
// 
// 
// DEFINITION
// Class:FolderSize
// Method:calculateWaste
// Parameters:String[], int, int
// Returns:int[]
// Method signature:int[] calculateWaste(String[] files, int folderCount, int
// clusterSize)
// 
// 
// NOTES
// -While many systems use a cluster size that is a power of two, no such
// restriction exists here.
// -There may be folders that have no files in them. (Wasted space for such a
// folder is 0.)
// 
// 
// CONSTRAINTS
// -clusterSize will be between 1 and 1000000, inclusive
// -folderCount will be between 1 and 50, inclusive
// -files will contain between 0 and 50 elements, inclusive
// -Each element of files will contain between 3 and 50 characters, inclusive
// -Each element of files will be in the form "<folder> <size>" (quotes added
// for
// clarity)
// -Each value of <folder> will be between 0 and folderCount - 1, inclusive
// -Each value of <size> will be between 0 and 1000000, inclusive
// -<folder> and <size> may contain leading zeros
// 
// 
// EXAMPLES
// 
// 0)
// {"0 55", "0 47", "1 86"}
// 3
// 50
// 
// Returns: { 48, 14, 0 }
// 
// Here, in folder 0, we have two files. The first requires 2 clusters, and
// wastes 45 bytes, the second uses 1 cluster and only wastes 3 bytes. The waste
// for folder 0 is 48 bytes. Folder 1 only has a single file, which wastes 14
// bytes. Folder 2 has no files in it, and thus wastes 0 bytes.
// 
// 1)
// {"0 123", "2 456", "4 789", "6 012", "8 345"}
// 10
// 98
// 
// Returns: { 73, 0, 34, 0, 93, 0, 86, 0, 47, 0 }
// 
// Note here that we can use a leading zero in the file size, and that only
// every
// other folder even has a file in it.
// 
// 2)
// {}
// 5
// 100
// 
// Returns: { 0, 0, 0, 0, 0 }
// 
// The cluster size really doesn't matter since our disk is empty.
// 
// 3)
// {"0 93842", "1 493784", "2 43212", "3 99327", "4 456209",
// "5 947243", "6 59348", "7 58237", "8 5834", "9 492384",
// "0 58342", "3 538432", "6 1432", "9 453983", "2 4321",
// "4 583729", "6 6974", "8 9864", "4 43211", "8 38437"}
// 10
// 22485
// 
// Returns: { 27696, 886, 19922, 14306, 18616, 19612, 44671, 9218,
// 35805, 20488 }
// 
// END CUT HERE

import java.util.*;

public class FolderSize {
    public int[] calculateWaste(String[] files, int folderCount, int clusterSize) {
        int[] res = new int[folderCount];
        for (int i=0;i<files.length;++i) {
            String[] ss = files[i].split(" ");
            int foln = Integer.parseInt(ss[0]);
            int size = Integer.parseInt(ss[1]);
            int diff = clusterSize- (size % clusterSize);
            if (diff == clusterSize) {
                diff=0;
            }
            res[foln] +=diff;
        }
        return res;
    }

    public static void main(String[] args) {
        FolderSize temp = new FolderSize();
        String[] files = new String[0];
        int folderCount = 0;
        int clusterSize = 0;
        System.out
                .println(temp.calculateWaste(files, folderCount, clusterSize));
    }
}