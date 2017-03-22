package main;
import network.*;
import network.Comparator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import scala.Tuple2;

import java.io.*;
import java.util.*;

//6 wires : (2,4) (4,6) (2,4) (1,5) (3,5) (5,6) (1,3) (3,4) (4,5) (1,2) (2,4) (2,3) -> 19 min on 4 Cores,1GB RAM
public class Main implements Serializable{
    final static Logger log = LogManager.getRootLogger();
    private final static int NUMBER_OF_SEQ_GENERATE_STEPS = 0;
    private final static int NUMBER_OF_SPLITS = 8;

    public static void main(String[] args){
        log.setLevel(Level.WARN);

        //initialize the SparkContext
        SparkConfSetter sparkConfSetter = new SparkConfSetter();
        SparkConf conf = sparkConfSetter.getSparkConf();
        JavaSparkContext sc = new JavaSparkContext(conf);

        //Sequentially doing some steps of the generate phase so there will be enough data to parallelize
        List<Network> data = Arrays.asList(new Network());
        SequentiallyGenerator sequentiallyGenerator = new SequentiallyGenerator();
        for(int i=0;i<NUMBER_OF_SEQ_GENERATE_STEPS;i++) {
            data = sequentiallyGenerator.addComparatorsToNetworks(data);
        }

        //Parallel generate
        JavaRDD<Network> currentN = sc.parallelize(data);
        for(int k=NUMBER_OF_SEQ_GENERATE_STEPS;k<NetworkProperties.NUMBER_OF_COMPARATORS;k++) {
            System.out.println("k: " + k);
            currentN = currentN.flatMap(new MyFlatMap());
            //System.out.println("Full: " + currentN.count());
            JavaPairRDD<Network,Network> allPairs = currentN.cartesian(currentN);
            JavaPairRDD<Network,Boolean> prunedNetworksWithFlags = allPairs.mapToPair(new PruningMap());
            JavaPairRDD<Network,Boolean> prunedNetworksWithAggregatedFlags = prunedNetworksWithFlags.reduceByKey((a,b) -> a & b);
            JavaPairRDD<Network,Boolean> prunedNetworks = prunedNetworksWithAggregatedFlags.filter(tuple -> tuple._2());
            currentN = prunedNetworks.keys();
            if(currentN.isEmpty()) {
                JavaPairRDD<Network,Integer>  aggregatedByInt;
                aggregatedByInt = prunedNetworksWithFlags.aggregateByKey(0,(a,b)->a+(b?1:0),(a,b)->a+b);
                Tuple2 bestNetwork = aggregatedByInt.max(new NetworkComparator());
                currentN = sc.parallelize(Arrays.asList((Network)bestNetwork._1()));
            }
            //System.out.println("Red: " + currentN.count());
            //System.out.println("----------------------------------------------------");
        }

        //System.out.println(currentN.count());
        currentN.collect().forEach(System.out::println);

        //Get only the sorting networks using a filter
        //JavaRDD<Network> filteredNetworks = currentN.filter(new SnFilter());

        //System.out.println(filteredNetworks.count());
        //filteredNetworks.take(10).forEach(System.out::println);
        //filteredNetworks.collect().forEach(System.out::println);
        //System.out.println(filteredNetworks.count());

        sc.stop();
    }
}