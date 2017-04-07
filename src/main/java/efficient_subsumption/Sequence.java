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

/**
 * An input sequence of 0 and 1.
 *
 * @author Cristian Frăsinaru
 */
public class Sequence {

    private int nbits;
    private final boolean[] bitSet;
    private int value;
    private int cardinality;
    private static final Sequence[][] CACHE;

    static {
        int n = 9;
        CACHE = new Sequence[n + 1][];
        for (int k = 1; k <= n; k++) {
            int m = (int) Math.pow(2, k);
            CACHE[k] = new Sequence[m];
            for (int value = 0; value < m; value++) {
                CACHE[k][value] = new Sequence(k);
                CACHE[k][value].setValue(value);
            }
        }
    }

    /**
     * Returns the cached sequence. Do not modify the bitset!
     *
     * @param nbits
     * @param value
     * @return
     */
    public static Sequence getInstance(int nbits, int value) {
        return CACHE[nbits][value];
    }

    /**
     * The instance where the bits at the given indices are ordered ascending.
     *
     * @param other
     * @param idx0
     * @param idx1
     * @return
     */
    public static Sequence getSwappedInstance(Sequence other, int idx0, int idx1) {
        int temp = 0;
        int n = other.nbits;
        for (int i = 0; i < n; i++) {
            int j = i;
            if (i == idx0) {
                j = idx1;
            } else if (i == idx1) {
                j = idx0;
            }
            temp += other.bitSet[j] ? (1 << n - i - 1) : 0;
        }
        return Sequence.getInstance(n, temp);
    }
    
    private Sequence(int nbits) {
        this.nbits = nbits;
        this.cardinality = 0;
        bitSet = new boolean[nbits];
    }

    /**
     *
     * @param nbits
     * @param value
     */
    private Sequence(int nbits, int value) {
        this(nbits);
        this.value = value;
        this.cardinality = CACHE[nbits][value].cardinality;
        System.arraycopy(CACHE[nbits][value].bitSet, 0, this.bitSet, 0, nbits);
    }

    /**
     *
     * @param other
     */
    private Sequence(Sequence other) {
        this(other.nbits, other.value);
    }

    /**
     *
     * @return the number of bits of the sequence
     */
    public int length() {
        return nbits;
    }

    private void setValue(int value) {
        this.value = value;
        //
        int index = 0;
        while (value != 0) {
            boolean bit = value % 2 != 0;
            bitSet[nbits - index - 1] = bit;
            index++;
            value = value >>> 1;
            if (bit) {
                cardinality++;
            }
        }
    }

    private void computeValue() {
        this.value = 0;
        for (int i = 0; i < nbits; i++) {
            value += bitSet[nbits - i - 1] ? (1 << i) : 0;
        }
    }

    /**
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @return
     */
    public boolean[] getBitSet() {
        return bitSet;
    }

    /**
     *
     * @return
     */
    public int cardinality() {
        return cardinality;
    }

    /**
     *
     * @param bitIndex
     * @return
     */
    public boolean get(int bitIndex) {
        return bitSet[bitIndex];
    }


    /**
     *
     * @param perm
     * @return
     */
    public Sequence permute(int[] perm) {
        int n = perm.length;
        int permValue = 0;
        for (int i = 0; i < n; i++) {
            permValue += bitSet[i] ? 1 << nbits - perm[i] - 1 : 0;
        }

        return CACHE[nbits][permValue];
    }

    /**
     *
     * @param nbits
     * @return
     */
    public String toString(int nbits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nbits; i++) {
            sb.append(bitSet[i] ? "1" : "0");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(bitSet.length);
    }

    public static void main(String args[]) {
        for (int i = 1; i < CACHE.length; i++) {
            System.out.println(Arrays.toString(CACHE[i]));
            for (Sequence s : CACHE[i]) {
                System.out.println(s + " = " + s.getValue());
            }
        }
    }
}
