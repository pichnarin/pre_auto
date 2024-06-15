package org.nome.pre_auto.automata_sim;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.nome.pre_auto.PrimaryData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserInputApplication extends Application {

    private TextField stateField;
    private TextField alphabetField;
    private TextField startStateField;
    private TextField finalStateField;
    private TextArea transitionTextAreald;
    private TextField testStringField;
    private Label resultLabel;
    private TextArea resultTextArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Input Application");

        // Initialize GUI components
        stateField = new TextField();
        stateField.setPromptText("Enter state");

        alphabetField = new TextField();
        alphabetField.setPromptText("Enter alphabet");

        startStateField = new TextField();
        startStateField.setPromptText("Enter start state");

        finalStateField = new TextField();
        finalStateField.setPromptText("Enter final state");

        transitionTextAreald = new TextArea();
        transitionTextAreald.setPromptText("Enter transition");

        testStringField = new TextField();
        testStringField.setPromptText("Enter string to test");

        resultTextArea = new TextArea();
        resultTextArea.setPromptText("Results");

        Button submitButton = getButton();

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(stateField, alphabetField, startStateField, finalStateField,transitionTextAreald, testStringField, submitButton, resultTextArea);

        Scene scene = new Scene(root, 1000, 850);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button getButton() {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(_ -> {
            // Handle the user's input here
            Set<String> state = new HashSet<>(Arrays.asList(stateField.getText().split(",")));
            Set<String> alphabet = new HashSet<>(Arrays.asList(alphabetField.getText().split(",")));
            String startState = startStateField.getText();
            Set<String> finalState = new HashSet<>(Arrays.asList(finalStateField.getText().split(",")));
            Set<String> transition = new HashSet<>(Arrays.asList(transitionTextAreald.getText().split(",")));
            Set<String> initial_string = new HashSet<>(Arrays.asList(testStringField.getText().split(",")));
            // You can now use these values in your application
            PrimaryData primaryData = new PrimaryData(state, alphabet, startState, finalState, transition, initial_string);

            // Test the input strings
            String[] testStrings = testStringField.getText().split(",");
            resultTextArea.clear(); // Clear previous results
            for (String testString : testStrings) {
                boolean isAccepted = primaryData.isStringAccepted(testString);
                resultTextArea.appendText(testString + " is " + (isAccepted ? "Accepted" : "Rejected") + "\n");
            }
        });
        return submitButton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}