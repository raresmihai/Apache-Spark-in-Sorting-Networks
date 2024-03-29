package main;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Set the spark cluster properties, along with the app name
 */
class SparkConfSetter {
    private SparkConf conf;
    private static final String APP_NAME = "Sorting Networks with 9 Channels and 25 Comparators";
    private static final String MASTER = "local[4]";
    //private static final String MASTER = "spark://52.33.189.17:7077";
    private static final String JAR_PATH = "target/spark_sorting_networks-1.0.jar";
    private static final String SPARK_DRIVER_HOST = "spark.driver.host";
    private static final String SPARK_DEFAULT_PARALLELISM = "spark.default.parallelism";
    private static final String MASTER_IP = "79.112.7.252";
    private static final String NUMBER_OF_SPLITS = "8";

    SparkConfSetter() {
        conf = new SparkConf();
        conf.setAppName(APP_NAME);
        conf.setMaster(MASTER);
        conf.setJars(new String[]{JAR_PATH});
        //conf.set("spark.local.ip","127.0.0.1");
        //conf.set(SPARK_DRIVER_HOST,MASTER_IP);
        //conf.set(SPARK_DEFAULT_PARALLELISM,NUMBER_OF_SPLITS);
        //conf.set("spark.shuffle.service.enabled", "false");
        //conf.set("spark.dynamicAllocation.enabled", "false");
        //conf.set("spark.executor.memory","450m");
        //conf.set("spark.driver.memory","450m");
        //conf.set("spark.executor.cores","1");
        //conf.set("spark.deploy.defaultCores","1");
        //conf.set("spark.cores.max","1");
        //conf.set("spark.driver.port","2323");
    }

    JavaSparkContext getSparkContext() {
        return new JavaSparkContext(conf);
    }
}
