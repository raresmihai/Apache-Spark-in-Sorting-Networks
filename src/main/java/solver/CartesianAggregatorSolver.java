package solver;

import spark_functions.*;
import network.Network;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import util.SequentiallyGenerator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Generate and prune approach that uses the cartesian product between the networks in order to be able to compare them and check for subsumption.
 */
public class CartesianAggregatorSolver implements Solver, Serializable {

    /**
     * @param sc The spark context that will be used to parallelize data and communicate with the master.
     *
     * Finds a sorting network using the cartesian product to prune the networks.
     *
     * In order to have enough data to make the algorithm parallel, some sequentially steps of generation are first done before the parallelization of networks.
     * <br />
     * See: {@link util.SequentiallyGenerator SequentiallyGenerator}
     *
     * <h2>GENERATE STEP</h2>
     * Start from the empty network and then incrementally add new comparators to the current networks in a BFS manner.
     * This is called the generate step and is done using the FlatMapGenerator.
     * <br />
     * See: {@link spark_functions.FlatMapGenerator FlatMapGenerator}
     *
     * <h2>PRUNING STEP</h2>
     * <ol>
     *     <li>
     *         After comparators are added, the networks RDD will be transformed into a (Key,Value) PairRDD of form (String,List<Network>),
     *         where the Key is a string that represents the number of outputs in each cluster of the outputSet and the Value is a list composed only of the network represented by the key.
     *         <br />
     *         See: {@link spark_functions.MapToKeyNetworkPair MapToKeyNetworkPair}
     *     </li>
     *     <li>
     *         All the singleton lists with the same key are reduced to only one list containing only the minimal networks that subsume all the other networks that were pruned.
     *         spark_functions.SameKeyPruning
     *         <br />
     *         See: {@link util.Subsumption Subsumption}
     *     </li>
     *     <li>
     *         The lists for each key are flat mapped to only one big RDD containing all the minimal networks for each different key.
     *     </li>
     *     <li>
     *         The cartesian product is done between the current set of networks and itself, in order to test each network with all the other networks for subsumtion.
     *         This basically simulates 2 for loops.
     *         A PairRDD of (Network1,Network2) is obtained where Network1 is the key and will be checked if it's not subsumed by Network2.
     *         n^2 pairs are obtained, with n pairs for each different network.
     *         Where n is the initial number of networks (before the cartesian product).
     *     </li>
     *     <li>
     *         Each pair (Network1,Network2) will be maped to a pair of (Network1,Boolean).
     *         The boolean flag will be true if after pruning Network1 will be kept (e.g. Network1 is not subsumed by Network2), and false otherwise.
     *         <br />
     *         See: {@link spark_functions.PruningMap PruningMap}, {@link util.Subsumption Subsumption}
     *     </li>
     *     <li>
     *         A network will be kept (will not be pruned) iff the network is not subsumed by any of the other networks.
     *         This means that a network <b>N</b> will be kept iff the network has the flag <i>true</i> for each pair (N,Ni).
     *         So a network will be kept if the result of the AND operation between all the flags for that network is true.
     *         This is achieved using a reduceByKey transformation that uses the lambda expression (a,b) -> a & b.
     *         (a and b are boolean flags for the same network and the result is the AND operation between those flags).
     *     </li>
     *     <li>
     *         The final pruned networks for the current step (the current number of comparators)
     *         are the ones with boolean flag true (after the AND operation).
     *         This is achieved using a filter lambda expression of form (tuple -> tuple._2()).
     *         (tuple is a pair of form (Network,Boolean) and will be kept iff the boolean flag is true).     *
     *     </li>
     *     <li>
     *         In order to advance to the next step (the next number of comparators)
     *         only the networks (the keys) will be kept from the PairRDD of (Network,Boolean).     *
     *     </li>
     * </ol>
     *
     */
    @Override
    public void solve(JavaSparkContext sc) {
        List<Network> data = Arrays.asList(new Network());
        SequentiallyGenerator sequentiallyGenerator = new SequentiallyGenerator();
        for(int i=0;i<NUMBER_OF_SEQ_GENERATE_STEPS;i++) {
            data = sequentiallyGenerator.addComparatorsToNetworks(data);
        }

        JavaRDD<Network> currentN = sc.parallelize(data);
        for(int k = NUMBER_OF_SEQ_GENERATE_STEPS; k< NUMBER_OF_COMPARATORS; k++) {
            System.out.println("Step " + k);
            currentN = currentN.flatMap(new FlatMapGenerator());
            JavaPairRDD<String, List<Network>> keyPairs = currentN.mapToPair(new MapToKeyNetworkPair());
            //keyPairs.collect().forEach(System.out::println);
            keyPairs = keyPairs.reduceByKey(new SameKeyPruning());
            JavaRDD<List<Network>> networksAfterPruning = keyPairs.values();
            currentN = networksAfterPruning.flatMap(networks -> networks.iterator());
            JavaPairRDD<Network,Network> allPairs = currentN.cartesian(currentN);
            System.out.println("Cartesian: " + allPairs.count());
            JavaPairRDD<Network,Boolean> prunedNetworksWithFlags = allPairs.mapToPair(new PruningMap());
            JavaPairRDD<Network,Boolean> prunedNetworksWithAggregatedFlags = prunedNetworksWithFlags.reduceByKey((a,b) -> a & b);
            System.out.println("Dupa reduce: " + prunedNetworksWithAggregatedFlags.count());
            //System.out.println("Aggregated: " + prunedNetworksWithAggregatedFlags.count() + ": " + prunedNetworksWithAggregatedFlags.take(10));
            JavaPairRDD<Network,Boolean> prunedNetworks = prunedNetworksWithAggregatedFlags.filter(tuple -> tuple._2());
            currentN = prunedNetworks.keys();
        }

        System.out.println(currentN.count());
        //currentN.collect().forEach(System.out::println);

        //Get only the sorting networks using a filter
        JavaRDD<Network> filteredNetworks = currentN.filter(new SnFilter());
        System.out.println(filteredNetworks.count());
        filteredNetworks.take(1).forEach(System.out::println);

        sc.stop();
    }
}
