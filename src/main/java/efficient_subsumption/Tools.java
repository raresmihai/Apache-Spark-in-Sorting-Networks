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
import java.util.StringJoiner;

/**
 *
 * @author Cristian Frăsinaru
 */
public class Tools {

    /**
     *
     * @param value
     * @return
     */
    public static BitSet toBitSet(int value) {
        BitSet bitSet = new BitSet();
        int index = 0;
        while (value != 0) {
            bitSet.set(index++, value % 2 != 0);
            value = value >>> 1;
        }
        return bitSet;
    }

    /**
     *
     * @param bitSet
     * @return
     */
    public static int toInt(BitSet bitSet) {
        int value = 0;
        for (int i = bitSet.nextSetBit(0); i != -1; i = bitSet.nextSetBit(i + 1)) {
            value += bitSet.get(i) ? (1 << i) : 0;
        }
        return value;
    }

    /**
     *
     * @param bitSet
     * @return
     */
    public static int toInt(boolean[] bitSet) {
        int value = 0;
        int n = bitSet.length;
        for (int i = 0; i < n; i++) {
            value += bitSet[i] ? (1 << n - i - 1) : 0;
        }
        return value;
    }

    /**
     *
     * @param bitSet
     * @param nbits
     * @return
     */
    public static String toBinaryString(BitSet bitSet, int nbits) {
        StringBuilder sb = new StringBuilder();
        for (int i = nbits - 1; i >= 0; i--) {
            sb.append(bitSet.get(i) ? "1" : "0");
        }
        return sb.toString();
    }

    /**
     *
     * @param value
     * @param nbits
     * @return
     */
    public static String toBinaryString(int value, int nbits) {
        BitSet bitSet = toBitSet(value);
        return toBinaryString(bitSet, nbits);
    }



}
