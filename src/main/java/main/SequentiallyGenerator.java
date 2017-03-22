package main;

import network.Comparator;
import network.Network;
import network.OutputUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rares on 22.03.2017.
 */
public class SequentiallyGenerator {
        List<Comparator> getAllComparators(int n) {
            List<Comparator> comparators = new ArrayList<>();
            for(int i=1;i<n;i++) {
                for(int j=i+1;j<=n;j++) {
                    Comparator c = new Comparator(i,j);
                    comparators.add(c);
                }
            }
            return comparators;
        }

        List<Network> addComparatorsToNetwork(Network network)  {
            List<Comparator> comparators = getAllComparators(NetworkProperties.NUMBER_OF_WIRES);
            List<Network> newNetworks = new ArrayList<>();

            for(Comparator c : comparators) {
                if(!OutputUtil.isRedundant(network,c)) {
                    Network newNetwork = new Network(network,c);
                    newNetworks.add(newNetwork);
                }
            }
            return newNetworks;
        }

        List<Network> addComparatorsToNetworks(List<Network> networks) {
            List<Network> newNetworks = new ArrayList<>();
            for(Network network : networks) {
                newNetworks.addAll(addComparatorsToNetwork(network));
            }
            return newNetworks;
        }
}
