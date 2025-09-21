package model;

public class Posicao {
    public final int lin;
    public final int col;

    public Posicao(int lin, int col) {
        this.lin = lin;
        this.col = col;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Posicao)) return false;
        Posicao p = (Posicao) o;
        return this.lin == p.lin && this.col == p.col;
    }

    public int hashCode() {
        return 31 * lin + col;
    }

    @Override
    public String toString() {
        return "(" + lin + "," + col + ")";
    }
}
