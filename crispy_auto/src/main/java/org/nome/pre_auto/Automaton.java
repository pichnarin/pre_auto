package org.nome.pre_auto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Automaton extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader
                (Automaton.class.getResource("pre_auto_version_1.0.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Automaton Simulation!");
        stage.getIcons().add(new Image("https://cdn-icons-png.flaticon.com/512/426/426224.png"));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}