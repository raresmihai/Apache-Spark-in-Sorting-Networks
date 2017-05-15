package main;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.*;
import solver.CartesianAggregatorSolver;
import solver.ListReducerSolver;
import solver.MinimumOutputsSolver;
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
 *  (6,7) (5,7) (3,4) (1,2) (2,4) (1,3) (4,7) (2,5) (4,5) (3,6) (5,6) (4,5) (2,3) (1,3) (3,4) (1,2)  | 0001111 0111111 1111111 0011111 0000111 0000000 0000001 0000011
 *   optimal size: 1-3-7-19-51-141-325-564-678-510-280-106-33-11-6-1
 *   this        : 1-3-7-19-49-138-302-502-603-439-246- 99-33-11-6-1
 *
 *   8 Wires and 19 Comparators
 *   (1,2) (3,4) (5,6) (7,8) (1,3) (2,4) (2,3) (5,7) (6,8) (6,7) (1,5) (2,6) (3,7) (4,8) (4,6) (3,5) (4,5) (6,7) (2,3)  | 00011111 01111111 11111111 00111111 00001111 00000001 00000000 00000011 00000111
 *
 *   9 Wires and 25 Comparators
 *   27 hours on 4 Cores with MinimumOutputSolver
 *   (8,9) (6,7) (4,5) (2,3) (6,8) (7,9) (7,8) (3,5) (2,4) (3,4) (1,6) (5,9) (5,6) (4,8) (3,7) (5,7) (1,2) (6,8) (2,5) (6,7) (4,5) (5,6) (8,9) (2,3) (3,4)  | 000111111 011111111 111111111 001111111 000011111 000000011 000000001 000000000 000000111 000001111
 *
 */
public class Main implements Serializable{
    final static Logger log = LogManager.getRootLogger();


    public static void main(String[] args){

        log.setLevel(Level.WARN);
        SparkConfSetter sparkConfSetter = new SparkConfSetter();
        JavaSparkContext sc = sparkConfSetter.getSparkContext();
        //Solver solver = new CartesianAggregatorSolver();
        Solver solver = new ListReducerSolver();
        //Solver solver = new MinimumOutputsSolver();
        solver.solve(sc);
    }

}