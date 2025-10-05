package model;

import controller.Observado;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Jogo extends Observado<Jogo> {

    public enum Estado { EM_ANDAMENTO, VITORIA_BRANCO, VITORIA_MARROM, EMPATE }
    public enum Fase { COLOCACAO, MOVIMENTO }
    private static final int NUMERO_DE_SOLDADOS = 16;

    private Tabuleiro tabuleiro;
    private Jogador jogadorAtual;
    private Fase fase;
    private Estado estado;
    private boolean turnoExtra = false;
    private List<Peca> pecasBrancasCapturadas;
    private List<Peca> pecasMarromCapturadas;
    private int pecasBrancasColocadas;
    private int pecasMarromColocadas;

    private Tabuleiro ultimoTabuleiro;
    private Jogador ultimoJogador;
    private List<Peca> ultimasPecasBrancasCapturadas;
    private List<Peca> ultimasPecasMarromCapturadas;

    public Jogo(int tam) {
        this.tabuleiro = new Tabuleiro(tam);
        iniciarNovoJogo();
    }

    public void iniciarNovoJogo() {
        this.tabuleiro = new Tabuleiro(tabuleiro.getTamanho());
        this.jogadorAtual = Jogador.BRANCO;
        this.pecasBrancasColocadas = 0;
        this.pecasMarromColocadas = 0;
        this.fase = Fase.COLOCACAO;
        this.estado = Estado.EM_ANDAMENTO;
        this.pecasBrancasCapturadas = new ArrayList<>();
        this.pecasMarromCapturadas = new ArrayList<>();
        this.turnoExtra = false;
        limparUltimoEstado();
        notifObservadores(this);
    }

    private void salvarEstado() {
        this.ultimoTabuleiro = new Tabuleiro(this.tabuleiro.getTamanho());
        for (Map.Entry<Posicao, Peca> entry : this.tabuleiro.getGrid().entrySet()) {
            this.ultimoTabuleiro.lugarPeca(entry.getKey(), entry.getValue());
        }
        this.ultimoJogador = this.jogadorAtual;
        this.ultimasPecasBrancasCapturadas = new ArrayList<>(this.pecasBrancasCapturadas);
        this.ultimasPecasMarromCapturadas = new ArrayList<>(this.pecasMarromCapturadas);
    }

    public void desfazerJogada() {
        if (ultimoTabuleiro == null) return;
        this.tabuleiro = this.ultimoTabuleiro;
        this.jogadorAtual = this.ultimoJogador;
        this.pecasBrancasCapturadas = this.ultimasPecasBrancasCapturadas;
        this.pecasMarromCapturadas = this.ultimasPecasMarromCapturadas;
        this.estado = Estado.EM_ANDAMENTO;
        limparUltimoEstado();
        notifObservadores(this);
    }

    private void limparUltimoEstado() {
        this.ultimoTabuleiro = null;
        this.ultimoJogador = null;
        this.ultimasPecasBrancasCapturadas = null;
        this.ultimasPecasMarromCapturadas = null;
    }

    public void lugarPeca(Posicao pos) {
        if (fase != Fase.COLOCACAO) return;
        if (!tabuleiro.ehPosicaoValida(pos) || tabuleiro.getPeca(pos) != null) return;

        int totalPecas = (jogadorAtual == Jogador.BRANCO) ? pecasBrancasColocadas : pecasMarromColocadas;
        PecaTipo tipo = (totalPecas < NUMERO_DE_SOLDADOS) ? PecaTipo.SOLDADO : PecaTipo.GENERAL;
        if (totalPecas >= NUMERO_DE_SOLDADOS + 1) return;

        tabuleiro.lugarPeca(pos, new Peca(jogadorAtual, tipo));

        if (jogadorAtual == Jogador.BRANCO) pecasBrancasColocadas++;
        else pecasMarromColocadas++;

        if (pecasBrancasColocadas > NUMERO_DE_SOLDADOS && pecasMarromColocadas > NUMERO_DE_SOLDADOS) {
            fase = Fase.MOVIMENTO;
        }

        trocarJogador();
        notifObservadores(this);
    }

    public void moverPeca(Posicao from, Posicao to) {
        if (fase != Fase.MOVIMENTO || !getMovimentosValidos(from).contains(to)) return;

        salvarEstado();

        tabuleiro.moverPeca(from, to);

        int capturas = verificarCapturas(to);
        if (capturas > 0) turnoExtra = true;

        verificarCondicaoDeVitoria();
        if (estado == Estado.EM_ANDAMENTO) trocarJogador();
        notifObservadores(this);
    }

    private int verificarCapturas(Posicao pos) {
        Peca pecaMovida = tabuleiro.getPeca(pos);
        if (pecaMovida == null) return 0;

        int capturasRealizadas = 0;
        Jogador inimigo = pecaMovida.getJogador().opposite();
        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] d : dirs) {
            Posicao adj = new Posicao(pos.lin + d[0], pos.col + d[1]);
            Posicao depois = new Posicao(pos.lin + 2 * d[0], pos.col + 2 * d[1]);

            if (tabuleiro.ehPosicaoValida(adj) && tabuleiro.ehPosicaoValida(depois)) {
                Peca pecaMeio = tabuleiro.getPeca(adj);
                Peca pecaExtremidade = tabuleiro.getPeca(depois);

                if (pecaMeio != null && pecaMeio.getJogador() == inimigo &&
                        pecaExtremidade != null && pecaExtremidade.getJogador() == pecaMovida.getJogador()) {

                    if (pecaMeio.getJogador() == Jogador.BRANCO) pecasBrancasCapturadas.add(pecaMeio);
                    else pecasMarromCapturadas.add(pecaMeio);

                    tabuleiro.removerPeca(adj);
                    capturasRealizadas++;
                }
            }
        }
        return capturasRealizadas;
    }

    public List<Posicao> getMovimentosValidos(Posicao from) {
        List<Posicao> movimentos = new ArrayList<>();
        Peca peca = tabuleiro.getPeca(from);
        if (fase != Fase.MOVIMENTO || peca == null) return movimentos;

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] d : dirs) {
            Posicao to = new Posicao(from.lin + d[0], from.col + d[1]);
            if (tabuleiro.ehPosicaoValida(to) && tabuleiro.getPeca(to) == null) {
                movimentos.add(to);
            }
        }

        if (peca.getTipo() == PecaTipo.GENERAL) {
            for (int[] d : dirs) {
                Posicao sobre = new Posicao(from.lin + d[0], from.col + d[1]);
                Posicao to = new Posicao(from.lin + d[0] * 2, from.col + d[1] * 2);

                if (tabuleiro.ehPosicaoValida(to) && tabuleiro.getPeca(to) == null) {
                    Peca pecaSaltada = tabuleiro.getPeca(sobre);
                    if (pecaSaltada != null && pecaSaltada.getJogador() == peca.getJogador().opposite()) {
                        movimentos.add(to);
                    }
                }
            }
        }
        return movimentos;
    }

    private void verificarCondicaoDeVitoria() {
        if (!podeMover(jogadorAtual) || !podeMover(jogadorAtual.opposite())) {
            long pecasBrancas = tabuleiro.getGrid().values().stream().filter(p -> p.getJogador() == Jogador.BRANCO).count();
            long pecasMarrom = tabuleiro.getGrid().values().stream().filter(p -> p.getJogador() == Jogador.MARROM).count();

            if (pecasBrancas > pecasMarrom) estado = Estado.VITORIA_BRANCO;
            else if (pecasMarrom > pecasBrancas) estado = Estado.VITORIA_MARROM;
            else estado = Estado.EMPATE;
        }
    }

    private boolean podeMover(Jogador jogador) {
        return tabuleiro.getGrid().entrySet().stream()
                .filter(entry -> entry.getValue().getJogador() == jogador)
                .anyMatch(entry -> !getMovimentosValidos(entry.getKey()).isEmpty());
    }

    private void trocarJogador() {
        if (turnoExtra) {
            turnoExtra = false;
            return;
        }
        jogadorAtual = jogadorAtual.opposite();
    }

    public List<Peca> getPecasBrancasCapturadas() { return pecasBrancasCapturadas; }
    public List<Peca> getPecasMarromCapturadas() { return pecasMarromCapturadas; }
    public Fase getFase() { return fase; }
    public Estado getEstado() { return estado; }
    public Tabuleiro getTabuleiro() { return tabuleiro; }
    public Jogador getJogadorAtual() { return jogadorAtual; }
}