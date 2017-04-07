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


import java.util.BitSet;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A group of possible outputs all having the same number of ones.
 *
 * The level of the cluster is the number of ones. There are 1 + nbWires
 * clusters in a network. The cluster at level 0 has only one element: 00..0.
 * The cluster at level nbWires has only one element: 11...1. A cluster at level
 * k has at most C(nbWires;k) elements.
 *
 * @author Cristian Frăsinaru
 */
public class OutputCluster {

    private final OutputSet outputSet;
    private final int level;
    //
    private final BitSet bitValues; //the values in this cluster
    private int[] intValues; //lazy
    private int count0;
    private int count1;
    boolean[] pos0; //all positions where zeros appear 
    boolean[] pos1; //all positions where ones appear
    public int nbWires;
    private int size;

    /**
     *
     * @param outputSet
     * @param level
     */
    public OutputCluster(OutputSet outputSet, int level) {
        this(outputSet, level, true);
    }

    /**
     *
     * @param outputSet
     * @param level
     * @param createPos
     */
    private OutputCluster(OutputSet outputSet, int level, boolean createPos) {
        this.outputSet = outputSet;
        this.level = level;
        this.nbWires = outputSet.nbWires;
        this.bitValues = new BitSet();
        this.size = 0;
        if (createPos) {
            count0 = 0;
            count1 = 0;
            pos0 = new boolean[nbWires];
            pos1 = new boolean[nbWires];
        }
    }

    /**
     *
     * @return
     */
    public OutputSet getOutputSet() {
        return outputSet;
    }

    /**
     *
     * @return
     */

    /**
     * Adds a new possible output to the cluster.
     *
     * @param sequence
     * @return
     */
    public int add(Sequence sequence) {
        int k = sequence.cardinality(); //how many one's
        if (k != this.level) {
            throw new IllegalArgumentException("Number of ones is different than the cluster level.");
        }
        int value = sequence.getValue();
        if (bitValues.get(value)) {
            return -1;
        }
        bitValues.set(value);
        boolean[] bitSet = sequence.getBitSet();
        for (int i = 0; i < nbWires; i++) {
            if (!pos0[i] && !bitSet[i]) {
                pos0[i] = true;
                count0++;
            }
            if (!pos1[i] && bitSet[i]) {
                pos1[i] = true;
                count1++;
            }
        }
        //
        size++;
        return value;
    }

    /**
     * Returns the number of elements in this cluster.
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Returns the output values in the cluster, as a BitSet.
     *
     * A bit set at the position i in the returned object means that the binary
     * representation of the integer i is a possible output of the network.
     *
     * @return
     */
    public BitSet bitValues() {
        return bitValues;
    }

    /**
     * Returns the output values in the clutser, as an array of integers.
     *
     * Each binary representation of an integer in the returned array is a
     * possible output of the network.
     *
     * @return
     */
    public int[] intValues() {
        if (intValues != null) {
            return intValues;
        }
        intValues = new int[bitValues.cardinality()];
        int j = 0;
        for (int i = bitValues.nextSetBit(0); i >= 0; i = bitValues.nextSetBit(i + 1)) {
            intValues[j++] = i;
        }
        return intValues;
    }

    /**
     *
     * @return
     */
    public boolean[] zeroPositions() {
        return pos0;
    }

    /**
     *
     * @return
     */
    public boolean[] onePositions() {
        return pos1;
    }

    /**
     *
     * @return
     */
    public int zeroCount() {
        return count0;
    }

    /**
     *
     * @return
     */
    public int oneCount() {
        return count1;
    }

    /**
     *
     * @param other
     * @return
     */
    public boolean includes(OutputCluster other) {
        if (other.size > this.size) {
            return false;
        }
        BitSet values0 = this.bitValues;
        BitSet values1 = other.bitValues;

        BitSet bs = new BitSet(values1.size());
        bs.or(values1);
        bs.andNot(values0);
        return bs.isEmpty();
        /*
        for (int value1 = values1.nextSetBit(0); value1 >= 0; value1 = values1.nextSetBit(value1 + 1)) {
            if (!values0.get(value1)) {
                return false;
            }
        }
        return true;        
         */
    }

    /**
     *
     * @param other
     * @return
     */
    public boolean cannotSubsume(OutputCluster other) {
        return this.size > other.size || this.count0 > other.count0 || this.count1 > other.count1;
    }

    /**
     *
     * @param other
     * @param perm
     * @return
     */
    public boolean checkMatching(OutputCluster other, int[] perm) {
        BitSet values0 = this.bitValues;
        BitSet values1 = other.bitValues;
        for (int value0 = values0.nextSetBit(0); value0 >= 0; value0 = values0.nextSetBit(value0 + 1)) {
            int value1 = Sequence.getInstance(nbWires, value0).permute(perm).getValue();
            if (!values1.get(value1)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OutputCluster)) {
            return false;
        }
        OutputCluster other = (OutputCluster) obj;
        if (this.level != other.level) {
            return false;
        }
        return this.bitValues.equals(other.bitValues);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.level;
        hash = 89 * hash + Objects.hashCode(this.bitValues);
        return hash;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        for (int i = bitValues.nextSetBit(0); i != -1; i = bitValues.nextSetBit(i + 1)) {
            joiner.add(Tools.toBinaryString(i, nbWires));
        }
        return joiner.toString();
    }

    public String toStringZeros() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nbWires; i++) {
            sb.append(pos0[i] ? "0" : "_");
        }
        return sb.toString();
    }

    public String toStringOnes() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nbWires; i++) {
            sb.append(pos1[i] ? "1" : "_");
        }
        return sb.toString();
    }

}
