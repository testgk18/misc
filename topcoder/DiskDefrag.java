
 // too slow for system test.
import java.util.Arrays;
import java.util.StringTokenizer;

/*
 * Problem Statement      When files are stored on a hard disk, they often
 * become fragmented. This means that the file is not stored in sequential
 * sectors on the disk. The first half of a file might be stored in sector 243,
 * while the second half of a file might be far away in sector 105. The goal of
 * defragmenting a hard drive is to arrange the files so that each file is
 * stored in order on sequential sectors of the disk. Thus, if a file required 4
 * sectors of storage space, it would end up in sectors N, N+1, N+2, and N+3,
 * for some N. Typically, this is done to increase the overall speed of the
 * computer. There are a number of programs that will defrag a hard disk as
 * described above. However, many of them are painfully slow. You are trying to
 * develop a new algorithm to defrag hard drives, but before you start, you
 * would like to determine how fast you can defrag a very small drive without
 * very many files on it. You will be given the locations of a number of files
 * on a small hard disk, and are to determine the minimum number of sectors that
 * must be moved before the entire drive is defragged. You have enough memory to
 * hold two sectors worth of data at once, but that is all. You will be given a
 * String[], disk, each of whose elements represents a single file. Each element
 * of disk will be formatted as a single-space delimited list of integers which
 * represent the locations of the parts of the file, in order. Hence, the
 * String, "4 9 6 59 41" represents a file stored in 5 sectors where the first
 * part of the file is in sector 4 of the disk. One way to defrag this file
 * would be to move the contents of sector 9 to sector 5, the contents of sector
 * 59 to sector 7, and the contents of sector 41 to sector 8. By doing this, the
 * file would be stored sequentially in sectors 4-8. You will also be given an
 * int, size, representing the total number of sectors on the disk (sectors 0
 * through size-1, inclusive, may contain data). You are to return the smallest
 * number of sectors that must be moved to defragment the whole disk. Keep in
 * mind that you can not move data to a sector until any data being stored there
 * is moved. Definition      Class: DiskDefrag Method: minMoves Parameters:
 * String[], int Returns: int Method signature: int minMoves(String[] disk, int
 * size) (be sure your method is public)     
 * 
 * Constraints - size will be between 10 and 100, inclusive. - disk will contain
 * between 1 and 12 elements, inclusive. - Each element of disk will contain
 * between 1 and 50 characters, inclusive. - Each element of disk will be a
 * single-space delimited list of integers, without extraneous leading zeros. -
 * Each integer in disk will be between 0 and size-1, inclusive. - No integer
 * will be appear more than once in disk.
 * 
 * Examples 0)
 * 
 *      {"3 4 5 6 8 9 10","17 16 15"} 20 Returns: 5 We can defrag the first file
 * by moving the contents of sector 8 to sector 7, then 9 to 8, and finally 10
 * to 9. The second file can be defragged in a number of ways by moving the
 * contents of two sectors, for a total of 5. 1)
 * 
 *      {"1 2 3 5 4 6 7 8"} 10 Returns: 2 Here we can take advantage of the fact
 * that we have enough memory to hold two sectors worth of data. First, load the
 * contents of sectors 4 and 5 into memory. Now, simply write the data back in
 * the reverse order. 2)
 * 
 *      {"1 3 5 7","0 2 4 8","6 9"} 100 Returns: 7
 * 
 * This problem statement is the exclusive and proprietary property of TopCoder,
 * Inc. Any unauthorized use or reproduction of this information without the
 * prior written consent of TopCoder, Inc. is strictly prohibited. (c)2003,
 * TopCoder, Inc. All rights reserved.
 */

public class DiskDefrag {

    MyFile[] files;

    int minMoves, maxMoves = 0;

    int level, maxDiff, curDiff, curWaste, maxWaste;

    int minHole = Integer.MAX_VALUE;

    Hole[] curHoles = new Hole[100];

    int holePointer = 0;

    Hole[] lHoles, rHoles;

    Hole[] cache = new Hole[100];

    int pointer = 0;

