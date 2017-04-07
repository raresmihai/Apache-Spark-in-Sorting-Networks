package network;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * A comparator is composed of two indices, i and j, indicating the two wires that the comparator connects
 * 2 comparators are equal if they have the same two indices, i and j
 *
 * Example comparator: (2,3)
 *  - connects the wires 2 and 3 and if the input value on wire 2 is bigger than the input value on wire 3, the values will be swapped.
 */
public class Comparator implements Serializable{
    private int i,j;

    Comparator(Comparator c) {
        this.i = c.getI();
        this.j = c.getJ();
    }

    public Comparator(int i, int j) {
        this.i = i;
        this.j = j;
    }

    int getI() {
        return i;
    }

    void setI(int i) {
        this.i = i;
    }

    int getJ() {
        return j;
    }

    void setJ(int j) {
        this.j = j;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(i).append(",").append(j).append(")");
        return String.valueOf(sb);
    }

    @Override
    public int hashCode() {
        return 31 * i + j;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Comparator)) {
            return false;
        }
        Comparator other = (Comparator) obj;
        return i == other.getI() && j == other.getJ();
    }
}
