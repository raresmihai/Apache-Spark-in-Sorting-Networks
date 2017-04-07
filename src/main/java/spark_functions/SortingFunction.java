package spark_functions;

import org.apache.spark.api.java.function.Function;

/**
 * Created by Rares on 05.04.2017.
 */
public class SortingFunction implements Function<Integer,Object> {
    @Override
    public Integer call(Integer integer) throws Exception {
        return integer;
    }
}
