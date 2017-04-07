package spark_functions;

import network.Network;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

/**
 * Created by Rares on 05.04.2017.
 */
public class OutputSizeMap implements PairFunction<Network, Integer, Network> {
    @Override
    public Tuple2<Integer, Network> call(Network network) throws Exception {
        return new Tuple2<>(network.getOutputSet().size(),network);
    }
}
