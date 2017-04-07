package network;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class
 */
public class OutputUtil implements Serializable{
    /**
     *
     * @param outputSet The current outputSet that will be checked for redundancy with the new comparator.
     * @param c The new comparator that is being added to the current network with the outputSet.
     * @return true if there is no pair of bits in the outputSet on c.wire0 and c.wire1 such that bit on wire0 > bit on wire1.
     *         false if there exists a pair of bits in the outputSet that will be swapped after adding the new comparator c.
     *
     * Example:
     * For the outputSet |0000,0001,0010,0011,0111,1111|
     * comparator (3,4) is not redundant because 0010 will be transformed into 0001
     * comparator (2,3) is redundant because there is no output with 'bit 2 == 1' and 'bit 3 == 0'
     */
    public static boolean isRedundant(Set<Output> outputSet, Comparator c) {
        int wire0 = c.getI();
        int wire1 = c.getJ();
        if(outputSet.size() == 0) {
            return false;
        }
        for(Output output : outputSet) {
            // wire0 = 1 and wire1 = 0
            if(output.values.get(wire0-1) && !output.values.get(wire1-1)) {
                return false;
            }
        }
        //no pair of bits from wire0 to wire1 found such that wire0 > wire1
        return true;
    }

    static Set<Output> generate(Comparator comparator) {
        Set<Output> outputSet = new HashSet<>();
        int maxValue = (int)Math.pow(2, NetworkProperties.NUMBER_OF_WIRES) ;
        int i = comparator.getI();
        int j = comparator.getJ();
        for(int v = 0; v<maxValue; v++) {
            int bit1 = (int)(v/Math.pow(2,i-1)) % 2;
            int bit2 = (int)(v/Math.pow(2,j-1)) % 2;
            if(bit1 <= bit2) {
                Output output = new Output(v);
                outputSet.add(output);
            }
        }
        return outputSet;
    }
}
