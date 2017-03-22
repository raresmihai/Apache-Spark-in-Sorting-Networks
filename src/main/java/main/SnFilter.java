package main;

import network.Network;
import org.apache.spark.api.java.function.Function;

import java.io.Serializable;

/**
 * Created by Rares on 19.03.2017.
 */
public class SnFilter implements Function<Network,Boolean>,Serializable {

    @Override
    public Boolean call(Network network) throws Exception {
        return network.getOutputSet().size() == NetworkProperties.NUMBER_OF_WIRES + 1;
    }
}
