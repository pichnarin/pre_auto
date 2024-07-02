package org.nome.pre_auto;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;

import java.awt.image.BufferedImage;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

public class test extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create a simple graph
            MutableGraph g = (MutableGraph) graph("example").directed()
                    .with(node("a").link(node("b")));

            // Generate the graph image
            BufferedImage bufferedImage = Graphviz.fromGraph(g).width(200).render(Format.PNG).toImage();

            // Convert BufferedImage to JavaFX Image
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            // Display the image in a JavaFX ImageView
            ImageView imageView = new ImageView(image);
            VBox root = new VBox(imageView);

            Scene scene = new Scene(root, 400, 400);

            primaryStage.setTitle("Graphviz with JavaFX");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
