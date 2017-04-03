package spark_functions;

import network.Network;
import network.NetworkProperties;
import network.Output;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Rares on 23.03.2017.
 */
public class MapToKeyNetworkPair implements PairFunction<Network,String,List<Network>> {
    @Override
    public Tuple2<String, List<Network>> call(Network network) throws Exception {
        String key = extractKeyFromNetwork(network);
        return new Tuple2<>(key, Arrays.asList(network));
    }

    private String extractKeyFromNetwork(Network network) {
        StringBuilder key = new StringBuilder();
        int[] numberOfOnesCluster = getNumberOfOnesPerCluster(network.getOutputSet());
        for(int i=0;i<numberOfOnesCluster.length;i++) {
            key.append(String.valueOf(numberOfOnesCluster[i])).append("_");
        }
        return key.toString();
    }

    private int[] getNumberOfOnesPerCluster(Set<Output> outputSet) {
        int numberOfOnesCluster[] = new int[NetworkProperties.NUMBER_OF_WIRES+1];
        for(Output output : outputSet) {
            int numberOfOnes = getNumberOfOnesInOutput(output);
            numberOfOnesCluster[numberOfOnes]++;
        }
        return numberOfOnesCluster;
    }

    private int getNumberOfOnesInOutput(Output output) {
        return output.getValues().cardinality();
    }
}
