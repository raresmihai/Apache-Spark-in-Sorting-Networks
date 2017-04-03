package util;

import network.Network;
import network.NetworkProperties;
import network.Output;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Rares on 31.03.2017.
 */
public class Subsumption implements Serializable {
    /**
     *
     * @param network1 the network that is checked if it subsumes network2
     * @param network2 the network that is checked if it subsumed by network1
     * @return true if network1 subsumes network2, false otherwise
     */
    public boolean subsumes(Network network1, Network network2) {
        Set<Output> outputSet1 = network1.getOutputSet();
        Set<Output> outputSet2 = network2.getOutputSet();

        if(outputSet1.size() > outputSet2.size()) {
            return false;
        }

        //check Lema4
        int os1NumberOfOnesCluster[] = getNumberOfOnesPerCluster(outputSet1);
        int os2NumberOfOnesCluster[] = getNumberOfOnesPerCluster(outputSet2);
        for(int i = 0; i< NetworkProperties.NUMBER_OF_WIRES+1; i++) {
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
        PermutationGenerator permutationGenerator = new PermutationGenerator();
        List<List<Integer>> permutations = permutationGenerator.getAllPermutations();
        for(List<Integer> permutation : permutations) {
            Set<Output> permutedOutputSet = applyPermutation(outputSet1,permutation);
            if(subset(permutedOutputSet,outputSet2)) {
                return true;
            }
        }
        return false;
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
