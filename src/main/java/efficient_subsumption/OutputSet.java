/*
 * Copyright (C) 2016 Faculty of Computer Science Iasi, Romania
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package efficient_subsumption;

import java.util.BitSet;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A representation of the output of a network.
 *
 * For every input array in {0,1}^n the output array is computed and stored in a
 * bitset.
 *
 * @author Cristian Frăsinaru
 */
public class OutputSet {

    private final OutputCluster[] clusters = null;
    private final BitSet values = null;
    private int[] intValues; //lazy
    private int size;
    final int nbWires = 9;

    private int minClusterSize, maxClusterSize;
    private int maxZeroCount, minZeroCount;
    private int maxOneCount, minOneCount;

    public OutputSet() {
//        this.values = new BitSet();
//        this.size = 0;
//        this.clusters = new OutputCluster[nbWires + 1];
//        for (int level = 0; level <= nbWires; level++) {
//            clusters[level] = new OutputCluster(this, level);
//        }
//        maxClusterSize = 0;
//        minClusterSize = Integer.MAX_VALUE;
//        maxZeroCount = 0;
//        minZeroCount = Integer.MAX_VALUE;
//        maxOneCount = 0;
//        minOneCount = Integer.MAX_VALUE;
    }

    /**
     *
     * @return
     */

    /**
     *
     * @return
     */
    public OutputCluster[] clusters() {
        return clusters;
    }

    /**
     *
     * @param level
     * @return
     */
    public OutputCluster cluster(int level) {
        return clusters[level];
    }

    /**
     * Adds a new possible output to the set.
     *
     * @param sequence
     */
    public void add(Sequence sequence) {
        int level = sequence.cardinality(); //how many one's
        OutputCluster cluster = clusters[level];
        int intValue = cluster.add(sequence);
        if (intValue >= 0) {
            values.set(intValue);
            size++;
        }
    }

/**
     * Whenever the output set is modified manually, invoke this method at the
     * end in order to recompute internal variables.
     */
    public void computeMinMaxValues() {
        maxClusterSize = 0;
        minClusterSize = Integer.MAX_VALUE;
        maxZeroCount = 0;
        minZeroCount = Integer.MAX_VALUE;
        maxOneCount = 0;
        minOneCount = Integer.MAX_VALUE;
        for (int k = 1; k < nbWires; k++) {
            OutputCluster c = clusters[k];
            int sz = c.size();
            if (sz > maxClusterSize) {
                maxClusterSize = sz;
            }
            if (sz < minClusterSize) {
                minClusterSize = sz;
            }
            int zc = c.zeroCount();
            if (zc > maxZeroCount) {
                maxZeroCount = zc;
            }
            if (zc < minZeroCount) {
                minZeroCount = zc;
            }
            int oc = c.zeroCount();
            if (oc > maxOneCount) {
                maxOneCount = oc;
            }
            if (oc < minOneCount) {
                minOneCount = oc;
            }
        }
    }
    
    /**
     * Returns all the output values, as a BitSet.
     *
     * A bit set at the position i in the returned object means that the binary
     * representation of the integer i is a possible output of the network.
     *
     * @return
     */
    public BitSet bitValues() {
        return values;
    }

    /**
     * Returns all the output values, as an array of integers.
     *
     * Lazy. Creates a new array. Don't use it if performance is an issue.
     *
     * Each binary representation of an integer in the returned array is a
     * possible output of the network.
     *
     * @return
     */
    public int[] intValues() {
        if (intValues != null) {
            return intValues;
        }
        intValues = new int[values.cardinality()];
        int j = 0;
        for (int i = values.nextSetBit(0); i >= 0; i = values.nextSetBit(i + 1)) {
            intValues[j++] = i;
        }
        return intValues;
    }

    /**
     * Checks if the binary representation of the given integer argument is a
     * possible output of the network.
     *
     * @param value
     * @return
     */
    public boolean contains(int value) {
        return values.get(value);
    }

    /**
     * Returns the number of elements in the output set.
     *
     * If the size of the output is (1 + nbWires) then the network is a sorting
     * network.
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     *
     * @param other
     * @return
     */
    public boolean includes(OutputSet other) {
        if (other.size > this.size) {
            return false;
        }
        BitSet bs = new BitSet(other.values.size());
        bs.or(other.values);
        bs.andNot(this.values);
        return bs.isEmpty();
        /*
        for (int i = 1; i < nbWires; i++) {
            if (!this.clusters[i].includes(other.clusters[i])) {
                return false;
            }
        }
        return true;
         */
    }

    public int minClusterSize() {
        return minClusterSize;
    }

    public int maxClusterSize() {
        return maxClusterSize;
    }

    public int minZeroCount() {
        return minZeroCount;
    }

    public int maxZeroCount() {
        return maxZeroCount;
    }

    public int minOneCount() {
        return minOneCount;
    }

    public int maxOneCount() {
        return maxOneCount;
    }

