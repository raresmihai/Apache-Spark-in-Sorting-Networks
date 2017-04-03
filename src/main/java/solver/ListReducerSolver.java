package solver;

import network.Network;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaPairRDD$;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import solver.Solver;
import spark_functions.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rares on 03.04.2017.
 */
public class ListReducerSolver implements Solver,Serializable {
    @Override
    public void solve(JavaSparkContext sc) {
        List<Network> data = Arrays.asList(new Network());

        JavaRDD<Network> currentN = sc.parallelize(data);
        for(int k = 0; k< NUMBER_OF_COMPARATORS; k++) {
            System.out.println("Step " + k);
            currentN = currentN.flatMap(new FlatMapGenerator());
            JavaPairRDD<String,List<Network>> keyPairs = currentN.mapToPair(new MapToKeyNetworkPair());
            //keyPairs.collect().forEach(System.out::println);
            keyPairs = keyPairs.reduceByKey(new SameKeyPruning());
            JavaRDD<List<Network>> networksAfterPruning = keyPairs.values();
            //System.out.println("After same key pruning");
            //networksAfterPruning.collect().forEach(System.out::println);
            ///List<Network> reducedNetworks = networksAfterPruning.reduce(new DifferentKeyPruning());
            //System.out.println("After different key reduce");
            //System.out.println(reducedNetworks);

            keyPairs = networksAfterPruning.mapToPair(l -> Tuple2.apply("k",l));
            JavaPairRDD<String, List<Network>> reducedNetworks = keyPairs.reduceByKey(new DifferentKeyPruning());
            JavaRDD<List<Network>> values = reducedNetworks.values();
            currentN = values.flatMap(networks -> networks.iterator());


            //currentN = sc.parallelize(reducedNetworks);
        }

        System.out.println(currentN.count());
        //currentN.collect().forEach(System.out::println);

        //Get only the sorting networks using a filter
        JavaRDD<Network> filteredNetworks = currentN.filter(new SnFilter());
        filteredNetworks.take(10).forEach(System.out::println);
    }
}
