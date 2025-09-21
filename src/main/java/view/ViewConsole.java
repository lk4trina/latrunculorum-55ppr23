package view;

import controller.ControllerJogo;
import model.*;
import controller.Observador;

import java.util.Scanner;

public class ViewConsole implements Observador<Jogo> {
    private final ControllerJogo controller;

    public ViewConsole(ControllerJogo controller) {
        this.controller = controller;
    }

    public void iniciar() {
        Scanner s = new Scanner(System.in);
        while (controller.ehFaseDeColocacao()) {
            System.out.println("Jogador atual: " + controller.getJogadorAtual());
            System.out.print("Linha: ");
            int lin = s.nextInt();
            System.out.print("Coluna: ");
            int col = s.nextInt();

            // definindo tipo da peça com base em quantas foram colocadas
            PecaTipo tipo = PecaTipo.SOLDADO;

            controller.lugarPeca(lin, col, tipo);
        }
    }

    @Override
    public void atualizar(Jogo jogo) {
        jogo.getTabuleiro().exibirTabuleiro();
    }
}

