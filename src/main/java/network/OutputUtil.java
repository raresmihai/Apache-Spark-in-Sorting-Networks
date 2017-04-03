package network;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rares on 07.03.2017.
 */
public class OutputUtil implements Serializable{
    public static boolean isRedundant(Network network, Comparator c) {
        int wire0 = c.getI();
        int wire1 = c.getJ();
        Set<Output> outputSet = network.getOutputSet();
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
