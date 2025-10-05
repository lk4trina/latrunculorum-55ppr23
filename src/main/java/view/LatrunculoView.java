package view;

import controller.ControllerJogo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.*;
import controller.Observador;

import java.util.List;
import java.util.Optional;

public class LatrunculoView implements Observador<Jogo> {

    @FXML private GridPane tabuleiroGrid;
    @FXML private Label labelJogadorAtual;
    @FXML private Label labelFase;
    @FXML private Label labelMensagem;
    @FXML private FlowPane pecasCapturadasBranco;
    @FXML private FlowPane pecasCapturadasMarrom;

    private final ControllerJogo controller;
    private Posicao posicaoSelecionada = null;

    public LatrunculoView(ControllerJogo controller) {
        this.controller = controller;
    }

    @FXML
    public void initialize() {
        tabuleiroGrid.getStyleClass().add("grid-pane");
        criarTabuleiroVisual(controller.getJogo().getTabuleiro().getTamanho());
    }

    @FXML
    private void handleNovoJogo() {
        controller.reiniciarJogo();
        posicaoSelecionada = null;
    }

    @FXML
    private void handleDesfazer() {
        controller.desfazerJogada();
        posicaoSelecionada = null;
    }

    @FXML
    private void handleAjuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajuda/Regras do Ludus Latrunculorum");
        alert.setHeaderText("Regras do Jogo");

        String regras =
                "1. FASE DE COLOCACAO:\n" +
                        "   - Cada jogador, alternadamente, coloca uma peca no tabuleiro em qualquer casa vazia.\n" +
                        "   - Apos colocar os 16 soldados, o jogador posiciona seu General.\n\n" +
                        "2. FASE DE MOVIMENTO:\n" +
                        "   - As pecas movem-se uma casa por vez, na horizontal ou vertical, para um espaco vazio.\n" +
                        "   - O General pode, alem de se mover uma casa, saltar sobre uma peca adversaria para uma casa vazia logo atras.\n\n" +
                        "3. CAPTURA:\n" +
                        "   - Uma peca e capturada por 'custodia' quando e cercada em lados opostos por duas pecas inimigas (na horizontal ou vertical).\n" +
                        "   - Um jogador que captura uma peca tem direito a um turno extra.\n" +
                        "   - Uma peca que se move para um espaco entre duas pecas inimigas nao e capturada.\n\n" +
                        "4. VITORIA:\n" +
                        "   - Vence quem deixar o adversario sem movimentos possiveis ou capturar quase todas as suas pecas.\n" +
                        "   - Se houver empate (impasse), vence quem tiver mais pecas no tabuleiro.";

