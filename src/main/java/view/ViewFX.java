package view;

import controller.ControllerJogo;
import model.Jogo;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.net.URL;

public class ViewFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Jogo jogo = new Jogo(8);
        ControllerJogo controller = new ControllerJogo(jogo);

        LatrunculoView view = new LatrunculoView(controller);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Latrunculo.fxml"));
        loader.setController(view);

        Parent root = loader.load();

        Scene scene = new Scene(root);

        URL cssUrl = getClass().getResource("/view/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Aviso: style.css nao encontrado.");
        }

        primaryStage.setTitle("Latrunculo");
        primaryStage.setScene(scene);
        primaryStage.show();

        jogo.addObserver(view);
        // A linha controller.iniciarJogo() foi removida pois o jogo ja inicia
        // e notifica os observadores atraves do construtor de Jogo.
    }

    public static void main(String[] args) {
        launch(args);
    }
}