    /**
     *
     * @param other
     * @return
     */
    public int[] subsumes(OutputSet other) {
        int[][] graph = new int[nbWires][nbWires];
        int[][] degrees = new int[2][nbWires];
        for (int u = 0; u < nbWires; u++) {
            next:
            for (int v = 0; v < nbWires; v++) {
                for (int k = 1; k < nbWires; k++) {
                    OutputCluster c0 = this.clusters[k];
                    OutputCluster c1 = other.clusters[k];
                    //for each cluster (except the first and the last)
                    if ((c0.pos0[u] && !c1.pos0[v]) || (c0.pos1[u] && !c1.pos1[v])
                            || (c0.size() == c1.size()
                            && ((c1.pos0[v] && !c0.pos0[u]) || (c1.pos1[v] && !c0.pos1[u])))) {
                        continue next;
                    }
                }
                graph[u][v] = 1;
                degrees[0][u]++;
                degrees[1][v]++;

            }
            if (degrees[0][u] == 0) {
                return null;
            }
        }
        for (int w = 0; w < nbWires; w++) {
            if (degrees[1][w] == 0) {
                return null;
            }
        }
        return checkMatchings(other, graph, degrees);
    }

    private int[] checkMatchings(OutputSet other, int[][] graph, int[][] degrees) {
        int[][] matching = findMatching(graph);
        if (matching == null) {
            return null;
        }
        if (checkMatching(other, matching[0])) {
            return matching[0];
        }
        return checkMatchingsRec(other, graph, degrees, matching);
    }

    /*
     * Find a perfect matching in the bipartite graph. If no perfect matching
     * exists, it returns null.
     *
     * @param graph
     * @return
     */
    private int[][] findMatching(int[][] graph) {
        int n = graph.length;
        int matching[][] = new int[2][n];
        for (int i = 0; i < n; i++) {
            matching[0][i] = matching[1][i] = -1;
        }
        for (int u = 0; u < n; u++) {
            boolean seen[] = new boolean[n];
            if (!matchRec(graph, u, 0, seen, matching)) {
                return null;
            }
        }
        return matching;
    }

    // A DFS function that returns true if a matching for vertex u is possible
    private boolean matchRec(int[][] graph, int u, int next, boolean seen[], int[][] matching) {
        int n = graph.length;
        for (int v = next; v < n; v++) {
            if (graph[u][v] == 1 && matching[1][v] == -1) {
                matching[0][u] = v;
                matching[1][v] = u;
                return true;
            }
        }
        for (int v = next; v < n; v++) {
            if (graph[u][v] == 1 && !seen[v] && matching[1][v] >= 0) {
                seen[v] = true;
                if (matchRec(graph, matching[1][v], 0, seen, matching)) {
                    matching[0][u] = v;
                    matching[1][v] = u;
                    return true;
                }
            }
        }
        return false;
    }

    private int[] checkMatchingsRec(OutputSet other, int[][] graph, int[][] degrees, int[][] matching) {
        int n = graph.length;
        int[] cycle = findCycle(graph, degrees, matching);
        if (cycle == null) {
            return null;
        }
        //Find a perfect matching M' by exchanging edges along the cycle. Output M'.
        int mprime[][] = new int[2][n];
        for (int i = 0; i < n; i++) {
            mprime[0][i] = matching[0][i];
            mprime[1][i] = matching[1][i];
        }
        int i = 1;
        while (i > 0) {
            //
            int v = cycle[i];
            int w = i + 1 < cycle.length ? cycle[i + 1] : -1;
            if (w == -1) {
                w = cycle[0];
                i = 0;
            } else {
                i += 2;
            }
            mprime[1][v] = w;
            mprime[0][w] = v;
        }
        if (checkMatching(other, mprime[0])) {
            return mprime[0];
        }

        int u = cycle[0];
        int v = cycle[1]; //e=uv

        //enumMatchingsRec(G-e,M) 
        graph[u][v] = 0;
        degrees[0][u]--;
        degrees[1][v]--;
        int[] perm = checkMatchingsRec(other, graph, degrees, mprime);
        if (perm != null) {
            return perm;
        }
        graph[u][v] = 1;
        degrees[0][u]++;
        degrees[1][v]++;

        //enumMatchingsRec(G+e,M)
        //remove all edges adjacent to u or v (uv remains in the graph)
        for (int w = 0; w < n; w++) {
            if (w != v && graph[u][w] > 0) {
                if (graph[u][w] == 1) {
                    degrees[0][u]--;
                    degrees[1][w]--;
                }
                graph[u][w]++;
            }
            if (w != u && graph[w][v] > 0) {
                if (graph[w][v] == 1) {
                    degrees[0][w]--;
                    degrees[1][v]--;
                }
                graph[w][v]++;
            }
        }
        boolean emptyNode = false;
        for (int w = 0; w < n; w++) {
            if (degrees[0][w] == 0 || degrees[1][w] == 0) {
                emptyNode = true;
                break;
            }
        }
        if (!emptyNode) {
            perm = checkMatchingsRec(other, graph, degrees, matching);
            if (perm != null) {
                return perm;
            }
        }
        for (int w = 0; w < n; w++) {
            if (w != v && graph[u][w] > 1) {
                graph[u][w]--;
                if (graph[u][w] == 1) {
                    degrees[0][u]++;
                    degrees[1][w]++;
                }
            }
            if (w != u && graph[w][v] > 1) {
                graph[w][v]--;
                if (graph[w][v] == 1) {
                    degrees[0][w]++;
                    degrees[1][v]++;
                }
            }
        }
        return null;
    }

