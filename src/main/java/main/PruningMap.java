package main;

import network.Network;
import network.Output;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Rares on 22.03.2017.
 *
 */
public class PruningMap implements PairFunction<Tuple2<Network,Network>, Network,Boolean>,Serializable {
    private List<List<Integer>> permutations = new ArrayList<>();

    /*
    <Network1,Network2> remains iff Network2 <= Network1 is false (Network1 is not subsumed by Network2)
    Network1 is the key and will be kept iff Network2 <= Network1 is false (Network1 is not subsumed by Network2)
    The boolean flag in the tuple says if Network1 will be kept or no
     */
    @Override
    public Tuple2<Network, Boolean> call(Tuple2<Network, Network> networkPair) throws Exception {
        Network network1 = networkPair._1();
        Network network2 = networkPair._2();
        if(network1.equals(network2)) {
            return new Tuple2<>(network1,true);
        }
        return new Tuple2<>(network1,!subsumes(network2,network1));
    }

    private boolean subsumes(Network network1,Network network2) {
        Set<Output> outputSet1 = network1.getOutputSet();
        Set<Output> outputSet2 = network2.getOutputSet();

        if(outputSet1.size() > outputSet2.size()) {
            return false;
        }

        //check Lema4
        int os1NumberOfOnesCluster[] = getNumberOfOnesPerCluster(outputSet1);
        int os2NumberOfOnesCluster[] = getNumberOfOnesPerCluster(outputSet2);
        for(int i=0;i<NetworkProperties.NUMBER_OF_WIRES+1;i++) {
            if(os1NumberOfOnesCluster[i] > os2NumberOfOnesCluster[i]) {
                return false;
            }
        }

        return permutationExists(outputSet1,outputSet2);
    }

    private int[] getNumberOfOnesPerCluster(Set<Output> outputSet) {
        int numberOfOnesCluster[] = new int[NetworkProperties.NUMBER_OF_WIRES+1];
        for(Output output : outputSet) {
            int numberOfOnes = getNumberOfOnesInOutput(output);
            numberOfOnesCluster[numberOfOnes]++;
        }
        return numberOfOnesCluster;
    }

    private int getNumberOfOnesInOutput(Output output) {
        return output.getValues().cardinality();
    }

    private boolean permutationExists(Set<Output> outputSet1, Set<Output> outputSet2) {
        List<Integer> initialPermutation = new ArrayList<>();
        for(int i=0;i<NetworkProperties.NUMBER_OF_WIRES;i++) {
            initialPermutation.add(i);
        }
        computeAllPermutations(initialPermutation,0);
        for(List<Integer> permutation : permutations) {
            Set<Output> permutedOutputSet = applyPermutation(outputSet1,permutation);
            if(subset(permutedOutputSet,outputSet2)) {
                return true;
            }
        }
        return false;
    }

    private void computeAllPermutations(java.util.List<Integer> arr, int k){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            computeAllPermutations(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            permutations.add(new ArrayList<>(arr));
        }
    }

    private Set<Output> applyPermutation(Set<Output> outputSet, List<Integer> permutation) {
        Set<Output> permutedOutput = new HashSet<>();
        for(Output output : outputSet) {
            Output copy = new Output(output);
            for(int i=0;i<permutation.size();i++) {
                Integer newIndex = permutation.get(i);
                boolean newValue = output.getValues().get(newIndex);
                copy.getValues().set(i,newValue);
            }
            permutedOutput.add(copy);
        }
        return permutedOutput;
    }

    private boolean subset(Set<Output> permutedOutputSet, Set<Output> outputSet2) {
        for(Output output : permutedOutputSet) {
            if(!outputSet2.contains(output)) {
                return false;
            }
        }
        return true;
    }
}
