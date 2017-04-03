package solver;


import network.NetworkProperties;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by Rares on 03.04.2017.
 */
public interface Solver {
    int NUMBER_OF_COMPARATORS = NetworkProperties.NUMBER_OF_COMPARATORS;
    int NUMBER_OF_SEQ_GENERATE_STEPS = NetworkProperties.NUMBER_OF_SEQ_GENERATE_STEPS;

    void solve(JavaSparkContext sc);
}
