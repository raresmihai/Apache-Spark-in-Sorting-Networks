package network;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

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