    private int[] findCycle(int[][] graph, int[][] degrees, int[][] matching) {
        int n = graph.length;
        for (int u = 0; u < n; u++) {
            if (degrees[0][u] < 2) {
                continue;
            }
            int[][] visited = new int[2][n]; //all zeros
            visited[0][u] = 1;
            int cycle[] = findCycleRec(graph, degrees, matching, u, 1, visited);
            if (cycle != null) {
                return cycle;
            }
        }
        return null;
    }

    /*
     *
     * @param matching
     * @param u the last node in the cycle
     * @param pos the current free position in the cycle
     * @param visited
     * @return
     */
    private int[] findCycleRec(int[][] graph, int[][] degrees, int[][] matching, int u, int pos, int[][] visited) {
        int n = graph.length;
        int set = (pos - 1) % 2; //the partition set, 0 or 1; u is in set; going from u in the other set
        //try to close the cycle (only if we're coming from set=1)
        if (set == 1) {
            for (int v = 0; v < n; v++) {
                if (graph[v][u] != 1 || matching[1][u] == v || visited[0][v] <= 0) {
                    continue;
                }
                //bingo: we found a cycle ending in the node v from the set 1-v
                int first = visited[0][v];
                int[] cycle = new int[pos - first + 1];
                for (int i = 0; i < n; i++) {
                    if (visited[0][i] >= first) {
                        cycle[visited[0][i] - first] = i;
                    }
                    if (visited[1][i] >= first) {
                        cycle[visited[1][i] - first] = i;
                    }
                }
                return cycle;
            }
        }
        //Cannot close the cycle yet, move on to a free node
        for (int v = 0; v < n; v++) {
            if (visited[1 - set][v] < 0 || degrees[1 - set][v] < 2
                    || (set == 0 && (graph[u][v] != 1 || matching[set][u] != v))
                    || (set == 1 && (graph[v][u] != 1 || matching[set][u] == v))) {
                continue;
            }
            //ok, found a free one
            visited[1 - set][v] = pos + 1;
            int[] cycle = findCycleRec(graph, degrees, matching, v, pos + 1, visited);
            if (cycle != null) {
                return cycle;
            }
            //no luck, keep going
            visited[1 - set][v] = -1;
        }
        return null;
    }

    /**
     *
     * @param other
     * @return
     */
    public boolean cannotSubsume(OutputSet other) {
        OutputSet out0 = this;
        OutputSet out1 = other;
        if (out0.size> out1.size) {
            return true;
        }
        if (out0.size< out1.size) {
            if (out0.maxClusterSize > out1.maxClusterSize
                    || out0.minClusterSize > out1.minClusterSize
                    || out0.maxZeroCount > out1.maxZeroCount
                    || out0.minZeroCount > out1.minZeroCount
                    || out0.maxOneCount > out1.maxOneCount
                    || out0.minOneCount > out1.minOneCount) {
                return true;
            }
        } else if (out0.maxClusterSize != out1.maxClusterSize
                || out0.minClusterSize != out1.minClusterSize
                || out0.maxZeroCount != out1.maxZeroCount
                || out0.minZeroCount != out1.minZeroCount
                || out0.maxOneCount != out1.maxOneCount
                || out0.minOneCount != out1.minOneCount) {
            return true;
        }        
        for (int k = 1; k < nbWires; k++) {
            if (this.clusters[k].cannotSubsume(other.clusters[k])) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMatching(OutputSet other, int[] perm) {
        for (int k = 2; k < nbWires - 1; k++) {
            if (!this.clusters[k].checkMatching(other.clusters[k], perm)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OutputSet)) {
            return false;
        }
        OutputSet other = (OutputSet) obj;
        return this.values.equals(other.values);
    }
    
      
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.values);
        return hash;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        for (OutputCluster cluster : clusters) {
            joiner.add(cluster.toString());
        }
        return joiner.toString();
    }

    /**
     *
     * @return
     */
    public String toStringZeros() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        for (OutputCluster cluster : clusters) {
            joiner.add(cluster.toStringZeros());
        }
        return joiner.toString();
    }

    /**
     *
     * @return
     */
    public String toStringOnes() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        for (OutputCluster cluster : clusters) {
            joiner.add(cluster.toStringOnes());
        }
        return joiner.toString();
    }

    /**
     *
     * @return
     */
    public String toStringIntValues() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        for (int value = values.nextSetBit(0); value >= 0; value = values.nextSetBit(value + 1)) {
            joiner.add(String.valueOf(value));
        }
        return joiner.toString();
    }

}
