package spark_functions;

import network.Network;
import org.apache.spark.api.java.function.Function2;
import util.Subsumption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rares on 31.03.2017.
 */
public class ListPruning implements Function2<List<Network>,List<Network>,List<Network>> {
    @Override
    public List<Network> call(List<Network> networks1, List<Network> networks2) throws Exception {
        List<Network> remainingNetworksAfterPruning = new ArrayList<>();
        remainingNetworksAfterPruning.addAll(getMinimalNetworks(networks1,networks2));
        remainingNetworksAfterPruning.addAll(getMinimalNetworks(networks2,remainingNetworksAfterPruning));
        return remainingNetworksAfterPruning;
    }

    private List<Network> getMinimalNetworks(List<Network> networks1, List<Network> networks2) {
        Subsumption subsumptionTest = new Subsumption();
        List<Network> minimalNetworks = new ArrayList<>();
        for(Network network1 : networks1) {
            boolean keepNetwork1 = true;
            for(Network network2 : networks2) {
                if(subsumptionTest.subsumes(network2,network1)) {
                    keepNetwork1 = false;
                    break;
                }
            }
            if(keepNetwork1) {
                minimalNetworks.add(network1);
            }
        }
        return minimalNetworks;
    }
}
