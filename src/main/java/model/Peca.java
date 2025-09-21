package model;

public class Peca {
    private final Jogador jogador;
    private final PecaTipo tipo;

    public Peca(Jogador jogador, PecaTipo tipo) {
        this.jogador = jogador;
        this.tipo = tipo;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public PecaTipo getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return jogador.toString().charAt(0) + (tipo == PecaTipo.GENERAL ? "G" : "S");
    }
}