        TextArea textArea = new TextArea(regras);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        alert.getDialogPane().setContent(textArea);
        alert.setResizable(true);
        alert.showAndWait();
    }

    private void handleCellClick(int lin, int col) {
        Posicao clicada = new Posicao(lin, col);

        if (controller.getFase() == Jogo.Fase.COLOCACAO) {
            controller.lugarPeca(lin, col);
        } else {
            Peca pecaNoLocal = controller.getJogo().getTabuleiro().getPeca(clicada);

            if (posicaoSelecionada != null) {
                if (posicaoSelecionada.equals(clicada)) {
                    posicaoSelecionada = null;
                } else {
                    controller.moverPeca(posicaoSelecionada, clicada);
                    posicaoSelecionada = null;
                }
            } else if (pecaNoLocal != null && pecaNoLocal.getJogador() == controller.getJogadorAtual()) {
                posicaoSelecionada = clicada;
            }
        }
        atualizarVisualizacao(controller.getJogo());
    }

    @Override
    public void atualizar(Jogo jogo) {
        Platform.runLater(() -> atualizarVisualizacao(jogo));
    }

    private void atualizarVisualizacao(Jogo jogo) {
        atualizarLabels(jogo);
        limparEstilosDasCelulas();

        if (posicaoSelecionada != null) {
            mostrarSugestoesDeMovimento(posicaoSelecionada);
        }

        atualizarPecasNoTabuleiro(jogo);
        atualizarPecasCapturadas(jogo);

        if (jogo.getEstado() != Jogo.Estado.EM_ANDAMENTO) {
            exibirTelaFimDeJogo(jogo.getEstado());
            posicaoSelecionada = null;
        }
    }

    private void criarTabuleiroVisual(int tamanho) {
        tabuleiroGrid.getChildren().clear();
        for (int lin = 0; lin < tamanho; lin++) {
            for (int col = 0; col < tamanho; col++) {
                StackPane celula = new StackPane();
                celula.getStyleClass().add("cell");
                final int finalLin = lin;
                final int finalCol = col;
                celula.setOnMouseClicked(event -> handleCellClick(finalLin, finalCol));
                tabuleiroGrid.add(celula, col, lin);
            }
        }
    }

    private void atualizarPecasNoTabuleiro(Jogo jogo) {
        for (Node node : tabuleiroGrid.getChildren()) {
            if (node instanceof StackPane) {
                ((StackPane) node).getChildren().clear();
            }
        }
        for (var entry : jogo.getTabuleiro().getGrid().entrySet()) {
            Posicao pos = entry.getKey();
            StackPane celula = (StackPane) getNodeByRowColumnIndex(pos.lin, pos.col);
            if (celula != null) {
                celula.getChildren().add(criarVisualPeca(entry.getValue(), false));
            }
        }
    }

    private void atualizarPecasCapturadas(Jogo jogo) {
        pecasCapturadasBranco.getChildren().clear();
        for (Peca peca : jogo.getPecasBrancasCapturadas()) {
            pecasCapturadasBranco.getChildren().add(criarVisualPeca(peca, true));
        }

        pecasCapturadasMarrom.getChildren().clear();
        for (Peca peca : jogo.getPecasMarromCapturadas()) {
            pecasCapturadasMarrom.getChildren().add(criarVisualPeca(peca, true));
        }
    }

    private Node criarVisualPeca(Peca peca, boolean capturada) {
        boolean ehBranco = peca.getJogador() == Jogador.BRANCO;
        StackPane pecaVisual = new StackPane();
        pecaVisual.setAlignment(Pos.CENTER);

        double raioCirculo = capturada ? 12 : (peca.getTipo() == PecaTipo.GENERAL ? 22 : 25);

        if (peca.getTipo() == PecaTipo.GENERAL) {
            Circle circulo = new Circle(raioCirculo);
            circulo.getStyleClass().addAll("piece", "general", "general-circle", ehBranco ? "white" : "brown");

            Polygon triangulo = new Polygon(0.0, -14.0, -11.0, 9.0, 11.0, 9.0);
            triangulo.getStyleClass().addAll("piece", "general-triangle", ehBranco ? "white" : "brown");

            pecaVisual.getChildren().addAll(circulo, triangulo);
        } else {
            Circle circulo = new Circle(raioCirculo);
            circulo.getStyleClass().addAll("piece", ehBranco ? "white" : "brown");
            pecaVisual.getChildren().add(circulo);
        }

        if (capturada) pecaVisual.getStyleClass().add("captured-piece");
        return pecaVisual;
    }

    private void mostrarSugestoesDeMovimento(Posicao pos) {
        List<Posicao> movimentos = controller.getJogo().getMovimentosValidos(pos);
        for (Posicao p : movimentos) {
            Node cell = getNodeByRowColumnIndex(p.lin, p.col);
            if (cell != null) cell.getStyleClass().add("suggestion-cell");
        }
    }

    private void limparEstilosDasCelulas() {
        for (Node node : tabuleiroGrid.getChildren()) {
            node.getStyleClass().remove("suggestion-cell");
        }
    }

    private void exibirTelaFimDeJogo(Jogo.Estado estado) {
        String mensagem;
        switch(estado) {
            case VITORIA_BRANCO: mensagem = "O jogador Branco venceu!"; break;
            case VITORIA_MARROM: mensagem = "O jogador Marrom venceu!"; break;
            default: mensagem = "O jogo terminou em empate!"; break;
        }

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Fim de Jogo");
        alert.setHeaderText(mensagem);

        ButtonType novoJogoBtn = new ButtonType("Novo Jogo");
        ButtonType encerrarBtn = new ButtonType("Encerrar");
        alert.getButtonTypes().setAll(novoJogoBtn, encerrarBtn);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == novoJogoBtn) {
            controller.reiniciarJogo();
        } else {
            Stage stage = (Stage) tabuleiroGrid.getScene().getWindow();
            stage.close();
        }
    }

    private void atualizarLabels(Jogo jogo) {
        labelJogadorAtual.setText((jogo.getJogadorAtual() == Jogador.BRANCO) ? "Branco" : "Marrom");
        labelFase.setText((jogo.getFase() == Jogo.Fase.COLOCACAO) ? "Colocacao" : "Movimento");
        labelMensagem.setText((jogo.getFase() == Jogo.Fase.COLOCACAO) ? "Posicione suas pecas." : "Mova uma peca.");
    }

    private Node getNodeByRowColumnIndex(final int row, final int column) {
        for (Node node : tabuleiroGrid.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row &&
                    GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }
}