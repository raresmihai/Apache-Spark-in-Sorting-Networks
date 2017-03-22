package main;
import network.Network;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import java.io.*;
import java.util.*;

public class Main implements Serializable{
    final static Logger log = LogManager.getRootLogger();
    private final static int NUMBER_OF_SEQ_GENERATE_STEPS = 3;
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
        JavaRDD<Network> currentN = sc.parallelize(data,NUMBER_OF_SPLITS);
        for(int k=NUMBER_OF_SEQ_GENERATE_STEPS;k<NetworkProperties.NUMBER_OF_COMPARATORS;k++) {
            currentN = currentN.flatMap(new MyFlatMap());
        }

        //Get only the sorting networks using a filter
        JavaRDD<Network> filteredNetworks = currentN.filter(new SnFilter());

        System.out.println(filteredNetworks.count());
        filteredNetworks.take(10).forEach(System.out::println);
        //filteredNetworks.collect().forEach(System.out::println);
        //System.out.println(filteredNetworks.count());

        sc.stop();
    }
}