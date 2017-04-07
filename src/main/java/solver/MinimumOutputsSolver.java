package solver;

import network.Network;
import network.NetworkProperties;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;
import spark_functions.*;

import java.io.Serializable;
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
        int minsPercentages[] = {1,1,1,1,2,5,3,8,4,1,1,6,3,3,6,3,2,3,1,2,2,2,2,2,2,2};
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
            //System.out.println("After flatmap: " + currentN.count());
            JavaPairRDD<Integer,Network> networksWithOutputSize = currentN.mapToPair(new OutputSizeMap());
            //networksWithOutputSize.collect().forEach(System.out::println);
            JavaRDD<Integer> minsRDD = networksWithOutputSize.keys();
            minsRDD = minsRDD.distinct();//in loc de distinct, se poate incerca un reduceByKey care sa nu faca nimic la valori, doar sa obtina valori distincte pt chei
            minsRDD = minsRDD.sortBy(new SortingFunction(),true, 32);
            //System.out.println("Total mins: " + minsRDD.count());
            //networksWithOutputSize = networksWithOutputSize.sortByKey(true);
            //int percentage = maximum((int)(1 * minsRDD.count() / 100),2);
            //System.out.println(percentage);
            List<Integer> mins = minsRDD.take(minsPercentages[k]);

            System.out.println(mins);
            //Tuple2<Integer,Network> minimum = networksWithOutputSize.min(new NetworkComparator());
            //Integer minOutputSize = minimum._1();
            //System.out.println("Minimum: " + minOutputSize);
            networksWithOutputSize = networksWithOutputSize.filter(tuple -> inMins(mins,tuple._1()));
            currentN = networksWithOutputSize.values();
            //System.out.println("Dupa filtrare mins: " + currentN.count());


            JavaPairRDD<String,List<Network>> keyPairs = currentN.mapToPair(new MapToKeyNetworkPair());
            //keyPairs.collect().forEach(System.out::println);
            System.out.println("Before same: " + keyPairs.count());
            keyPairs = keyPairs.reduceByKey(new SameKeyPruning());
            JavaRDD test = keyPairs.values().flatMap(l -> l.iterator());
            System.out.println("After same: " + test.count());

            keyPairs = keyPairs.values().mapToPair(l -> Tuple2.apply("k",l));
            JavaPairRDD<String, List<Network>> reducedNetworks = keyPairs.reduceByKey(new DifferentKeyPruning());
            JavaRDD<List<Network>> values = reducedNetworks.values();
            currentN = values.flatMap(networks -> networks.iterator());
            System.out.println("Dupa different: " + currentN.count());

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
