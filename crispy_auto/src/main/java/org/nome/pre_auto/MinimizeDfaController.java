package org.nome.pre_auto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MinimizeDfaController {

    @FXML
    private TextField inAlphabet;

    @FXML
    private TextField inFinalState;

    @FXML
    private TextField inStartState;

    @FXML
    private TextField inState;

    @FXML
    private TextField inString;

    @FXML
    private TextArea inTransition;

    @FXML
    private ImageView outGraph;

    @FXML
    private TextArea outResult;

    @FXML
    private Button resetBtn;

    @FXML
    private Button submitBtn;

    @FXML
    private Button backToOptBtn;

    private Stage stage;

    private Parent parent;
    //switch scene forwards and backwards
    @FXML
    public void btnOnClick(ActionEvent event){
        try{
            if(event.getSource() == backToOptBtn){
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("optional.fxml")));
            }

            assert parent != null;
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void initialize(){
        initializeSubmitBtn();
        initializeResetBtn();
    }

    private void initializeResetBtn() {
        resetBtn.setOnAction(_ -> {
            inState.clear();
            inAlphabet.clear();
            inStartState.clear();
            inFinalState.clear();
            inTransition.clear();
            inString.clear();
            outResult.clear();
            outGraph.setImage(null);
        });
    }

    private void initializeSubmitBtn() {
        submitBtn.setOnAction(_ -> {

                Set<String> states = new HashSet<>(Arrays.asList(inState.getText().split(",")));
                Set<String> alphabet = new HashSet<>(Arrays.asList(inAlphabet.getText().split(",")));
                String startState = inStartState.getText();
                Set<String> finalStates = new HashSet<>(Arrays.asList(inFinalState.getText().split(",")));
                Set<String> transitions = new HashSet<>(Arrays.asList(inTransition.getText().split(",\\s*")));

                Set<String> strings = new HashSet<>(Arrays.asList(inString.getText().split(",")));
                 // Validate the input
                if (states.isEmpty() || alphabet.isEmpty() || startState.isEmpty() || finalStates.isEmpty() || transitions.isEmpty()) {
                    outResult.setText("Please fill all the fields");
                    return;
                } else if (!states.contains(startState)) {
                    outResult.setText("Start state must be one of the states");
                    return;
                } else if (!states.containsAll(finalStates)) {
                    outResult.setText("Final state must be from the states");
                    return;
                }

                DesignFa data = new DesignFa(states, alphabet, startState, finalStates, transitions, strings);

                MinimizeDfa dfa = new MinimizeDfa(states, alphabet, startState, finalStates, transitions);
                MinimizeDfa minimizedDfa = dfa.minimize();

                // Test the input strings
                String[] testStrings = inString.getText().split(",");
                outResult.clear(); // Clear previous results
                StringBuilder testStringsResult = new StringBuilder();
                int i = 0;
                for (String testString : testStrings) {
                    boolean isAccepted = data.isStringAccepted(testString);
                    testStringsResult.append("String %d: %s have been %s\n".formatted(i + 1,testString ,isAccepted ? "Accepted" : "Rejected"));
                    i++;
                }

                String getDotScript = minimizedDfa.generateDotScript();
                String getOutput = "minimizedDfa.png";

                try {
                    minimizedDfa.GenerateImage(getDotScript, getOutput);
                    Image image = new Image(new FileInputStream(getOutput));
                    outGraph.setPreserveRatio(true);
                    outGraph.setImage(image);
                    GridPane.setHalignment(outGraph, HPos.CENTER); // Center horizontally in the cell
                    GridPane.setValignment(outGraph, VPos.CENTER); // Center vertically in the cell
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //display the output of minimized dfa and test strings result
                outResult.setText(String.format("Minimized DFA:\n\nReachable state: %s\n\nEquivalent states: %s\n\nStates: %s\n\nAlphabet: %s\n\nStart State: %s\n\nFinal States: %s\n\nTransitions: %s\n\nString result:\n%s",
                        dfa.getReachableStates(), dfa.findEquivalentStates(), minimizedDfa.state, minimizedDfa.alphabet, minimizedDfa.startState, minimizedDfa.finalState, minimizedDfa.transitions, testStringsResult));

        });
    }

}
