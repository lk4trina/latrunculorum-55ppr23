package model;

import java.util.HashMap;
import java.util.Map;

public class Tabuleiro {
    private final int tam;
    private final Map<Posicao, Peca> grid;

    public Tabuleiro(int tam) {
        this.tam = tam;
        this.grid = new HashMap<>();
    }

    public boolean ehPosicaoValida(Posicao pos) {
        return pos.lin >= 0 && pos.col >= 0 && pos.lin < tam && pos.col < tam;
    }

    public Peca getPeca(Posicao pos) {
        return grid.get(pos);
    }

    public void lugarPeca(Posicao pos, Peca peca) {
        if (ehPosicaoValida(pos)) {
            grid.put(pos, peca);
        }
    }

    public void moverPeca(Posicao from, Posicao to) {
        if (grid.containsKey(from)) {
            Peca p = grid.remove(from);
            grid.put(to, p);
        }
    }

    public void removerPeca(Posicao pos) {
        grid.remove(pos);
    }

    public int getTamanho() {
        return tam;
    }

    public Map<Posicao, Peca> getGrid() {
        return grid;
    }

    public void exibirTabuleiro() {
        for (int lin = 0; lin < tam; lin++) {
            for (int col = 0; col < tam; col++) {
                Peca peca = grid.get(new Posicao(lin, col));
                System.out.print((peca == null ? "__" : peca.toString()) + " ");
            }
            System.out.println();
        }
    }
}
