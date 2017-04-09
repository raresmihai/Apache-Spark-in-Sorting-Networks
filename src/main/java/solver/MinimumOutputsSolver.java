package solver;

import network.Network;
import network.NetworkComparator;
import network.NetworkProperties;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
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
        int minsPercentages[] = {2,2,2,2,2,5,3,8,4,2,2,6,3,3,6,3,2,3,2,2,2,2,2,2,2,2};

//        Network n = new Network(new Comparator(1,2)); // 1/1
//        n = new Network(n,new Comparator(3,4));// 1/2
//        n = new Network(n,new Comparator(5,6));// 1/3
//        n = new Network(n,new Comparator(7,8));// 1/3
//        n = new Network(n,new Comparator(2,4));// 2/2
//        n = new Network(n,new Comparator(6,8));//5
//        n = new Network(n,new Comparator(2,6));//3
//        n = new Network(n,new Comparator(4,8));//8
//        n = new Network(n,new Comparator(2,9));//4
//        n = new Network(n,new Comparator(1,3));//1
//        n = new Network(n,new Comparator(5,7));//1
//        n = new Network(n,new Comparator(1,5));//6
//        n = new Network(n,new Comparator(3,7));//3
//        n = new Network(n,new Comparator(3,5));//3
//        n = new Network(n,new Comparator(2,3));//6
//        n = new Network(n,new Comparator(4,6));//3
//        n = new Network(n,new Comparator(5,9));//2
//        n = new Network(n,new Comparator(3,5));// 3/3
//        n = new Network(n,new Comparator(7,9));// 1/3
//        n = new Network(n,new Comparator(6,9));// 2/2
//        n = new Network(n,new Comparator(4,7));// 1/1
//        n = new Network(n,new Comparator(1,2));// 1/1
//        n = new Network(n,new Comparator(4,5));// 1/1
        //n = new Network(n,new Comparator(6,7));// 1/1
        //n = new Network(n,new Comparator(8,9));// 1/1
//        Network n12 = new Network(n,new Comparator(1,2));
//        System.out.println("n12: " + n12.getOutputSet().size());
//        Network n34 = new Network(n,new Comparator(3,4));
//        System.out.println("n34: " + n34.getOutputSet().size());
//        Network n56 = new Network(n,new Comparator(5,6));
//        System.out.println("n56: " + n56.getOutputSet().size());
//        Network n78 = new Network(n,new Comparator(7,8));
//        System.out.println("n78:|" + n78.getOutputSet().size());
//        Network n24 = new Network(n,new Comparator(2,4));
//        System.out.println("n24: " + n24.getOutputSet().size());
//        Network n68 = new Network(n,new Comparator(6,8));
//        System.out.println("n68: " + n68.getOutputSet().size());
//        Network n26 = new Network(n,new Comparator(2,6));
//        System.out.println("n26: " + n26.getOutputSet().size());
//        Network n48 = new Network(n,new Comparator(4,8));
//        System.out.println("n48: " + n48.getOutputSet().size());
//        Network n29 = new Network(n,new Comparator(2,9));
//        System.out.println("n29: " + n29.getOutputSet().size());
//        Network n13 = new Network(n,new Comparator(1,3));
//        System.out.println("n13: " + n13.getOutputSet().size());
//        Network nl = new Network(n,new Comparator(5,7));
//        System.out.println("57: " + nl.getOutputSet().size());
//        Network nlast = new Network(n,new Comparator(1,5));
//        System.out.println("15: " + nlast.getOutputSet().size());
//        Network nn = new Network(n,new Comparator(3,7));
//        System.out.println("37: " + nn.getOutputSet().size());
//        Network n_ = new Network(n,new Comparator(3,5));
//        System.out.println("35: " + n_.getOutputSet().size());
//        Network n_23 = new Network(n,new Comparator(2,3));
//        Network n_46 = new Network(n,new Comparator(4,6));
//        System.out.println("n23: " + n_23.getOutputSet().size());
//        System.out.println("n46: " + n_46.getOutputSet().size());
//        Network n_23_46 = new Network(n_23,new Comparator(4,6));
//        Network n_46_49 = new Network(n_46,new Comparator(4,9));
//        System.out.println("n_23_46: " + n_23_46.getOutputSet().size());
//        System.out.println("n_46_49: " + n_46_49.getOutputSet().size());
//        Network n_23_46_59 = new Network(n_23_46,new Comparator(5,9));
//        Network n_46_49_79 = new Network(n_46_49,new Comparator(7,9));
//        System.out.println("n_23_46_59: " + n_23_46_59.getOutputSet().size());
//        System.out.println("n_46_49_79: " + n_46_49_79.getOutputSet().size());
//        Network n_23_46_59_35 = new Network(n_23_46_59,new Comparator(5,9));
//        Network n_46_49_79_45 = new Network(n_46_49_79,new Comparator(7,9));
//        System.out.println("n_23_46_59_35: " + n_23_46_59_35.getOutputSet().size());
//        System.out.println("n_46_49_79_45: " + n_46_49_79_45.getOutputSet().size());
//
//        Network n12 = new Network(n,new Comparator(6,7));
//        System.out.println("n: " + n12.getOutputSet().size());


        //System.out.println(n);
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
            keyPairs = keyPairs.reduceByKey(new SameKeyPruning());


            JavaRDD<List<Network>> networksAfterPruning = keyPairs.values();
            List<Network> reducedNetworks = networksAfterPruning.reduce(new DifferentKeyPruning());
            currentN = sc.parallelize(reducedNetworks);

            System.out.println("After pruning: " + currentN.count());
            System.out.println("------------------------------------------\n");
            //initial, fara different
//            JavaRDD<List<Network>> networksAfterPruning = keyPairs.values();
//            currentN = networksAfterPruning.flatMap(l -> l.iterator());
            //System.out.println(currentN.count());
        }

        //System.out.println(currentN.collect().forEach(System.out::println);
        //currentN.collect().forEach(System.out::println);

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

    int maximum(int a, int b) {
        return a>b?a:b;
    }
}
