package spark_functions;

import network.Comparator;
import network.Network;
import network.NetworkProperties;
import network.OutputUtil;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FlatMapGenerator implements FlatMapFunction<Network,Network>, Serializable {
    @Override
    public Iterator<Network> call(Network network) throws Exception {
        List<Comparator> comparators = getAllComparators(NetworkProperties.NUMBER_OF_WIRES);
        List<Network> newNetworks = new ArrayList<>();
        Logger log = LogManager.getRootLogger();
        log.setLevel(Level.WARN);
        if(comparators.get(0) == null) {
            log.warn(network);
        }
        for(Comparator c : comparators) {
            if(!OutputUtil.isRedundant(network.getOutputSet(),c)) {
                Network newNetwork = new Network(network,c);
                newNetworks.add(newNetwork);
            }
        }
        return newNetworks.iterator();
    }

    private List<Comparator> getAllComparators(int n) {
        List<Comparator> comparators = new ArrayList<>();
        for(int i=1;i<n;i++) {
            for(int j=i+1;j<=n;j++) {
                Comparator c = new Comparator(i,j);
                comparators.add(c);
            }
        }
        return comparators;
    }
}