    public int minMoves(String[] param, int size) {
        long timestamp = System.currentTimeMillis();

        // init disk status before defrag
        files = new MyFile[param.length];
        for (int i = 0; i < param.length; ++i) {
            StringTokenizer tt = new StringTokenizer(param[i]);
            int c = 0;
            files[i] = new MyFile();
            files[i].sectors = new int[tt.countTokens()];
            while (tt.hasMoreTokens()) {
                files[i].sectors[c++] = Integer.parseInt(tt.nextToken());
            }
            minHole = Math.min(minHole, c);
            maxMoves += c;
        }

        // init FileMoves
        for (int i = 0; i < files.length; ++i) {
            files[i].moves = new FileMove[size - files[i].sectors.length + 1];
            int cmin = Integer.MAX_VALUE;
            for (int j = 0; j < files[i].moves.length; ++j) {
                files[i].moves[j] = new FileMove();
                files[i].moves[j].index = j;
                int c = 0;
                for (int k = 0; k < files[i].sectors.length; ++k) {
                    if (files[i].sectors[k] != j + k) {
                        c++;
                    }
                }
                files[i].moves[j].diff = c;
                cmin = Math.min(c, cmin);
            }
            for (int j = 0; j < files[i].moves.length; ++j) {
                files[i].moves[j].diff -= cmin;
            }
            Arrays.sort(files[i].moves);
            minMoves += cmin;
        }

        maxWaste = size - maxMoves;
        Arrays.sort(files);
        lHoles = new Hole[files.length];
        rHoles = new Hole[files.length];

        // start defragging
        curHoles[holePointer++] = new Hole(0, size);
        while (true) {
            //System.out.println(maxDiff);
            level = 0;
            curDiff = 0;
            curWaste = 0;
            if (set()) {
                double performance = 0.001 * (System.currentTimeMillis() - timestamp);
                System.out.println(performance);
                return maxDiff + minMoves;
            }
            maxDiff++;
        }
    }

    final private boolean set() {

        // tail of recursion. evaluate moves
        if (level == files.length) {
            return true;
        }

        // find free place for file
        MyFile file = files[level];
        for (int i = 0; i < file.moves.length; ++i) {
            FileMove move = file.moves[i];
            if (maxDiff - curDiff < move.diff) {
                return false;
            }

            // find hole for our move
            Hole oldHole = findhole(move.index, move.index
                    + file.sectors.length);

            //System.out.println("oldhole "+oldHole.l);

            // check if hole is ok for our move
            if (oldHole != null) {
                int newWaste = oldHole.shrink(move.index, file.sectors.length);

                // if we are wasting too much, we don't go this move
                if (curWaste + newWaste > maxWaste) {
                    //System.out.println("Cannot put file #" + fileIndex
                    //      + " at index " + move.index);
                    continue;
                }

                // prepare for recursive call
                curWaste += newWaste;
                removeHole(oldHole);
                if (rHoles[level] != null) {
                    curHoles[holePointer++] = rHoles[level];
                }
                if (lHoles[level] != null) {
                    curHoles[holePointer++] = lHoles[level];
                }
                curDiff += move.diff;
                level++;

                // recursive call
                if (set()) {
                    return true;
                }

                // takeback move
                curWaste -= newWaste;
                level--;
                if (rHoles[level] != null) {
                    removeHole(rHoles[level]);
                    cache[pointer++] = rHoles[level];
                }
                if (lHoles[level] != null) {
                    removeHole(lHoles[level]);
                    cache[pointer++] = lHoles[level];
                }
                curHoles[holePointer++] = oldHole;
                curDiff -= move.diff;
            }
        }
        return false;
    }

    final private class Hole {
        int l, r;

        private Hole(int ll, int rr) {
            l = ll;
            r = rr;
        }

        final int shrink(int index, int length) {
            int ret = 0;
            int diff = r - (index + length);
            if (diff < minHole) {
                ret += diff;
                rHoles[level] = null;
            } else {
                rHoles[level] = get(index + length, r);
            }

            diff = index - l;
            if (diff < minHole) {
                ret += diff;
                lHoles[level] = null;
            } else {
                lHoles[level] = get(l, index);
            }
            return ret;
        }
    }

    final private class FileMove implements Comparable {
        int index, diff;

        final public int compareTo(Object o) {
            return diff - ((FileMove) o).diff;
        }
    }

    final private class MyFile implements Comparable {
        int[] sectors;

        FileMove[] moves;

        final public int compareTo(Object o) {
            MyFile mf = (MyFile) o;
            return mf.sectors.length - sectors.length;
        }
    }

    Hole get(int l, int r) {
        if (pointer == 0) {
            return new Hole(l, r);
        }
        cache[--pointer].l = l;
        cache[pointer].r = r;
        return cache[pointer];
    }

    void removeHole(Hole h) {
        for (int i = 0; i < holePointer; ++i) {
            if (h.l == curHoles[i].l) {
                curHoles[i] = curHoles[--holePointer];
                return;
            }
        }
        System.err.println("no that hole");
    }

    Hole findhole(int l, int r) {
        for (int i = 0; i < holePointer; ++i) {
            if (curHoles[i].l <= l && curHoles[i].r >= r) {
                return curHoles[i];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        DiskDefrag dd = new DiskDefrag();
        String[] param = { "8 0 7 51", "47 22 26 30 46", "41 4 9 10 20",
                "33 5 28 39 42", "31 12 56 57", "13 6 17 24 27 36 54",
                "58 32 34 45", "19 2", "3 11 15 25 38 48",
                "23 1 18 21 29 44 55", "40 16 35 43 49 52 53 59", "37 14 50" };

        //String[] param = { "3 4 5 6 8 9 10", "17 16 15" };
        //String[] param = {"1 2 3 5 4 6 7 8"};
        //String[] param = { "1 3 5 7", "0 2 4 8", "6 9" };
        System.out.println(dd.minMoves(param, 60));
    }
}