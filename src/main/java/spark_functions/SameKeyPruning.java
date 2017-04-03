package spark_functions;

import util.Subsumption;
import network.Network;
import org.apache.spark.api.java.function.Function2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rares on 23.03.2017.
 * This reduce function will be called between Networks with the same key
 * (e.g. networks with the same number of outputs and the same numbers of ones in each output cluster)
 * Having the same key, if n1 subsumes n2 => n2 subsumes n1
 * *
 */
public class SameKeyPruning implements Function2<List<Network>,List<Network>,List<Network>> {

    /**
     *
     * @param networks1
     * @param networks2
     * @return a list of Networks extended with the Network in the one element List if that element is not subsumed
     * by any other element from the bigger list,
     * otherwise return the current list of Networks.
     *
     * Loop through the list of netorks and check if there exists a network that subsumes currentNetwork.
     * If yes, then this means that currentNetwork can be pruned.
     */
    @Override
    public List<Network> call(List<Network> networks1, List<Network> networks2)  {
        List<Network> allNetworks;
        Network currentNetwork;
        if(networks1.size() > networks2.size()) {
            allNetworks = networks1;
            currentNetwork = networks2.get(0);
        } else {
            allNetworks = networks2;
            currentNetwork = networks1.get(0);
        }

        List<Network> newNetworks = new ArrayList<>();
        newNetworks.addAll(allNetworks);

        Subsumption subsumptionTest = new Subsumption();
        for (Network network : allNetworks) {
            if (subsumptionTest.subsumes(network, currentNetwork)) {
                return newNetworks;
            }
        }
        newNetworks.add(currentNetwork);
        return newNetworks;
    }
}
