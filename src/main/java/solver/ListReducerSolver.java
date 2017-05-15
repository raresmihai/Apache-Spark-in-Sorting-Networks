package solver;

import network.Network;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import spark_functions.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Generate and prune approach that uses the reduce action between list of networks.
 */
public class ListReducerSolver implements Solver,Serializable {
    /**
     * @param sc The spark context that will be used to parallelize data and communicate with the master.
     *
     * Finds a sorting network using the reduce action to prune the networks.
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
     *         <br />
     *         See: {@link spark_functions.SameKeyPruning SameKeyPruning} , {@link util.Subsumption Subsumption}
     *     </li>
     *     <li>
     *         A new RDD of lists of networks is created by taking only the values from the PairRDD <br />
     *         (only the lists containing the minimal networks for each key).
     *     </li>
     *     <li>
     *         A similar approach to the same key pruning is done, but this time between the lists of networks with different keys (with different outputClusters).
     *         Having two lists of networks, l1 and l2, each network from l1 will be checked to see if it's not subsumed by the networks in l2.
     *         From l1 only the networks that are not subsumed by networks from l2 will be kept.
     *         After that, each network from l2 will be checked to see if it's not subsumed by the remaining networks from l1.
     *         <b>Note</b> <br />
     *         "reduce is an action that aggregates all the elements of the RDD using some function and returns the final result to the driver program" <br />
     *         The reduce action transforms an RDD (a distributed data set) into a List, and sends the list to the driver program. <br />
     *         Because the List of networks can be huge, this may result in a OutOfMemory Exception. <br />
     *         This is why, it may be a good idea to use reduceByKey which returns a Distributed data set. <br />
     *         To use reduceByKey, all that needs to be done is to transform the RDD of networks into a PairRDD (Key,List<Network>). <br />
     *         Each list of networks will have the same key "k".
     *         See: {@link ListPruning ListPruning} , {@link util.Subsumption Subsumption}
     *     </li>
     * </ol>
     *
     */
    @Override
    public void solve(JavaSparkContext sc) {
        List<Network> data = Arrays.asList(new Network());

        JavaRDD<Network> currentN = sc.parallelize(data);
        for(int k = 0; k< NUMBER_OF_COMPARATORS; k++) {
            System.out.println("Step " + k);
            currentN = currentN.flatMap(new FlatMapGenerator());
            System.out.println("FULL: " + currentN.count());
            JavaPairRDD<String,List<Network>> keyPairs = currentN.mapToPair(new MapToKeyNetworkPair());
            //keyPairs.collect().forEach(System.out::println);
            keyPairs = keyPairs.reduceByKey(new ListPruning());
            JavaRDD<List<Network>> networksAfterPruning = keyPairs.values();
            //System.out.println("After same key pruning");
            //networksAfterPruning.collect().forEach(System.out::println);
            List<Network> reducedNetworks = networksAfterPruning.reduce(new ListPruning());
            //System.out.println("After different key reduce");
            System.out.println("Reduced:" + reducedNetworks.size());
            currentN = sc.parallelize(reducedNetworks);

            //Pair Reduce By Key in order to keep the networks distributed
//            keyPairs = networksAfterPruning.mapToPair(l -> Tuple2.apply("k",l));
//            JavaPairRDD<String, List<Network>> reducedNetworks = keyPairs.reduceByKey(new ListPruning());
//            JavaRDD<List<Network>> values = reducedNetworks.values();
//            currentN = values.flatMap(networks -> networks.iterator());
        }

        System.out.println(currentN.count());
        //currentN.collect().forEach(System.out::println);

        //Get only the sorting networks using a filter
        JavaRDD<Network> filteredNetworks = currentN.filter(new SnFilter());
        filteredNetworks.take(10).forEach(System.out::println);
        sc.stop();
    }
}
