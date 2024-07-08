package org.nome.pre_auto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ConstructDfaController {

    @FXML
    private TextField inAlphabet;

    @FXML
    private TextField inEState;

    @FXML
    private TextArea inETransition;

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
        resetBtn.setOnAction(_->{
            inAlphabet.clear();
            inEState.clear();
            inETransition.clear();
            inFinalState.clear();
            inStartState.clear();
            inState.clear();
            inString.clear();
            inTransition.clear();
            outResult.clear();
        });
    }

    private void initializeSubmitBtn() {
        submitBtn.setOnAction(_->{

                Set<String> state = new HashSet<>(Arrays.asList(inState.getText().split(",")));
                Set<String> alphabet = new HashSet<>(Arrays.asList(inAlphabet.getText().split(",")));
                String startState = inStartState.getText();
                Set<String> finalState = new HashSet<>(Arrays.asList(inFinalState.getText().split(",")));
                Set<String> transition = new HashSet<>(Arrays.asList(inTransition.getText().split(",")));
                Set<String> epsilonTransition = new HashSet<>(Arrays.asList(inETransition.getText().split(",\\s*")));

                Set<String> strings = new HashSet<>(Arrays.asList(inETransition.getText().split(",")));

                transition.addAll(epsilonTransition);

                // Validate the input
                if (state.isEmpty() || alphabet.isEmpty() || startState.isEmpty() || finalState.isEmpty() || transition.isEmpty() || strings.isEmpty()) {
                    outResult.setText("Please fill all the fields");
                    return;
                } else if (!state.contains(startState)) {
                    outResult.setText("Start state must be one of the states");
                    return;
                } else if (!state.containsAll(finalState)) {
                    outResult.setText("Final state must be from the states");
                    return;
                }

                ConstructDfa constructDfa = new ConstructDfa(state, alphabet, startState, finalState, transition, "e", epsilonTransition);
                PrimaryData data = new PrimaryData(state, alphabet, startState, finalState, transition, strings);

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

                String getStartStart = constructDfa.findStartState().toString();
                String getFinalState = constructDfa.findFinalState().toString();
                String getTransitionFunction = constructDfa.findTransitionFunction().toString();

                outResult.setText("The DFA output after constructed:\n\nStart State: %s\n\nFinal State: %s\n\nTransition Function: %s\n\nString result: \n%s".formatted(getStartStart, getFinalState, getTransitionFunction, testStringsResult));

        });
    }
}
