package spark_functions;

import network.Network;
import org.apache.spark.api.java.function.Function2;
import util.Subsumption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rares on 31.03.2017.
 */
public class DifferentKeyPruning implements Function2<List<Network>,List<Network>,List<Network>> {
    @Override
    public List<Network> call(List<Network> networks1, List<Network> networks2) throws Exception {
        List<Network> remainingNetworksAfterPruning = new ArrayList<>();
        remainingNetworksAfterPruning.addAll(getBestNetworks(networks1,networks2));
        remainingNetworksAfterPruning.addAll(getBestNetworks(networks2,remainingNetworksAfterPruning));
        return remainingNetworksAfterPruning;
    }

    private List<Network> getBestNetworks(List<Network> networks1, List<Network> networks2) {
        Subsumption subsumptionTest = new Subsumption();
        List<Network> bestNetworks = new ArrayList<>();
        for(Network network1 : networks1) {
            boolean keepNetwork1 = true;
            for(Network network2 : networks2) {
                if(subsumptionTest.subsumes(network2,network1)) {
                    keepNetwork1 = false;
                    break;
                }
            }
            if(keepNetwork1) {
                bestNetworks.add(network1);
            }
        }
        return bestNetworks;
    }
}
