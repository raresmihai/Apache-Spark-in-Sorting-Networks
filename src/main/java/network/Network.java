package network;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * A network is composed of a list of comparators and a set of outputs.
 * 2 networks are equal if they have the same list of comparators(in the exact order)
 * and the same set of outputs.
 *
 * Example network (which in this case is also a sorting network) with 4 wires and 5 comparators
 * comparators: (1,2) (3,4) (1,3) (2,4) (2,3)
 * outputSet: 0000 0001 0011 0111 1111
 */
public class Network implements Serializable {
    private List<Comparator> comparators;
    private Set<Output> outputSet;

    public Network() {
        this.comparators = new ArrayList<>();
        this.outputSet = new HashSet<>();
    }

    public Network(Comparator comparator) {
        this.comparators = new ArrayList<>();
        addComparator(comparator);
        this.outputSet = OutputUtil.generate(comparator);
    }


    /**     *
     * @param network The network to extend
     * @param c The comparator to be added to the network
     * This constructor is used in the FlatMapGenerator.
     * Extends the network by first, creating a copy of the network and then adding the comparator c to the network.
     * After the new comparator c is added, the output set is updated.
     */
    public Network(Network network, Comparator c) {
        Logger log = LogManager.getRootLogger();
        log.setLevel(Level.WARN);

        this.comparators = new ArrayList<>();
        this.outputSet = new HashSet<>();
        List<Comparator> comparators = network.getComparators();
        for(Comparator comparator : comparators) {
            addComparator(comparator);
        }
        addComparator(c);

        if(network.getOutputSet().size() == 0) {
            this.outputSet = OutputUtil.generate(c);
        } else {
            for(Output output : network.getOutputSet()) {
                addOutput(output);
            }
            int i = c.getI() - 1;
            int j = c.getJ() - 1;
            Iterator<Output> iterator = outputSet.iterator();
            Set<Output> newOutputs = new HashSet<>();
            while(iterator.hasNext()) {
                Output output = iterator.next();
                if(output.values.get(i) && !output.values.get(j)) {
                    iterator.remove();
                    output.swap(i,j);
                    newOutputs.add(output);
                }
            }
            outputSet.addAll(newOutputs);
        }
    }

    private void addOutput(Output output) {
        outputSet.add(new Output(output));
    }

    public List<Comparator> getComparators() {
        return comparators;
    }

    public void setComparators(List<Comparator> comparators) {
        this.comparators = comparators;
    }

    public List<Comparator> addComparator(Comparator c) {
        comparators.add(new Comparator(c));
        return comparators;
    }

    public Set<Output> getOutputSet() {
        return outputSet;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Comparator c : comparators) {
            sb.append(String.valueOf(c)).append(" ");
        }
        sb.append(" | ");
        for(Output output : outputSet) {
            sb.append(String.valueOf(output)).append(" ");
        }
        return String.valueOf(sb);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Network)) {
            return false;
        }
        Network other = (Network) obj;
        List<Comparator> otherNetworkComparators = other.getComparators();
        if(comparators.size() != otherNetworkComparators.size()) {
            return false;
        }
        for(int i=0;i<comparators.size();i++) {
            if(!comparators.get(i).equals(otherNetworkComparators.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        Iterator i = comparators.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
        }
        return hashCode;
    }
}
