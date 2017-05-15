package solver;

import network.Network;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import spark_functions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rares on 05.04.2017.
 */
public class MinimumOutputsSolver implements Solver, Serializable {
    @Override
    public void solve(JavaSparkContext sc) {
//        List<Network> data = Arrays.asList(new Network());
//        SequentiallyGenerator sequentiallyGenerator = new SequentiallyGenerator();
//        for(int i=0;i<NUMBER_OF_SEQ_GENERATE_STEPS;i++) {
//            data = sequentiallyGenerator.addComparatorsToNetworks(data);
//        }
        //int minsPercentages[] = {1,1,1,1,2,5,3,8,4,1,1,6,3,3,6,3,2,3,1,2,2,2,2,2,2,2};
        //int minsPercentages[] = {2,2,2,2,2,5,3,8,4,2,2,6,3,3,6,3,2,3,2,2,2,2,2,2,2,2};
        int minsPercentages[] = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
        List<Network> data = Arrays.asList(new Network());
        JavaRDD<Network> currentN = sc.parallelize(data);
        for(int k = NUMBER_OF_SEQ_GENERATE_STEPS; k< NUMBER_OF_COMPARATORS; k++) {
            System.out.println("Step " + k);
            currentN = currentN.flatMap(new FlatMapGenerator());
            System.out.println("After generate: " + currentN.count());

            JavaPairRDD<Integer,Network> networksWithOutputSize = currentN.mapToPair(new OutputSizeMap());
            JavaRDD<Integer> minsRDD = networksWithOutputSize.keys();
            List<Integer> mins = new ArrayList<>();
            int numberOfMins = minsPercentages[k];
            for(int i=0;i<numberOfMins;i++) {
                Integer min = minsRDD.min(new IntegerComparator());
                mins.add(min);
                minsRDD = minsRDD.filter(integer -> !integer.equals(min));
                if(minsRDD.isEmpty()) {
                    break;
                }
            }
            System.out.println("Mins selected:" + mins.size());

            networksWithOutputSize = networksWithOutputSize.filter(tuple -> inMins(mins,tuple._1()));
            currentN = networksWithOutputSize.values();

            System.out.println("Before pruning: " + currentN.count());
            JavaPairRDD<String,List<Network>> keyPairs = currentN.mapToPair(new MapToKeyNetworkPair());
            keyPairs = keyPairs.reduceByKey(new ListPruning());


            JavaRDD<List<Network>> networksAfterPruning = keyPairs.values();
            List<Network> reducedNetworks = networksAfterPruning.reduce(new ListPruning());
            currentN = sc.parallelize(reducedNetworks);

            System.out.println("After pruning: " + currentN.count());
            System.out.println("------------------------------------------\n");
        }

        //Get only the sorting networks using a filter
        JavaRDD<Network> filteredNetworks = currentN.filter(new SnFilter());
        System.out.println("Filter : " + filteredNetworks.count());
        filteredNetworks.take(10).forEach(System.out::println);
        sc.stop();
    }

    boolean inMins(List<Integer> mins, Integer i) {
        for(Integer min : mins) {
            if(i.equals(min)) {
                return true;
            }
        }
        return false;
    }

}


