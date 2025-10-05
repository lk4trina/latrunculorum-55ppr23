package controller;

import model.*;

public class ControllerJogo {
    private final Jogo jogo;

    public ControllerJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public void reiniciarJogo() {
        jogo.iniciarNovoJogo();
    }

    public void desfazerJogada() {
        jogo.desfazerJogada();
    }

    public void lugarPeca(int lin, int col) {
        jogo.lugarPeca(new Posicao(lin, col));
    }

    public void moverPeca(Posicao from, Posicao to) {
        jogo.moverPeca(from, to);
    }

    public Jogo.Fase getFase() {
        return jogo.getFase();
    }

    public Jogador getJogadorAtual() {
        return jogo.getJogadorAtual();
    }

    public Jogo getJogo() {
        return jogo;
    }
}