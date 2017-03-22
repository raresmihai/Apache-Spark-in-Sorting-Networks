package main;

import network.Network;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Rares on 22.03.2017.
 */
public class NetworkComparator implements Comparator<Tuple2<Network, Integer>>,Serializable {
    @Override
    public int compare(Tuple2<Network, Integer> o1, Tuple2<Network, Integer> o2) {
        if(o1._2() < o2._2()) return -1;
        if(o1._2() > o2._2()) return 1;
        return 0;
    }
}
