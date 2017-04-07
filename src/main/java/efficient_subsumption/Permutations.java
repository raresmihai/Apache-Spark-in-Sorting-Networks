/*
 * Copyright (C) 2016 Faculty of Computer Science Iasi, Romania
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package efficient_subsumption;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Cache for storing all permutations of {0,1,...,n}, for n=0..12;
 *
 *
 * @author Cristian Frăsinaru
 */
public class Permutations {

    private static final int FACT[]
            = {1, 1, 2, 6, 24, 120, 720, 5_040, 40_320, 362_880, 3_628_800, 39_916_800, 479_001_600};
    //
    private static int[][][] PERM;
    private static boolean[] used;
    private static int count;

    static {
        init();
    }

    private static void init() {
        int n = 9;
        PERM = new int[n + 1][][];
        for (int i = 1; i <= n; i++) {
            createUsingIterator(i);
            //createIdentity(i);
        }
    }

    /**
     * Returns all the permutations of {0,1,...,n-1}.
     *
     * @param n
     * @return an array (length n!) of arrays (each of length n).
     */
    public static int[][] get(int n) {
        return PERM[n];
    }

    /**
     *
     * @param n
     * @return
     */
    public static int[] identity(int n) {
        return PERM[n][0];
    }

    /**
     *
     * @param n
     * @return
     */
    public static int factorial(int n) {
        return FACT[n];
    }

    private static void createIdentity(int n) {
        PERM[n] = new int[1][n];
        for (int i = 0; i < n; i++) {
            PERM[n][0][i] = i;
        }
    }

    private static void createUsingIterator(int n) {
        PERM[n] = new int[FACT[n]][];
        int i = 0;
        for (Iterator<int[]> it = new PermIterator(n); it.hasNext();) {
            int[] p = it.next();
            PERM[n][i] = new int[n];
            System.arraycopy(p, 0, PERM[n][i], 0, n);
            i++;
        }
    }

    /*
     * SLOOOOW......
     */
    private static void create(int n) {
        PERM[n] = new int[FACT[n]][];
        int m = factorial(n);
        for (int i = 0; i < m; i++) {
            PERM[n][i] = new int[n];
        }
        used = new boolean[n];
        count = 0;
        createRec(n, 0);
    }

    /*
     * SLOOOOW......
     */
    private static void createRec(int n, int len) {
        for (int i = 0; i < n; i++) {
            if (used[i]) {
                continue;
            }
            PERM[n][count][len] = i;
            used[i] = true;
            if (len < n - 1) {
                createRec(n, len + 1);
            } else if (count < PERM[n].length - 1) {
                count++;
                System.arraycopy(PERM[n][count - 1], 0, PERM[n][count], 0, n);
            }
            used[i] = false;
        }
    }

    /*        
    private static void createDemo(int n) {        
        List<Integer> elements = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            elements.add(i);
        }
        List<Integer> perm = new ArrayList<>();
        create(elements, perm);
    }
    private static void createDemoRec(List<Integer> elements, List<Integer> perm) {
        if (perm.size() == elements.size()) {
            //System.out.println(perm);
        }
        for (Integer x : elements) {
            if (perm.contains(x)) {
                continue;
            }
            perm.add(x);
            createDemoRec(elements, perm);
            perm.remove(x);
        }
    }
     */
    public static void main(String args[]) {
        //int a[] = new int[39_916_800];
        int n = 10;
        for (int i = 0; i < PERM[n].length; i++) {
            System.out.println(Arrays.toString(PERM[n][i]));
        }
    }
}
