package spark_functions;

import network.Network;
import org.apache.spark.api.java.function.Function2;

import java.util.List;

/**
 * Created by Rares on 05.04.2017.
 */
public class KeepOneNetworkReducer implements Function2<List<Network>,List<Network>,List<Network>> {
    @Override
    public List<Network> call(List<Network> networks1, List<Network> networks2) throws Exception {
        return networks1;
    }
}
