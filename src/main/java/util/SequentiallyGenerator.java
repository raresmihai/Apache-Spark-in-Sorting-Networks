package util;

import network.Comparator;
import network.Network;
import network.NetworkProperties;
import network.OutputUtil;

import java.util.*;

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

        public List<Network> addComparatorsToNetwork(Network network)  {
            List<Comparator> comparators = getAllComparators(NetworkProperties.NUMBER_OF_WIRES);
            List<Network> newNetworks = new ArrayList<>();

            for(Comparator c : comparators) {
                if(!OutputUtil.isRedundant(network.getOutputSet(),c)) {
                    Network newNetwork = new Network(network,c);
                    newNetworks.add(newNetwork);
                }
            }
            return newNetworks;
        }

        public List<Network> addComparatorsToNetworks(List<Network> networks) {
            List<Network> newNetworks = new ArrayList<>();
            for(Network network : networks) {
                newNetworks.addAll(addComparatorsToNetwork(network));
            }
            return newNetworks;
        }

//        public List<Network> prune(List<Network> networks) {
//            PruningMap pm = new PruningMap();
//            Map<Network,Integer> map = new HashMap<>();
//            Set<Network> hash = new HashSet<>();
//            for(Network network1 : networks) {
//                for(Network network2 : networks) {
//                    if(network1.equals(network2) || !pm.subsumes(network2,network1)) {
//                        if(map.containsKey(network1)) {
//                            map.put(network1,map.get(network1)+1);
//                        } else {
//                            map.put(network1,0);
//                        }
//                    } else {
//                        hash.add(network1);
//                    }
//                }
//            }
//            List<Network> prunnedNetworks  = new ArrayList<>();
//            for(Network network : networks) {
//                if(!hash.contains(network)) {
//                    prunnedNetworks.add(network);
//                }
//            }
//            if(prunnedNetworks.size()==0) {
//                Map.Entry<Network, Integer> maxEntry = null;
//
//                for (Map.Entry<Network,Integer> entry : map.entrySet())
//                {
//                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
//                    {
//                        maxEntry = entry;
//                    }
//                }
//                //System.out.println("MaxEntry:" + maxEntry.getKey() + "  " + maxEntry.getValue());
//                prunnedNetworks.add(maxEntry.getKey());
//            }
//            return prunnedNetworks;
//        }
}
