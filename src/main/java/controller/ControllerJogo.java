package controller;

import model.*;

public class ControllerJogo {
    private final Jogo jogo;

    public ControllerJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public void lugarPeca(int lin, int col, PecaTipo tipo) {
        jogo.lugarPeca(new Posicao(lin, col), tipo);
    }

    public void printTabuleiro() {
        jogo.getTabuleiro().exibirTabuleiro();
    }

    public Jogador getJogadorAtual() {
        return jogo.getJogadorAtual();
    }

    public boolean ehFaseDeColocacao() {
        return jogo.ehFaseDeColocacao();
    }

    public void iniciarJogo() {
        jogo.notifObservadores(jogo);
    }

    public int getTamanhoTabuleiro() {
        return jogo.getTabuleiro().getTamanho();
    }
}

