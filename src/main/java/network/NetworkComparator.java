package network;

import scala.Serializable;
import scala.Tuple2;
import java.util.Comparator;

/**
 * Created by Rares on 05.04.2017.
 */
public class NetworkComparator implements Comparator<Tuple2<Integer, Network>>, Serializable {
    @Override
    public int compare(Tuple2<Integer, Network> o1, Tuple2<Integer, Network> o2) {
        if(o1._1() < o2._1()) return -1;
        if(o1._1() > o2._1()) return 1;
        return 0;
    }
}