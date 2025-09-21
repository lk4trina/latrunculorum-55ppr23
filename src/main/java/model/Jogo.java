package model;

import controller.Observado;

public class Jogo extends Observado<Jogo> {
    private final Tabuleiro tabuleiro;
    private Jogador jogadorAtual;
    private int pecasBrancasColocadas  = 0;
    private int pecasPretasColocadas  = 0;
    private boolean faseDeColocacao = true;

    public Jogo(int tam) {
        this.tabuleiro = new Tabuleiro(tam);
        this.jogadorAtual = Jogador.BRANCO;
    }

    public void lugarPeca(Posicao pos, PecaTipo type) {
        if (!tabuleiro.ehPosicaoValida(pos) || tabuleiro.getPeca(pos) != null) return;

        Peca peca = new Peca(jogadorAtual, type);
        tabuleiro.lugarPeca(pos, peca);

        if (jogadorAtual == Jogador.BRANCO) {
            pecasBrancasColocadas++;
        } else {
            pecasPretasColocadas++;
        }

        trocarJogadorSePrecisar();
        notifObservadores(this);
    }
    
    public void moverPeca(Posicao from, Posicao to) {
        Peca peca = tabuleiro.getPeca(from);
        if (peca == null) return;

        // movimento horizontal ou vertical
        if (from.lin != to.lin && from.col != to.col) return;

        // general pode pular sobre inimigo
        if (peca.getTipo() == PecaTipo.GENERAL && podePular(from, to)) {
            tabuleiro.moverPeca(from, to); // remove de 'from' e coloca em 'to'
        } else if (tabuleiro.getPeca(to) == null) { // movimento normal
            tabuleiro.moverPeca(from, to);
        }

        verificarCapturas(to, peca); // verifica capturas ao redor
        trocarJogadorSePrecisar();
        notifObservadores(this);
    }

    private boolean podePular(Posicao from, Posicao to) {
        // salto só de 2 casas em linha reta
        if (from.lin == to.lin) {
            int diff = to.col - from.col;
            Posicao meio = new Posicao(from.lin, from.col + diff / 2);
            return Math.abs(diff) == 2 && tabuleiro.getPeca(meio) != null && tabuleiro.getPeca(to) == null;
        } else if (from.col == to.col) {
            int diff = to.lin - from.lin;
            Posicao meio = new Posicao(from.lin + diff / 2, from.col);
            return Math.abs(diff) == 2 && tabuleiro.getPeca(meio) != null && tabuleiro.getPeca(to) == null;
        }
        return false;
    }

    private void verificarCapturas(Posicao pos, Peca pecaMovida) {
        Jogador inimigo = pecaMovida.getJogador().opposite();
        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        for (int[] d : dirs) {
            Posicao adj = new Posicao(pos.lin + d[0], pos.col + d[1]);
            Posicao depois = new Posicao(pos.lin + 2*d[0], pos.col + 2*d[1]);

            if (tabuleiro.ehPosicaoValida(adj) && tabuleiro.ehPosicaoValida(depois)) {
                Peca meio = tabuleiro.getPeca(adj);
                Peca extremidade = tabuleiro.getPeca(depois);

                if (meio != null && meio.getJogador() == inimigo) {
                    if (extremidade != null && extremidade.getJogador() == pecaMovida.getJogador()) {
                        tabuleiro.removerPeca(adj);
                    }
                }
            }
        }
    }


    private void trocarJogadorSePrecisar() {
        if (pecasBrancasColocadas >= 17 && pecasPretasColocadas >= 17) {
            faseDeColocacao = false;
        }
        jogadorAtual = jogadorAtual.opposite();
    }

    public boolean ehFaseDeColocacaoe() {
        return faseDeColocacao;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }
}
