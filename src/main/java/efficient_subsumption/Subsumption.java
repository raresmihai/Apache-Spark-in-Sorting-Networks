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
 * along with out0 program.  If not, see <http://www.gnu.org/licenses/>.
 */
package efficient_subsumption;



import java.util.BitSet;

/**
 *
 * @author Cristian Frăsinaru
 */
public interface Subsumption {

    int[] findPermutation(OutputSet out0, OutputSet out1);

    /**
     * Checks if the network net0 subsumes the network net1.
     *

     * @return
     */
    default int[] check(OutputSet out0, OutputSet out1) {

        //check cluster sizes and total number of zeros and ones (one by one)
        //if (cannotSubsume(out0, out1)) {
        if (out0.cannotSubsume(out1)) {
            return null;
        }
        //check direct inclusion
        if (out1.includes(out0)) {
            return Permutations.identity(out0.nbWires);
        }
        //find a permutation
        int perm[] = findPermutation(out0, out1);

        return perm;
    }

    /**
     *
     * @param c0
     * @param c1
     * @return
     */
    default boolean cannotSubsume(OutputCluster c0, OutputCluster c1) {
        return c0.size() > c1.size()
                || c0.zeroCount() > c1.zeroCount()
                || c0.oneCount() > c1.oneCount();
    }

    /**
     *
     * @param out0
     * @param out1
     * @return
     */
    default boolean cannotSubsume(OutputSet out0, OutputSet out1) {
        if (out0.size() > out1.size()) {
            return true;
        }
        if (out0.size() < out1.size()) {
            if (out0.maxClusterSize() > out1.maxClusterSize()
                    || out0.minClusterSize() > out1.minClusterSize()
                    || out0.maxZeroCount() > out1.maxZeroCount()
                    || out0.minZeroCount() > out1.minZeroCount()
                    || out0.maxOneCount() > out1.maxOneCount()
                    || out0.minOneCount() > out1.minOneCount()) {
                return true;
            }
        } else if (out0.maxClusterSize() != out1.maxClusterSize()
                || out0.minClusterSize() != out1.minClusterSize()
                || out0.maxZeroCount() != out1.maxZeroCount()
                || out0.minZeroCount() != out1.minZeroCount()
                || out0.maxOneCount() != out1.maxOneCount()
                || out0.minOneCount() != out1.minOneCount()) {
            return true;
        }
        for (int k = 1, n = out0.nbWires; k < n; k++) {
            if (cannotSubsume(out0.cluster(k), out1.cluster(k))) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param c0
     * @param c1
     * @param perm
     * @return
     */
    default boolean checkPermutation(OutputCluster c0, OutputCluster c1, int[] perm) {
        BitSet values0 = c0.bitValues();
        BitSet values1 = c1.bitValues();
        int n = c0.nbWires;
        for (int value0 = values0.nextSetBit(0); value0 >= 0; value0 = values0.nextSetBit(value0 + 1)) {
            int value1 = Sequence.getInstance(n, value0).permute(perm).getValue();
            if (!values1.get(value1)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param out0
     * @param out1
     * @param perm
     * @return
     */
    default boolean checkPermutation(OutputSet out0, OutputSet out1, int[] perm) {
        for (int k = 2, n = out0.nbWires; k < n - 1; k++) {
            if (!checkPermutation(out0.cluster(k), out1.cluster(k), perm)) {
                return false;
            }
        }
        return true;
    }

}
