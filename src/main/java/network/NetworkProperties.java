package network;

import java.io.Serializable;

/**
 * Created by Rares on 16.03.2017.
*/
public class NetworkProperties implements Serializable{
    private static final int comparatorsOptimalSize[] = {0,0,1,3,5,9,12,16,19,25,29,33,37,41,45,49,53};
    public static final int NUMBER_OF_WIRES = 6;
    public static final int NUMBER_OF_COMPARATORS = comparatorsOptimalSize[NUMBER_OF_WIRES];
    public static final int NUMBER_OF_SEQ_GENERATE_STEPS = 0;
}
