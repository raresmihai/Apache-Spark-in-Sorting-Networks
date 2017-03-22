package network;

import java.io.Serializable;

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
}
