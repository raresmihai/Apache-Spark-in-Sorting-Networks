package solver;


import network.NetworkProperties;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Finds a sorting network with the optimal size of comparators.
 * Generates all the networks starting from the empty network and then adding NUMBER_OF_COMPARATORS comparators.
 * Because the number of networks becomes very big with each comparator added, a pruning step will be done using different solvers.
 */
public interface Solver {
    /**
     * @param NUMBER_OF_COMPARATORS the optimal size of comparators on NUMBER_OF_WIRES wires
     * @see network.NetworkProperties
     */
    int NUMBER_OF_COMPARATORS = NetworkProperties.NUMBER_OF_COMPARATORS;
    int NUMBER_OF_SEQ_GENERATE_STEPS = NetworkProperties.NUMBER_OF_SEQ_GENERATE_STEPS;

    void solve(JavaSparkContext sc);
}
