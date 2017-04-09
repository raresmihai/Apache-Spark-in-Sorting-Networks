package spark_functions;

import network.Network;
import scala.Serializable;

import java.util.Comparator;

/**
 * Created by Rares on 07.04.2017.
 */
public class IntegerComparator implements Comparator<Integer>, Serializable {
    @Override
    public int compare(Integer o1, Integer o2) {
        if(o1 < o2) return -1;
        if(o1 > o2) return 1;
        return 0;
    }
}
