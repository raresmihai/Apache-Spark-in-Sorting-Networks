package main;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.*;
import solver.CarthesianAggregatorSolver;
import solver.ListReducerSolver;
import solver.Solver;
import java.io.*;


/**
 * Stats for 6 wires and 12 Comparators
 * 2 Workers, 8 Cores, 8.2 GB RAM total
 *
 * Cartesian Solver: 21 minutes
 * (2,5) (2,4) (3,6) (5,6) (1,3) (3,4) (3,5) (1,2) (2,5) (4,6) (4,5) (2,3)  | 000111 011111 111111 001111 000011 000000 000001
 *
 * List Reducer: 15 seconds
 * (5,6) (3,4) (3,5) (1,2) (2,5) (4,6) (2,4) (1,4) (5,6) (4,5) (1,3) (2,3)  | 000111 011111 111111 001111 000011 000000 000001
 *  ------------------------------------------------------
 *
 *  7 Wires and 16 Comparators : 53 minutes <-- List Reducer
 *  (6,7) (4,5) (2,3) (4,6) (5,7) (1,7) (3,7) (1,2) (2,3) (5,6) (2,5) (3,6) (3,5) (1,4) (3,4) (2,3)  | 0001111 0111111 1111111 0011111 0000111 0000000 0000001 0000011
 *   1-3-7-19-51-138-305
 */
public class Main implements Serializable{
    final static Logger log = LogManager.getRootLogger();

    public static void main(String[] args){
        log.setLevel(Level.WARN);
        SparkConfSetter sparkConfSetter = new SparkConfSetter();
        JavaSparkContext sc = sparkConfSetter.getSparkContext();
        //Solver solver = new CarthesianAggregatorSolver();
        Solver solver = new ListReducerSolver();
        solver.solve(sc);
    }
}