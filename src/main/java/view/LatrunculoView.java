package view;

import controller.ControllerJogo;
import model.*;
import controller.Observador;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LatrunculoView implements Observador<Jogo> {

    @FXML private GridPane tabuleiroGrid;
    @FXML private Label labelJogadorAtual;
    @FXML private Label labelFase;
    @FXML private Label labelMensagem;

    private final ControllerJogo controller;
    private Posicao posicaoSelecionada = null;

    public LatrunculoView(ControllerJogo controller) {
        this.controller = controller;
    }

    @FXML
    private void handleNovoJogo() {
        controller.iniciarJogo();
    }

    @FXML
    private void handleDesfazer() { }

    @FXML
    private void handleAjuda() { }

    private void handleCellClick(int lin, int col) {
        if (controller.ehFaseDeColocacao()) {
            controller.lugarPeca(lin, col, PecaTipo.SOLDADO);
        } else {
            System.out.println("Fase de Movimento não implementada ainda.");
        }
    }

    private void criarTabuleiroInicial(int tamanho) {
        tabuleiroGrid.getChildren().clear();

        for (int lin = 0; lin < tamanho; lin++) {
            for (int col = 0; col < tamanho; col++) {
                StackPane celula = new StackPane();
                celula.setMinSize(60, 60);

                // CORES DO TABULEIRO: Estilo Mármore/Pedra Romana
                String cor = ((lin + col) % 2 == 0) ? "#C19A6B" : "#8B4513"; // Tan e Marrom Escuro
                celula.setStyle("-fx-background-color: " + cor + ";");

                final int finalLin = lin;
                final int finalCol = col;
                celula.setOnMouseClicked(event -> handleCellClick(finalLin, finalCol));

                tabuleiroGrid.add(celula, col, lin);
            }
        }
    }

    @Override
    public void atualizar(Jogo jogo) {
        int tamanho = jogo.getTabuleiro().getTamanho();

        if (tabuleiroGrid.getChildren().isEmpty()) {
            criarTabuleiroInicial(tamanho);
        }

        labelJogadorAtual.setText(jogo.getJogadorAtual().toString());
        labelFase.setText(jogo.ehFaseDeColocacao() ? "Colocação" : "Movimento");
        labelMensagem.setText(jogo.ehFaseDeColocacao() ?
                "Fase de Colocação. Jogador " + jogo.getJogadorAtual() + ", clique para colocar." :
                "Fase de Movimento. Selecione uma peça para mover."
        );

        for (int lin = 0; lin < tamanho; lin++) {
            for (int col = 0; col < tamanho; col++) {
                Posicao pos = new Posicao(lin, col);
                Peca peca = jogo.getTabuleiro().getPeca(pos);

                StackPane celula = (StackPane) getNodeByRowColumnIndex(lin, col, tabuleiroGrid);
                if (celula == null) continue;

                celula.getChildren().clear();

                if (peca != null) {
                    // Raio maior para o General
                    double raio = peca.getTipo() == PecaTipo.GENERAL ? 28 : 22;
                    Circle circulo = new Circle(raio);

                    // Lógica de cores baseada na sua solicitação: Branco/Marrom e Ouro/Prata
                    if (peca.getJogador() == Jogador.BRANCO) {
                        // Jogador BRANCO: Soldados Brancos, General Dourado
                        if (peca.getTipo() == PecaTipo.SOLDADO) {
                            circulo.setFill(Color.web("#F0F0F0")); // Branco Sujo (Off-White)
                            circulo.setStroke(Color.DIMGRAY);
                        } else { // General
                            circulo.setFill(Color.web("#FFD700")); // Dourado
                            circulo.setStroke(Color.web("#DAA520")); // Marrom Dourado
                        }
                    } else {
                        // Jogador PRETO (Oposição): Soldados Marrons, General Prateado
                        if (peca.getTipo() == PecaTipo.SOLDADO) {
                            circulo.setFill(Color.web("#A0522D")); // Marrom (Sienna)
                            circulo.setStroke(Color.web("#5C3317"));
                        } else { // General
                            circulo.setFill(Color.web("#C0C0C0")); // Prata
                            circulo.setStroke(Color.web("#696969")); // Cinza Escuro
                        }
                    }

                    circulo.setStrokeWidth(2);

                    celula.getChildren().add(circulo);
                }
            }
        }
    }

    private javafx.scene.Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null &&
                    GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }
}