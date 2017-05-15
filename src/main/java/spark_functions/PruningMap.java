package spark_functions;

import network.Network;
import network.NetworkProperties;
import network.Output;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import util.Subsumption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Rares on 22.03.2017.
 *
 */
public class PruningMap implements PairFunction<Tuple2<Network,Network>, Network,Boolean>,Serializable {
    /*
    <Network1,Network2> remains iff Network2 <= Network1 is false (Network1 is not subsumed by Network2)
    Network1 is the key and will be kept iff Network2 <= Network1 is false (Network1 is not subsumed by Network2)
    The boolean flag in the tuple says if Network1 will be kept or no
     */
    @Override
    public Tuple2<Network, Boolean> call(Tuple2<Network, Network> networkPair) throws Exception {
        Network network1 = networkPair._1();
        Network network2 = networkPair._2();
        if(network1.equals(network2)) {
            return new Tuple2<>(network1,true);
        }
        Subsumption subsumptionTest = new Subsumption();
        return new Tuple2<>(network1,!subsumptionTest.subsumes(network2,network1));
    }
}
