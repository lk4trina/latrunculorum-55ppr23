package view;

import controller.ControllerJogo;
import model.*;
import controller.Observador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

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

                Rectangle fundo = new Rectangle(60, 60);
                fundo.setFill(Color.BISQUE);
                fundo.setStroke(Color.BLACK);
                fundo.setStrokeWidth(1);

                celula.getChildren().add(fundo);
                celula.getStyleClass().add("cell");

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

        String nomeJogador = (jogo.getJogadorAtual() == Jogador.BRANCO) ? "Branco" : "Marrom";
        labelJogadorAtual.setText("Jogador Atual: " + nomeJogador);

        labelFase.setText(jogo.ehFaseDeColocacao() ? "Fase: Colocação" : "Fase: Movimento");
        labelMensagem.setText(
                jogo.ehFaseDeColocacao()
                        ? "Coloque sua peça no tabuleiro."
                        : "Selecione e mova uma peça."
        );

        for (int lin = 0; lin < tamanho; lin++) {
            for (int col = 0; col < tamanho; col++) {
                Posicao pos = new Posicao(lin, col);
                Peca peca = jogo.getTabuleiro().getPeca(pos);

                StackPane celula = (StackPane) getNodeByRowColumnIndex(lin, col, tabuleiroGrid);
                if (celula == null) continue;

                javafx.scene.Node fundo = celula.getChildren().get(0);
                celula.getChildren().clear();
                celula.getChildren().add(fundo);

                if (peca != null) {
                    boolean ehBranco = peca.getJogador() == Jogador.BRANCO;

                    if (peca.getTipo() == PecaTipo.GENERAL) {
                        Circle circulo = new Circle(28);
                        circulo.setFill(ehBranco ? Color.BEIGE : Color.SIENNA);
                        circulo.setStroke(ehBranco ? Color.DARKGOLDENROD : Color.DARKRED);
                        circulo.setStrokeWidth(2);
                        circulo.getStyleClass().add("piece");

                        Polygon triangulo = new Polygon(
                                0.0, -18.0,
                                -14.0, 12.0,
                                14.0, 12.0
                        );
                        triangulo.setFill(ehBranco ? Color.GOLD : Color.BLACK);
                        triangulo.setStroke(ehBranco ? Color.BLACK : Color.WHITESMOKE);
                        triangulo.setStrokeWidth(1.5);
                        triangulo.getStyleClass().add("piece");

                        celula.getChildren().addAll(circulo, triangulo);

                    } else {
                        Circle circulo = new Circle(25);
                        circulo.setFill(ehBranco ? Color.BEIGE : Color.SIENNA);
                        circulo.setStroke(ehBranco ? Color.DARKGOLDENROD : Color.DARKRED);
                        circulo.setStrokeWidth(2);
                        circulo.getStyleClass().add("piece");
                        celula.getChildren().add(circulo);
                    }
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