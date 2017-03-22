package network;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;


public class Network implements Serializable {
    private List<Comparator> comparators;
    private Set<Output> outputSet;

    public Network() {
        this.comparators = new ArrayList<>();
        this.outputSet = new HashSet<>();
    }

    public Network(List<Comparator> comparators) {
        this.comparators = comparators;
    }

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
}
