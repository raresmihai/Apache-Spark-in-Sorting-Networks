package network;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Objects;

/**
 * Created by Rares on 07.03.2017.
 */
public class Output implements Serializable {

    BitSet values;
    int value;

    public Output() {
        values = new BitSet(4);
    }

    public Output(int value) {
        values = new BitSet(1);
        this.value = value;
        setValues(value);
    }

    public Output(Output output) {
        this.values = (BitSet) output.getValues().clone();
        this.value = output.getValue();
    }


    public BitSet getValues() {
        return values;
    }


    int getValue() {
        return value;
    }

    void flip(int index) {
        values.flip(index);
    }

    void swap(int i, int j) {
        flip(i);
        flip(j);
    }


    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.values);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Output)) {
            return false;
        }
        Output other = (Output) obj;
        return this.values.equals(other.values);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< NetworkProperties.NUMBER_OF_WIRES; i++) {
            sb.append(values.get(i) ? 1 : 0);
        }
        return sb.toString();
    }

    void setValues(int value) {
        int index = 0;
        while (value != 0) {
            if (value % 2 != 0) {
                values.set(index);
            }
            ++index;
            value = value >>> 1;
        }
    }
}
