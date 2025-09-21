package view;

import controller.ControllerJogo;
import model.Jogo;
import view.ViewConsole;

public class ViewPrincipal {
    public static void main(String[] args) {
        Jogo jogo = new Jogo(8); // Tabuleiro 8x8
        ControllerJogo controller = new ControllerJogo(jogo);
        ViewConsole view = new ViewConsole(controller);

        jogo.addObserver(view); // registrar view como observador do modelo
        view.iniciar();
    }
}
