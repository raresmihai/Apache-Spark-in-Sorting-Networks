package util;

import network.NetworkProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rares on 23.03.2017.
 */
public class PermutationGenerator implements Serializable{
    private List<List<Integer>> permutations = null;

    public  List<List<Integer>> getAllPermutations() {
        if(permutations != null) {
            return permutations;
        } else {
            permutations = new ArrayList<>();
            List<Integer> initialPermutation = new ArrayList<>();
            for(int i = 0; i< NetworkProperties.NUMBER_OF_WIRES; i++) {
                initialPermutation.add(i);
            }
            computeAllPermutations(initialPermutation,0);
            return permutations;
        }
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
}
