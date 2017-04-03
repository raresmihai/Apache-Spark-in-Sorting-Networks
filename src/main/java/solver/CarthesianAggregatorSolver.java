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
 * Created by Rares on 03.04.2017.
 */
public class CarthesianAggregatorSolver implements Solver, Serializable {
    @Override
    public void solve(JavaSparkContext sc) {
        List<Network> data = Arrays.asList(new Network());
        SequentiallyGenerator sequentiallyGenerator = new SequentiallyGenerator();
        for(int i=0;i<NUMBER_OF_SEQ_GENERATE_STEPS;i++) {
            data = sequentiallyGenerator.addComparatorsToNetworks(data);
        }

        JavaRDD<Network> currentN = sc.parallelize(data);
        for(int k = NUMBER_OF_SEQ_GENERATE_STEPS; k< NUMBER_OF_COMPARATORS; k++) {
            //System.out.println("Step " + k);
            currentN = currentN.flatMap(new FlatMapGenerator());
            JavaPairRDD<String, List<Network>> keyPairs = currentN.mapToPair(new MapToKeyNetworkPair());
            //keyPairs.collect().forEach(System.out::println);
            keyPairs = keyPairs.reduceByKey(new SameKeyPruning());
            JavaRDD<List<Network>> networksAfterPruning = keyPairs.values();
            currentN = networksAfterPruning.flatMap(networks -> networks.iterator());
            JavaPairRDD<Network,Network> allPairs = currentN.cartesian(currentN);
            //System.out.println("Cartesian: " + allPairs.count());
            JavaPairRDD<Network,Boolean> prunedNetworksWithFlags = allPairs.mapToPair(new PruningMap());
            JavaPairRDD<Network,Boolean> prunedNetworksWithAggregatedFlags = prunedNetworksWithFlags.reduceByKey((a,b) -> a & b);
            //System.out.println("Aggregated: " + prunedNetworksWithAggregatedFlags.count() + ": " + prunedNetworksWithAggregatedFlags.take(10));
            JavaPairRDD<Network,Boolean> prunedNetworks = prunedNetworksWithAggregatedFlags.filter(tuple -> tuple._2());
            currentN = prunedNetworks.keys();
        }

        System.out.println(currentN.count());
        //currentN.collect().forEach(System.out::println);

        //Get only the sorting networks using a filter
        JavaRDD<Network> filteredNetworks = currentN.filter(new SnFilter());
        filteredNetworks.take(10).forEach(System.out::println);

        sc.stop();
    }
}
