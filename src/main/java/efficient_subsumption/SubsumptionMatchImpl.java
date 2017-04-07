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
 * along with out0 program.  If not, see <http://www.gnu.org/licenses/>.
 */
package efficient_subsumption;


/**
 *
 * Let Ca and Cb be comparator networks on n channels. If there exists a
 * permutation psuch that p(outputs(Ca)) is included in outputs(Cb) then we we
 * say that Cb is subsumedMap by Ca (or Ca findPermutation Cb).
 *
 * @author Cristian Frăsinaru
 */
public class SubsumptionMatchImpl implements Subsumption {

    public SubsumptionMatchImpl() {
    }

    /**
     * Find a permutation using matchings in a bipartite graph.
     *
     * @param out0
     * @param out1
     * @return
     */
    @Override
    public int[] findPermutation(OutputSet out0, OutputSet out1) {
        int nbWires = out0.nbWires;
        int[][] graph = new int[nbWires][nbWires];
        int[][] degrees = new int[2][nbWires];
        for (int u = 0; u < nbWires; u++) {
            next:
            for (int v = 0; v < nbWires; v++) {
                for (int k = 1; k < nbWires; k++) {
                    OutputCluster c0 = out0.cluster(k);
                    OutputCluster c1 = out1.cluster(k);
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
        return checkMatchings(out0, out1, graph, degrees);
    }

    private int[] checkMatchings(OutputSet out0, OutputSet out1, int[][] graph, int[][] degrees) {
        int[][] matching = findMatching(graph);
        if (matching == null) {
            return null;
        }
        if (checkPermutation(out0, out1, matching[0])) {
            return matching[0];
        }
        return checkMatchingsRec(out0, out1, graph, degrees, matching);
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

    private int[] checkMatchingsRec(OutputSet out0, OutputSet out1, int[][] graph, int[][] degrees, int[][] matching) {
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
        if (checkPermutation(out0, out1, mprime[0])) {
            return mprime[0];
        }

        int u = cycle[0];
        int v = cycle[1]; //e=uv

        //enumMatchingsRec(G-e,M) 
        graph[u][v] = 0;
        degrees[0][u]--;
        degrees[1][v]--;
        int[] perm = checkMatchingsRec(out0, out1, graph, degrees, mprime);
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
            perm = checkMatchingsRec(out0, out1, graph, degrees, matching);
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

    @Override
    public boolean checkPermutation(OutputSet out0, OutputSet out1, int[] perm) {

        return Subsumption.super.checkPermutation(out0, out1, perm);
    }

}
