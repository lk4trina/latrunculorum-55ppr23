package view;

import controller.ControllerJogo;
import model.Jogo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;

public class ViewFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Jogo jogo = new Jogo(8);
        ControllerJogo controller = new ControllerJogo(jogo);

        LatrunculoView view = new LatrunculoView(controller);

        FXMLLoader loader = new FXMLLoader(Thread.currentThread().getContextClassLoader().getResource("view/Latrunculo.fxml"));

        loader.setController(view);

        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Latrunculo - UDESC MVC");
        primaryStage.setScene(scene);
        primaryStage.show();

        jogo.addObserver(view);

        controller.iniciarJogo();
    }

    public static void main(String[] args) {
        launch(args);
    }
}