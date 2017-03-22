package main;

import org.apache.spark.SparkConf;

/**
 * Created by Rares on 22.03.2017.
 */
class SparkConfSetter {
    private SparkConf conf;

    SparkConfSetter() {
        conf = new SparkConf();
        conf.setAppName("Sorting Networks with 5 Channels and 9 Comparators");
        conf.setMaster("spark://192.168.0.102:7077");
        conf.setJars(new String[]{"target/spark_sorting_networks-1.0.jar"});
        conf.set("spark.driver.host","192.168.0.102");
        //conf.set("spark.shuffle.service.enabled", "false");
        //conf.set("spark.dynamicAllocation.enabled", "false");
        //conf.set("spark.executor.memory","1g");
        //conf.set("spark.driver.memory","1g");
        //conf.set("spark.default.parallelism","8");
        //conf.set("spark.executor.cores","1");
        //conf.set("spark.deploy.defaultCores","1");
        //conf.set("spark.cores.max","1");
    }

    SparkConf getSparkConf() {
        return conf;
    }
}
