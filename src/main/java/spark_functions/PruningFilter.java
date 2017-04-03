package spark_functions;

import network.Network;
import network.Output;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Rares on 22.03.2017.
 */
public class PruningFilter implements Function<Tuple2<Network, Network>,Boolean>,Serializable {


    @Override
    public Boolean call(Tuple2<Network, Network> newtworkPair) throws Exception {
        return true;
    }


}
